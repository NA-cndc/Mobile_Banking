package com.finalproject.bankapp.data.model;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String transactionId;
    private String accountId;
    private String type;        // "DEPOSIT" (Nạp), "WITHDRAW" (Rút), "TRANSFER" (Chuyển)
    private double amount;
    private String timestamp;
    private String description;

    public Transaction() {}

    public Transaction(String transactionId, String accountId, String type, double amount, String timestamp, String description) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
    }

    // Generate Getter & Setter
    public String getTransactionId() { return transactionId; }
    public String getAccountId() { return accountId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
}
