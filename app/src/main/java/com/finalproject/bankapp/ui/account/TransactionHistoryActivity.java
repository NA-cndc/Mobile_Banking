package com.finalproject.bankapp.ui.account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.finalproject.bankapp.BaseActivity;
import com.finalproject.bankapp.R;
import com.finalproject.bankapp.data.model.Transaction;
import com.finalproject.bankapp.data.repository.IAccountRepository;
import com.finalproject.bankapp.data.repository.MockAccountRepositoryImpl;
import com.finalproject.bankapp.utils.DataCallback;
import java.util.List;

public class TransactionHistoryActivity extends BaseActivity {

    private RecyclerView rvTransactions;
    private TextView tvEmptyHistory, tvHistoryHeader;
    private Button btnBackHistory;
    private IAccountRepository accountRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        rvTransactions = findViewById(R.id.rvTransactions);
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory);
        tvHistoryHeader = findViewById(R.id.tvHistoryHeader);
        btnBackHistory = findViewById(R.id.btnBackHistory);

        String accountId = getIntent().getStringExtra("ACCOUNT_ID");
        tvHistoryHeader.setText("Lịch sử: " + accountId);

        btnBackHistory.setOnClickListener(v -> finish());

        rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        accountRepository = new MockAccountRepositoryImpl();

        if (accountId != null) {
            loadTransactions(accountId);
        }
    }

    private void loadTransactions(String accountId) {
        accountRepository.getTransactionHistory(accountId, new DataCallback<List<Transaction>>() {
            @Override
            public void onSuccess(List<Transaction> data) {
                if (data == null || data.isEmpty()) {
                    tvEmptyHistory.setVisibility(View.VISIBLE);
                } else {
                    tvEmptyHistory.setVisibility(View.GONE);
                    rvTransactions.setAdapter(new TransactionAdapter(data));
                }
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }
}
