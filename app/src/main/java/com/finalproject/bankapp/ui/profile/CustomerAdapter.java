package com.finalproject.bankapp.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.finalproject.bankapp.R;
import com.finalproject.bankapp.data.model.User;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<User> customerList;
    private OnCustomerActionListener listener;

    public interface OnCustomerActionListener {
        void onEditCustomer(User customer);
        void onCreateAccount(User customer);
        void onRegisterEkyc(User customer);
    }

    public CustomerAdapter(List<User> customerList, OnCustomerActionListener listener) {
        this.customerList = customerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        User customer = customerList.get(position);
        boolean hasEkyc = customer.getBiometricDataUrl() != null && !customer.getBiometricDataUrl().isEmpty();
        String ekycTag = hasEkyc ? " [Đã eKYC]" : " [Chưa eKYC]";

        holder.tvName.setText(customer.getFullName() + " (" + customer.getUserId() + ")" + ekycTag);
        holder.tvContact.setText("Email: " + customer.getEmail() + " | SĐT: " + customer.getPhoneNumber());

        holder.btnEkyc.setOnClickListener(v -> listener.onRegisterEkyc(customer));
        holder.btnEdit.setOnClickListener(v -> listener.onEditCustomer(customer));
        holder.btnCreateAccount.setOnClickListener(v -> listener.onCreateAccount(customer));
    }

    @Override
    public int getItemCount() {
        return customerList != null ? customerList.size() : 0;
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContact;
        Button btnEkyc, btnEdit, btnCreateAccount;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCustomerName);
            tvContact = itemView.findViewById(R.id.tvCustomerContact);
            btnEkyc = itemView.findViewById(R.id.btnEkycCustomer);
            btnEdit = itemView.findViewById(R.id.btnEditCustomer);
            btnCreateAccount = itemView.findViewById(R.id.btnCreateAccountForCustomer);
        }
    }
}