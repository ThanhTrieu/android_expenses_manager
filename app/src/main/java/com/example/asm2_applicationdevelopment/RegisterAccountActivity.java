package com.example.asm2_applicationdevelopment;

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

public class RegisterAccountActivity extends AppCompatActivity {
    private EditText edtUser, edtPass, edtEmail, edtPhone;
    private Button btnSignUp;
    public UserDatabase userDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtUser = findViewById(R.id.edtUser);
        edtPass =findViewById(R.id.edtPass);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignUp = findViewById(R.id.btnSubmit);
        userDatabase = new UserDatabase(RegisterAccountActivity.this);
        TextView btn = findViewById(R.id.tvAlreadyHaveAnAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterAccountActivity.this, LoginAccountActivity.class));
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
                Intent intent = new Intent(RegisterAccountActivity.this, LoginAccountActivity.class);
                startActivity(intent);
            }
        });
    }
    public void signUp(){
        String user = edtUser.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(user)){
            edtUser.setError("Username can be not empty");
            return;
        }
        if (TextUtils.isEmpty(pass)){
            edtPass.setError("Password can be not empty");
            return;
        }
        if (TextUtils.isEmpty(email)){
            edtEmail.setError("Email can be not empty");
            return;
        }
        if (TextUtils.isEmpty(phone)){
            edtPhone.setError("Phone can be not empty");
            return;
        }
        long insert = userDatabase.addNewUser(user, pass, email, phone);
        if (insert == -1){
            // loi
            Toast.makeText(RegisterAccountActivity.this, "Insert Failure", Toast.LENGTH_LONG).show();
            return;
        } else {
            // Thanh cong
            Toast.makeText(RegisterAccountActivity.this, "Insert Successfully", Toast.LENGTH_LONG).show();

        }

    }
}
