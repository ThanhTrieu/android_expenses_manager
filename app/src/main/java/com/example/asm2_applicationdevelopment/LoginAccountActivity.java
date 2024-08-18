package com.example.asm2_applicationdevelopment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.UserDatabase;
import com.example.asm2_applicationdevelopment.Model.User;

public class LoginAccountActivity extends AppCompatActivity {
    private EditText edtUser, edtPass;
    private Button btnLogin;
    public UserDatabase userDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        TextView btn = findViewById(R.id.tvSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAccountActivity.this, RegisterAccountActivity.class));
            }
        });
        userDatabase = new UserDatabase(LoginAccountActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                if (TextUtils.isEmpty(user)){
                    edtUser.setError("Username cannot be empty");
                }
                if (TextUtils.isEmpty(pass)){
                    edtPass.setError("Password cannot be empty");
                    return;
                }
                User data = userDatabase.getInfoUser(user, pass);
                if (data != null && data.getEmail() != null && data.getId() > 0){
                    // Login successful
                    Intent intent = new Intent(LoginAccountActivity.this, MainActivity.class);
                    intent.putExtra("username", data.getUsername());
                    intent.putExtra("password", pass);
                    intent.putExtra("email", data.getEmail());
                    intent.putExtra("phone", data.getPhone());
                    Toast.makeText(LoginAccountActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else {
                    // Login failed
                    Toast.makeText(LoginAccountActivity.this, "Account Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}


