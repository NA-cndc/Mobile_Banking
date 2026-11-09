package com.finalproject.bankapp;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    // Hàm dùng chung để cả nhóm gọi khi cần báo lỗi/thành công
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Sau này có thể thêm hàm showLoading(), hideLoading(), ẩn bàn phím...
}
