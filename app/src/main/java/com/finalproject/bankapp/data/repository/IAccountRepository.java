package com.finalproject.bankapp.data.repository;

import com.finalproject.bankapp.data.model.Account;
import com.finalproject.bankapp.utils.DataCallback;
import java.util.List;

public interface IAccountRepository {
    // Lấy danh sách tài khoản theo ID người dùng (Phục vụ Yêu cầu 3)
    void getAccounts(String userId, DataCallback<List<Account>> callback);

    // Nạp tiền hoặc rút tiền
    void updateBalance(String accountId, double amount, DataCallback<Boolean> callback);
}
