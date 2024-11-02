package com.example.scrollfeed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {
    Button started;
    EditText email, password, phoneNumber, username;
    FirebaseAuth auth;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);

        auth = FirebaseAuth.getInstance();
        userData = new UserData(this);

        started = findViewById(R.id.button);
        email = findViewById(R.id.email);
        password = findViewById(R.id.Password);
        phoneNumber = findViewById(R.id.number);
        username = findViewById(R.id.name);

        started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String userPhoneNumber = phoneNumber.getText().toString().trim();
                String userName = username.getText().toString().trim();

                if (!userEmail.isEmpty() && !userPassword.isEmpty() && !userPhoneNumber.isEmpty() && !userName.isEmpty()) {
                    auth.createUserWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(userName)
                                                .build();
                                        user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                userData.addUser(userName, userEmail, userPhoneNumber, userPassword);
                                                SessionManager sessionManager = new SessionManager(SignUp.this);
                                                sessionManager.setUserDetails(userName, userEmail, userPhoneNumber);

                                                startActivity(new Intent(SignUp.this, Categories.class));
                                                finish();
                                            } else {
                                                Toast.makeText(SignUp.this, "Failed to update user profile", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SignUp.this, "Please enter email, password, phone number, and username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}