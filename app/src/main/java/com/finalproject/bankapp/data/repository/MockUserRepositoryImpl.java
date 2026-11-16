package com.finalproject.bankapp.data.repository;

import android.os.Handler;
import android.os.Looper;
import com.finalproject.bankapp.data.model.User;
import com.finalproject.bankapp.utils.DataCallback;
import java.util.ArrayList;
import java.util.List;

public class MockUserRepositoryImpl implements IUserRepository {

    private static List<User> mockUsers = new ArrayList<>();
    private static boolean isInitialized = false;

    public MockUserRepositoryImpl() {
        if (!isInitialized) {
            mockUsers.add(new User("UID_001", "Tran Van Customer", "customer@tdtubank.vn", "0901234567", "CUSTOMER"));
            mockUsers.add(new User("UID_002", "Le Thi Khach Hang", "lethikhach@gmail.com", "0912345678", "CUSTOMER"));
            mockUsers.add(new User("UID_999", "Nguyen Van Officer", "officer@tdtubank.vn", "0909999999", "OFFICER"));
            isInitialized = true;
        }
    }

    // Hàm hỗ trợ lấy nhanh User theo ID cho MockAuthRepositoryImpl
    public static User getUserByIdSync(String userId) {
        new MockUserRepositoryImpl();
        for (User u : mockUsers) {
            if (u.getUserId().equals(userId)) return u;
        }
        return null;
    }

    @Override
    public void getAllCustomers(DataCallback<List<User>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            List<User> customers = new ArrayList<>();
            for (User u : mockUsers) {
                if ("CUSTOMER".equals(u.getRole())) {
                    customers.add(u);
                }
            }
            callback.onSuccess(customers);
        }, 400);
    }

    @Override
    public void createCustomer(User newUser, DataCallback<Boolean> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mockUsers.add(newUser);
            callback.onSuccess(true);
        }, 400);
    }

    @Override
    public void updateUserProfile(User updatedUser, DataCallback<Boolean> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            for (int i = 0; i < mockUsers.size(); i++) {
                if (mockUsers.get(i).getUserId().equals(updatedUser.getUserId())) {
                    mockUsers.set(i, updatedUser);
                    callback.onSuccess(true);
                    return;
                }
            }
            callback.onError("Không tìm thấy người dùng");
        }, 400);
    }
}
