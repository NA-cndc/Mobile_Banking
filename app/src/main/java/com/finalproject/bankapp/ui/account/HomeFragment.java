package com.finalproject.bankapp.ui.account;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.bankapp.R;
import com.finalproject.bankapp.data.model.Account;
import com.finalproject.bankapp.data.model.User;
import com.finalproject.bankapp.data.repository.IAccountRepository;
import com.finalproject.bankapp.data.repository.MockAccountRepositoryImpl;
import com.finalproject.bankapp.utils.DataCallback;
import com.finalproject.bankapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvWelcomeUser;
    private ProgressBar progressBar;
    private RecyclerView rvAccounts;
    private AccountAdapter accountAdapter;
    private List<Account> accountList = new ArrayList<>();
    private IAccountRepository accountRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvWelcomeUser = view.findViewById(R.id.tvWelcomeUser);
        progressBar = view.findViewById(R.id.progressBarAccounts);
        rvAccounts = view.findViewById(R.id.rvAccounts);

        accountRepository = new MockAccountRepositoryImpl();

        // Hiển thị tên người dùng từ SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            tvWelcomeUser.setText("Xin chào, " + currentUser.getFullName());
        }

        setupRecyclerView();
        loadAccounts();

        return view;
    }

    private void setupRecyclerView() {
        rvAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        accountAdapter = new AccountAdapter(accountList, new AccountAdapter.OnAccountActionListener() {
            @Override
            public void onDepositClick(Account account) {
                showAmountDialog(account, true);
            }

            @Override
            public void onWithdrawClick(Account account) {
                showAmountDialog(account, false);
            }

            @Override
            public void onHistoryClick(Account account) {
                Intent intent = new Intent(getActivity(), TransactionHistoryActivity.class);
                intent.putExtra("ACCOUNT_ID", account.getAccountId());
                intent.putExtra("ACCOUNT_TYPE", account.getAccountType());
                startActivity(intent);
            }
        });
        rvAccounts.setAdapter(accountAdapter);
    }

    private void loadAccounts() {
        progressBar.setVisibility(View.VISIBLE);
        User currentUser = SessionManager.getInstance().getCurrentUser();
        String userId = (currentUser != null) ? currentUser.getUserId() : "UID_001";

        accountRepository.getAccounts(userId, new DataCallback<List<Account>>() {
            @Override
            public void onSuccess(List<Account> data) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                accountList.clear();
                accountList.addAll(data);
                accountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hộp thoại nhập số tiền Nạp / Rút
    private void showAmountDialog(Account account, boolean isDeposit) {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(isDeposit ? "Nạp tiền vào " + account.getAccountId() : "Rút tiền từ " + account.getAccountId());

        // Tạo ô nhập số tiền có padding gọn gàng
        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(48, 24, 48, 8);

        final EditText edtAmount = new EditText(getContext());
        edtAmount.setHint("Nhập số tiền (VND)");
        edtAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        container.addView(edtAmount);
        builder.setView(container);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String amountStr = edtAmount.getText().toString().trim();
            if (amountStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount <= 0) {
                Toast.makeText(getContext(), "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            executeTransaction(account.getAccountId(), amount, isDeposit);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void executeTransaction(String accountId, double amount, boolean isDeposit) {
        progressBar.setVisibility(View.VISIBLE);
        DataCallback<Boolean> callback = new DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), isDeposit ? "Nạp tiền thành công!" : "Rút tiền thành công!", Toast.LENGTH_SHORT).show();
                // Tải lại danh sách để cập nhật số dư và lợi nhuận mới
                loadAccounts();
            }

            @Override
            public void onError(String errorMessage) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        };

        if (isDeposit) {
            accountRepository.deposit(accountId, amount, callback);
        } else {
            accountRepository.withdraw(accountId, amount, callback);
        }
    }
}