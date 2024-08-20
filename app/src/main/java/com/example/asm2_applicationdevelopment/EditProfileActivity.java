package com.example.asm2_applicationdevelopment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.UserDatabase;
import com.example.asm2_applicationdevelopment.Model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import android.content.SharedPreferences;
import android.net.Uri;

// Other imports remain the same

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREFS_NAME = "UserProfilePrefs";
    private static final String PROFILE_IMAGE_URI_KEY = "profile_image_uri";

    private ImageView imgProfile;
    private FloatingActionButton button;
    private EditText edtUsername, edtPass, edtEmail, edtPhone;
    private Button btnCancel, btnSave;
    private UserDatabase userDatabase;
    private String currentUsername;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imgProfile = findViewById(R.id.img_profile);
        button = findViewById(R.id.floatingActionButton);
        edtUsername = findViewById(R.id.edt_username);
        edtPass = findViewById(R.id.edt_pass);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);

        button.setOnClickListener(v -> ImagePicker.with(EditProfileActivity.this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        userDatabase = new UserDatabase(this);
        currentUsername = getIntent().getStringExtra("username");

        // Load current user info
        loadUserInfo(currentUsername);

        // Set click listeners
        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> updateUserProfile());
    }

    private void loadUserInfo(String username) {
        User user = userDatabase.getInfoUser(username, null);
        if (user != null) {
            edtUsername.setText(user.getUsername());
            edtPass.setText(user.getPassword());
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());

            // Load the image URI from SharedPreferences
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String imageUriString = prefs.getString(PROFILE_IMAGE_URI_KEY, null);
            if (imageUriString != null) {
                selectedImageUri = Uri.parse(imageUriString);
                imgProfile.setImageURI(selectedImageUri);
            }
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgProfile.setImageURI(selectedImageUri);
        }
    }

    private void updateUserProfile() {
        String username = edtUsername.getText().toString();
        String password = edtPass.getText().toString();
        String email = edtEmail.getText().toString();
        String phone = edtPhone.getText().toString();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(UserDatabase.USERNAME_COL, username);
        values.put(UserDatabase.PASSWORD_COL, password);
        values.put(UserDatabase.EMAIL_COL, email);
        values.put(UserDatabase.PHONE_COL, phone);

        SQLiteDatabase db = userDatabase.getWritableDatabase();
        int rowsAffected = db.update(UserDatabase.TABLE_NAME, values,
                UserDatabase.USERNAME_COL + " = ?", new String[]{currentUsername});

        if (rowsAffected > 0) {
            // Save the image URI to SharedPreferences
            if (selectedImageUri != null) {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PROFILE_IMAGE_URI_KEY, selectedImageUri.toString());
                editor.apply();
            }

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Return updated data to ProfileFragment
            Intent resultIntent = new Intent();
            resultIntent.putExtra("username", username);
            resultIntent.putExtra("password", password);
            resultIntent.putExtra("email", email);
            resultIntent.putExtra("phone", phone);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}

