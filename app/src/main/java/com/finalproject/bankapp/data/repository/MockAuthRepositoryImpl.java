package com.finalproject.bankapp.data.repository;

import android.os.Handler;
import android.os.Looper;
import com.finalproject.bankapp.data.model.User;
import com.finalproject.bankapp.utils.DataCallback;

public class MockAuthRepositoryImpl implements IAuthRepository {
    @Override
    public void login(String username, String password, DataCallback<User> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Giả lập logic: Nếu username có chữ "admin" thì là Officer, ngược lại là Customer
            // Trong hàm login() của MockAuthRepositoryImpl:
            if (username.contains("admin")) {
                User officer = MockUserRepositoryImpl.getUserByIdSync("UID_999");
                callback.onSuccess(officer);
            } else if (username.equals("test") && password.equals("123")) {
                User customer = MockUserRepositoryImpl.getUserByIdSync("UID_001");
                callback.onSuccess(customer);
            } else {
                callback.onError("Sai tài khoản hoặc mật khẩu");
            }
        }, 1000);
    }

    @Override
    public void sendOTP(String phoneNumber, DataCallback<Boolean> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> callback.onSuccess(true), 1000);
    }
}
