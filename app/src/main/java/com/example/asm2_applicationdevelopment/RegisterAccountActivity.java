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
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignUp = findViewById(R.id.btnSubmit);

        userDatabase = new UserDatabase(RegisterAccountActivity.this);

        // Xử lý khi người dùng đã có tài khoản và muốn đăng nhập
        TextView btn = findViewById(R.id.tvAlreadyHaveAnAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterAccountActivity.this, LoginAccountActivity.class));
            }
        });

        // Xử lý khi người dùng nhấn nút "Sign Up"
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    // Phương thức đăng ký người dùng mới
    public void signUp() {
        String user = edtUser.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        // Kiểm tra các trường không được để trống
        if (TextUtils.isEmpty(user)) {
            edtUser.setError("Username cannot be empty");
            return;
        }

        // Kiểm tra xem tên người dùng đã tồn tại hay chưa
        if (userDatabase.isUsernameExists(user)) {
            edtUser.setError("Username already exists");
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            edtPass.setError("Password cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Phone cannot be empty");
            return;
        }
        long insert = userDatabase.addNewUser(user, pass, email, phone);
        if (insert == -1) {
            // Lỗi khi thêm người dùng
            Toast.makeText(RegisterAccountActivity.this, "Insert Failure", Toast.LENGTH_LONG).show();
        } else {
            // Thêm thành công
            Toast.makeText(RegisterAccountActivity.this, "Insert Successfully", Toast.LENGTH_LONG).show();
            // Chuyển đến màn hình đăng nhập
            Intent intent = new Intent(RegisterAccountActivity.this, LoginAccountActivity.class);
            startActivity(intent);
        }
    }
}
        // Thêm người dùng mới vào cơ sở dữ liệu