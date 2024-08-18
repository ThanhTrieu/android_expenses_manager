package com.example.asm2_applicationdevelopment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.UserDatabase;
import com.example.asm2_applicationdevelopment.Model.User;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvPass, tvEmail, tvPhone;
    private Button btnEditProfile;
    private ImageView imgProfile;
    private UserDatabase userDatabase;
    private String currentUsername;

    private static final String PREFS_NAME = "UserProfilePrefs";
    private static final String PROFILE_IMAGE_URI_KEY = "profile_image_uri";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize TextViews, ImageView, and Button
        tvUsername = view.findViewById(R.id.tv_username);
        tvPass = view.findViewById(R.id.tv_pass);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhone = view.findViewById(R.id.tv_phone);
        imgProfile = view.findViewById(R.id.img_profile);
        btnEditProfile = view.findViewById(R.id.btn_editProfile);

        userDatabase = new UserDatabase(getActivity());

        // Retrieve user data from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentUsername = bundle.getString("username", "");
            loadUserInfo(currentUsername);
        } else {
            // Log or handle the case where the Bundle is null
            tvUsername.setText("N/A");
            tvPass.setText("N/A");
            tvEmail.setText("N/A");
            tvPhone.setText("N/A");
        }

        // Set click listener for the Edit button
        btnEditProfile.setOnClickListener(v -> openEditProfileActivity());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentUsername != null) {
            loadUserInfo(currentUsername); // Reload user info when fragment resumes
        }
    }

    private void loadUserInfo(String username) {
        User user = userDatabase.getInfoUser(username, null); // Get user data from database
        if (user != null) {
            tvUsername.setText(user.getUsername());
            tvPass.setText(user.getPassword()); // Ensure getPassword() exists in your User class
            tvEmail.setText(user.getEmail());
            tvPhone.setText(user.getPhone());

            // Load the image URI from SharedPreferences
            SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, requireContext().MODE_PRIVATE);
            String imageUriString = prefs.getString(PROFILE_IMAGE_URI_KEY, null);
            if (imageUriString != null) {
                Uri imageUri = Uri.parse(imageUriString);
                imgProfile.setImageURI(imageUri);
            }
        } else {
            tvUsername.setText("N/A");
            tvPass.setText("N/A");
            tvEmail.setText("N/A");
            tvPhone.setText("N/A");
        }
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        String username = tvUsername.getText().toString();
        String password = tvPass.getText().toString();
        String email = tvEmail.getText().toString();
        String phone = tvPhone.getText().toString();

        // Log values
        Log.d("ProfileFragment", "Username: " + username);
        Log.d("ProfileFragment", "Password: " + password);
        Log.d("ProfileFragment", "Email: " + email);
        Log.d("ProfileFragment", "Phone: " + phone);

        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }
}
