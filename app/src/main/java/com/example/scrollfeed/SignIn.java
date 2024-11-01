package com.example.scrollfeed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    TextView signup;
    Button signin;
    EditText email, password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin);

        auth = FirebaseAuth.getInstance();

        signin = findViewById(R.id.login);
        email = findViewById(R.id.name);
        password = findViewById(R.id.Password);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    auth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {
                                        String username = user.getDisplayName();
                                        String userPhoneNumber = user.getPhoneNumber();

                                        SessionManager sessionManager = new SessionManager(SignIn.this);
                                        sessionManager.setUserDetails(username, userEmail, userPhoneNumber);

                                        startActivity(new Intent(SignIn.this, Categories.class));
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(SignIn.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SignIn.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                if (!userEmail.isEmpty()) {
                    auth.sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignIn.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignIn.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SignIn.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup = findViewById(R.id.signUp);
        signup.setOnClickListener(v ->
        {
            startActivity(new Intent(SignIn.this, SignUp.class));
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signin), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}