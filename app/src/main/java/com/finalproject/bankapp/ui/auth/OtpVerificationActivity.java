package com.finalproject.bankapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.finalproject.bankapp.BaseActivity;
import com.finalproject.bankapp.R;
import com.finalproject.bankapp.ui.MainActivity;

public class OtpVerificationActivity extends BaseActivity {

    private EditText edtOtp;
    private Button btnVerifyOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        edtOtp = findViewById(R.id.edtOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = edtOtp.getText().toString().trim();
            // Giả lập kiểm tra mã OTP
            if ("123456".equals(otp)) {
                showToast("Xác thực thành công!");
                Intent intent = new Intent(OtpVerificationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                showToast("Mã OTP không chính xác (Thử nhập 123456)");
            }
        });
    }
}