package com.finalproject.bankapp.data.repository;

import com.finalproject.bankapp.data.model.User;
import com.finalproject.bankapp.utils.DataCallback;
import java.util.List;

public interface IUserRepository {
    void getAllCustomers(DataCallback<List<User>> callback);
    void createCustomer(User newUser, DataCallback<Boolean> callback);
    void updateUserProfile(User updatedUser, DataCallback<Boolean> callback);
}
