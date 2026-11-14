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
            if (username.contains("admin")) {
                callback.onSuccess(new User("UID_999", "Nguyen Van Officer", "OFFICER"));
            } else if (username.equals("test") && password.equals("123")) {
                callback.onSuccess(new User("UID_001", "Tran Van Customer", "CUSTOMER"));
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
