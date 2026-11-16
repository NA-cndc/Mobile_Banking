package com.finalproject.bankapp.data.model;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role; // "CUSTOMER" hoặc "OFFICER"
    private String biometricDataUrl; // Lưu trạng thái/ảnh eKYC cho Yêu cầu 2

    public User() {}

    public User(String userId, String fullName, String email, String phoneNumber, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getBiometricDataUrl() { return biometricDataUrl; }
    public void setBiometricDataUrl(String biometricDataUrl) { this.biometricDataUrl = biometricDataUrl; }
}