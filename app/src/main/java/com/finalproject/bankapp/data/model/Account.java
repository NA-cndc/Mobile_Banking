package com.finalproject.bankapp.data.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String accountId;
    private String userId;
    private String accountType; // "CHECKING", "SAVING", hoặc "MORTGAGE"
    private double balance; // Số dư hiện tại
    private double interestRate; // Lãi suất (Dành cho Saving)
    private double monthlyPayment; // Số tiền phải trả hàng tháng (Dành cho Mortgage)

    public Account() {
    }

    public Account(String accountId, String userId, String accountType, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
    }

    // Generate toàn bộ Getter và Setter ở đây

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }
}
