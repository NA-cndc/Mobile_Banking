package com.finalproject.bankapp.data.repository;

import android.os.Handler;
import android.os.Looper;
import com.finalproject.bankapp.data.model.Account;
import com.finalproject.bankapp.utils.DataCallback;
import java.util.ArrayList;
import java.util.List;

public class MockAccountRepositoryImpl implements IAccountRepository {

    @Override
    public void getAccounts(String userId, DataCallback<List<Account>> callback) {
        // Dùng Handler để giả lập độ trễ mạng (loading) khoảng 1.5 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            List<Account> mockAccounts = new ArrayList<>();
            // Tạo sẵn các loại tài khoản: Checking, Saving, Mortgage theo Yêu cầu 3
            mockAccounts.add(new Account("ACC_CHECK_01", userId, "CHECKING", 5000000.0));
            mockAccounts.add(new Account("ACC_SAVE_01", userId, "SAVING", 20000000.0));
            mockAccounts.add(new Account("ACC_MORT_01", userId, "MORTGAGE", -15000000.0));

            callback.onSuccess(mockAccounts);
        }, 1500);
    }

    @Override
    public void updateBalance(String accountId, double amount, DataCallback<Boolean> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Giả lập luôn thành công khi test
            callback.onSuccess(true);
        }, 1000);
    }
}
