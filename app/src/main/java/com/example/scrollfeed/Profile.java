package com.example.scrollfeed;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Profile extends AppCompatActivity {
    private ImageView profileImage;
    private EditText editTextName, editTextPhone, editTextEmail;
    private Button saveButton, backButton;
    private SessionManager sessionManager;
    private UserData userData;
    private TextView Logout;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        EdgeToEdge.enable(this);

        profileImage = findViewById(R.id.profile_image);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextTextEmail);
        saveButton = findViewById(R.id.Save);
        backButton = findViewById(R.id.back);
        sessionManager = new SessionManager(this);
        userData = new UserData(this);
        Logout = findViewById(R.id.logout);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Categories.class);
                startActivity(intent);
                finish();
            }
        });

        // Load saved profile details
        loadProfileDetails();

        // Set up activity result launcher for image selection
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                                profileImage.setImageBitmap(bitmap);
                                saveProfileImage(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // Set up click listener for profile image
        profileImage.setOnClickListener(v -> openGallery());

        // Save button click listener
        saveButton.setOnClickListener(v -> saveProfileDetails());
    }

    private void loadProfileDetails() {
        Cursor cursor = userData.getUserByEmail(sessionManager.getEmail());
        if (cursor != null && cursor.moveToFirst()) {
            editTextName.setText(cursor.getString(cursor.getColumnIndex("username")));
            editTextPhone.setText(cursor.getString(cursor.getColumnIndex("phone_number")));
            editTextEmail.setText(cursor.getString(cursor.getColumnIndex("email")));

            String profileImageString = cursor.getString(cursor.getColumnIndex("profile_image"));
            if (profileImageString != null) {
                byte[] imageBytes = android.util.Base64.decode(profileImageString, android.util.Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                profileImage.setImageBitmap(bitmap);
            }

            cursor.close();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

    private void saveProfileImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        sessionManager.setProfileImage(encodedImage);
        userData.updateProfileImage(sessionManager.getEmail(), encodedImage);
    }

    private void saveProfileDetails() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            sessionManager.setUserDetails(name, email, phone);
            userData.updateUserDetails(name, email, phone);
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}