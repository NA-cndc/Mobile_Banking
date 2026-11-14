package com.finalproject.bankapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.finalproject.bankapp.BaseActivity;
import com.finalproject.bankapp.R;
import com.finalproject.bankapp.data.model.User;
import com.finalproject.bankapp.data.repository.IAuthRepository;
import com.finalproject.bankapp.data.repository.MockAuthRepositoryImpl;
import com.finalproject.bankapp.utils.DataCallback;
import com.finalproject.bankapp.utils.SessionManager;

public class LoginActivity extends BaseActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private IAuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Khởi tạo Mock Repository (sau này đổi thành FirebaseAuthRepositoryImpl)
        authRepository = new MockAuthRepositoryImpl();

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showToast("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        btnLogin.setEnabled(false); // Khóa nút tránh bấm nhiều lần

        authRepository.login(username, password, new DataCallback<User>() {
            @Override
            public void onSuccess(User user) {
                btnLogin.setEnabled(true);
                // Lưu thông tin user (Customer hoặc Officer) vào Session dùng chung cho cả nhóm
                SessionManager.getInstance().loginUser(user);

                // Yêu cầu 2: Chuyển sang màn hình xác thực 2FA (OTP)
                Intent intent = new Intent(LoginActivity.this, OtpVerificationActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                btnLogin.setEnabled(true);
                showToast(errorMessage);
            }
        });
    }
}