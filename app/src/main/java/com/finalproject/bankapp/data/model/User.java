package com.finalproject.bankapp.data.model;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role; // Chỉ nhận giá trị: "CUSTOMER" hoặc "OFFICER"
    private String biometricDataUrl; // Link ảnh khuôn mặt dùng cho eKYC

    public User() {
        // Constructor rỗng bắt buộc cho Firebase
    }

    public User(String userId, String fullName, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
    }
    // Nhấn Alt + Insert (hoặc Ctrl + Enter trên Mac) để Generate toàn bộ Getter và Setter ở đây

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBiometricDataUrl() {
        return biometricDataUrl;
    }

    public void setBiometricDataUrl(String biometricDataUrl) {
        this.biometricDataUrl = biometricDataUrl;
    }
}