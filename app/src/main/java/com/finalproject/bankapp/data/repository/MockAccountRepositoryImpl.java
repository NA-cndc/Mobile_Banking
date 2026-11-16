package com.finalproject.bankapp.data.repository;

import android.os.Handler;
import android.os.Looper;
import com.finalproject.bankapp.data.model.Account;
import com.finalproject.bankapp.data.model.Transaction;
import com.finalproject.bankapp.utils.DataCallback;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MockAccountRepositoryImpl implements IAccountRepository {

    // Dùng static List để giữ dữ liệu không bị reset khi chuyển tab
    private static List<Account> mockAccounts = new ArrayList<>();
    private static List<Transaction> mockTransactions = new ArrayList<>();
    private static boolean isInitialized = false;

    public MockAccountRepositoryImpl() {
        if (!isInitialized) {
            // Khởi tạo đủ 3 loại tài khoản theo Yêu cầu 3
            mockAccounts.add(new Account("CHK-1001", "UID_001", "CHECKING", 15000000, 0, 0, null));
            mockAccounts.add(new Account("SAV-2002", "UID_001", "SAVING", 50000000, 6.5, 0, null));
            mockAccounts.add(new Account("MTG-3003", "UID_001", "MORTGAGE", 200000000, 0, 5500000, "MONTHLY"));

            // Khởi tạo vài giao dịch mẫu
            mockTransactions.add(new Transaction("TX01", "CHK-1001", "DEPOSIT", 15000000, "10/11/2026 09:00", "Mở tài khoản thanh toán"));
            mockTransactions.add(new Transaction("TX02", "SAV-2002", "DEPOSIT", 50000000, "12/11/2026 14:30", "Gửi tiết kiệm kỳ hạn"));
            isInitialized = true;
        }
    }

    @Override
    public void getAccounts(String userId, DataCallback<List<Account>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> callback.onSuccess(mockAccounts), 500);
    }

    @Override
    public void deposit(String accountId, double amount, DataCallback<Boolean> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            for (Account acc : mockAccounts) {
                if (acc.getAccountId().equals(accountId)) {
                    acc.setBalance(acc.getBalance() + amount);
                    addMockTransaction(accountId, "DEPOSIT", amount, "Nạp tiền vào tài khoản");
                    callback.onSuccess(true);
                    return;
                }
            }
            callback.onError("Không tìm thấy tài khoản");
        }, 500);
    }

    @Override
    public void withdraw(String accountId, double amount, DataCallback<Boolean> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            for (Account acc : mockAccounts) {
                if (acc.getAccountId().equals(accountId)) {
                    if (acc.getBalance() < amount) {
                        callback.onError("Số dư không đủ để rút!");
                        return;
                    }
                    acc.setBalance(acc.getBalance() - amount);
                    addMockTransaction(accountId, "WITHDRAW", amount, "Rút tiền từ tài khoản");
                    callback.onSuccess(true);
                    return;
                }
            }
            callback.onError("Không tìm thấy tài khoản");
        }, 500);
    }

    @Override
    public void getTransactionHistory(String accountId, DataCallback<List<Transaction>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            List<Transaction> result = new ArrayList<>();
            for (Transaction tx : mockTransactions) {
                if (tx.getAccountId().equals(accountId)) {
                    result.add(tx);
                }
            }
            callback.onSuccess(result);
        }, 400);
    }

    private void addMockTransaction(String accountId, String type, double amount, String desc) {
        String time = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        String txId = "TX" + System.currentTimeMillis();
        mockTransactions.add(0, new Transaction(txId, accountId, type, amount, time, desc));
    }
}