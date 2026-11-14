package com.finalproject.bankapp.data.repository;

import com.finalproject.bankapp.data.model.User;
import com.finalproject.bankapp.utils.DataCallback;

public interface IAuthRepository {
    // Hàm đăng nhập cơ bản
    void login(String username, String password, DataCallback<User> callback);
    // Hàm gửi OTP phục vụ 2FA
    void sendOTP(String phoneNumber, DataCallback<Boolean> callback);
}
