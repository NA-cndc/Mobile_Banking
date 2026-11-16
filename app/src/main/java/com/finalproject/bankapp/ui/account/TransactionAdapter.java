package com.finalproject.bankapp.ui.account;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.finalproject.bankapp.R;
import com.finalproject.bankapp.data.model.Transaction;
import com.finalproject.bankapp.utils.CurrencyUtils;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TxViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TxViewHolder holder, int position) {
        Transaction tx = transactionList.get(position);
        holder.tvTxDescription.setText(tx.getDescription());
        holder.tvTxTime.setText(tx.getTimestamp());

        if ("DEPOSIT".equals(tx.getType())) {
            holder.tvTxAmount.setText("+ " + CurrencyUtils.formatVND(tx.getAmount()));
            holder.tvTxAmount.setTextColor(Color.parseColor("#2E7D32")); // Xanh lá
        } else {
            holder.tvTxAmount.setText("- " + CurrencyUtils.formatVND(tx.getAmount()));
            holder.tvTxAmount.setTextColor(Color.parseColor("#C62828")); // Đỏ
        }
    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    static class TxViewHolder extends RecyclerView.ViewHolder {
        TextView tvTxDescription, tvTxTime, tvTxAmount;

        public TxViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTxDescription = itemView.findViewById(R.id.tvTxDescription);
            tvTxTime = itemView.findViewById(R.id.tvTxTime);
            tvTxAmount = itemView.findViewById(R.id.tvTxAmount);
        }
    }
}