package com.finalproject.bankapp.data.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String accountId;
    private String userId;
    private String accountType; // "CHECKING", "SAVING", "MORTGAGE"
    private double balance;
    private double interestRate;     // Lãi suất %/năm (cho SAVING)
    private double periodicPayment;  // Số tiền phải trả mỗi kỳ (cho MORTGAGE)
    private String paymentFrequency; // "MONTHLY" (hàng tháng) hoặc "BIWEEKLY" (mỗi 2 tuần)

    public Account() {}

    // Constructor đầy đủ
    public Account(String accountId, String userId, String accountType, double balance,
                   double interestRate, double periodicPayment, String paymentFrequency) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.interestRate = interestRate;
        this.periodicPayment = periodicPayment;
        this.paymentFrequency = paymentFrequency;
    }

    // Hàm tính lợi nhuận mỗi tháng cho tài khoản Saving
    public double getMonthlyProfit() {
        if ("SAVING".equals(accountType)) {
            return (balance * (interestRate / 100.0)) / 12.0;
        }
        return 0;
    }

    // Generate toàn bộ Getter và Setter bằng phím tắt Alt + Insert (hoặc Cmd + N)
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public double getPeriodicPayment() { return periodicPayment; }
    public void setPeriodicPayment(double periodicPayment) { this.periodicPayment = periodicPayment; }
    public String getPaymentFrequency() { return paymentFrequency; }
    public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }
}