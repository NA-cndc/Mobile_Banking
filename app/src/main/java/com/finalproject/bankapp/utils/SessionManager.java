package com.finalproject.bankapp.utils;

import com.finalproject.bankapp.data.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Hàm gọi khi user login thành công
    public void loginUser(User user) {
        this.currentUser = user;
    }

    // Hàm gọi khi user logout
    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // Hàm tiện ích kiểm tra xem có phải Bank Officer không (Phục vụ Yêu cầu 1)
    public boolean isBankOfficer() {
        return currentUser != null && "OFFICER".equals(currentUser.getRole());
    }
}
