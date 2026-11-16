package com.finalproject.bankapp.ui.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.finalproject.bankapp.R;
import com.finalproject.bankapp.data.model.Account;
import com.finalproject.bankapp.utils.CurrencyUtils;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<Account> accountList;
    private OnAccountActionListener listener;

    public interface OnAccountActionListener {
        void onDepositClick(Account account);
        void onWithdrawClick(Account account);
        void onHistoryClick(Account account);
    }

    public AccountAdapter(List<Account> accountList, OnAccountActionListener listener) {
        this.accountList = accountList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.tvAccountId.setText(account.getAccountId());
        holder.tvBalance.setText(CurrencyUtils.formatVND(account.getBalance()));

        // Phân loại hiển thị thông tin riêng cho 3 loại tài khoản (Yêu cầu 3)
        switch (account.getAccountType()) {
            case "CHECKING":
                holder.tvAccountType.setText("TÀI KHOẢN THANH TOÁN (CHECKING)");
                holder.tvBalanceLabel.setText("Số dư khả dụng:");
                holder.tvExtraInfo.setVisibility(View.GONE);
                holder.btnWithdraw.setVisibility(View.VISIBLE);
                break;

            case "SAVING":
                holder.tvAccountType.setText("TÀI KHOẢN TIẾT KIỆM (SAVING)");
                holder.tvBalanceLabel.setText("Số dư tiết kiệm:");
                holder.tvExtraInfo.setVisibility(View.VISIBLE);
                String savingInfo = String.format("Lãi suất: %.1f%%/năm | Lợi nhuận/tháng: %s",
                        account.getInterestRate(), CurrencyUtils.formatVND(account.getMonthlyProfit()));
                holder.tvExtraInfo.setText(savingInfo);
                holder.btnWithdraw.setVisibility(View.VISIBLE);
                break;

            case "MORTGAGE":
                holder.tvAccountType.setText("TÀI KHOẢN VAY THẾ CHẤP (MORTGAGE)");
                holder.tvBalanceLabel.setText("Dư nợ còn lại:");
                holder.tvExtraInfo.setVisibility(View.VISIBLE);
                String cycle = "BIWEEKLY".equals(account.getPaymentFrequency()) ? "mỗi 2 tuần" : "hàng tháng";
                String mortgageInfo = String.format("Số tiền cần trả (%s): %s",
                        cycle, CurrencyUtils.formatVND(account.getPeriodicPayment()));
                holder.tvExtraInfo.setText(mortgageInfo);
                // Tài khoản vay không cho rút tiền mặt trực tiếp
                holder.btnWithdraw.setVisibility(View.GONE);
                break;
        }

        holder.btnDeposit.setOnClickListener(v -> listener.onDepositClick(account));
        holder.btnWithdraw.setOnClickListener(v -> listener.onWithdrawClick(account));
        holder.btnHistory.setOnClickListener(v -> listener.onHistoryClick(account));
    }

    @Override
    public int getItemCount() {
        return accountList != null ? accountList.size() : 0;
    }

    static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccountType, tvAccountId, tvBalanceLabel, tvBalance, tvExtraInfo;
        Button btnDeposit, btnWithdraw, btnHistory;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAccountType = itemView.findViewById(R.id.tvAccountType);
            tvAccountId = itemView.findViewById(R.id.tvAccountId);
            tvBalanceLabel = itemView.findViewById(R.id.tvBalanceLabel);
            tvBalance = itemView.findViewById(R.id.tvBalance);
            tvExtraInfo = itemView.findViewById(R.id.tvExtraInfo);
            btnDeposit = itemView.findViewById(R.id.btnDeposit);
            btnWithdraw = itemView.findViewById(R.id.btnWithdraw);
            btnHistory = itemView.findViewById(R.id.btnHistory);
        }
    }
}