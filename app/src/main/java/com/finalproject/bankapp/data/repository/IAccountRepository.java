package com.finalproject.bankapp.data.repository;

import com.finalproject.bankapp.data.model.Account;
import com.finalproject.bankapp.utils.DataCallback;
import com.finalproject.bankapp.data.model.Transaction;
import java.util.List;

public interface IAccountRepository {
    void getAccounts(String userId, DataCallback<List<Account>> callback);
    void deposit(String accountId, double amount, DataCallback<Boolean> callback);
    void withdraw(String accountId, double amount, DataCallback<Boolean> callback);
    void getTransactionHistory(String accountId, DataCallback<List<Transaction>> callback);
}
