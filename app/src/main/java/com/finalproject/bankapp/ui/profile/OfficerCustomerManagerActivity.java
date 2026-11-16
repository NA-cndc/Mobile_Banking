package com.finalproject.bankapp.ui.profile;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.bankapp.BaseActivity;
import com.finalproject.bankapp.R;
import com.finalproject.bankapp.data.model.Account;
import com.finalproject.bankapp.data.model.User;
import com.finalproject.bankapp.data.repository.IAccountRepository;
import com.finalproject.bankapp.data.repository.IUserRepository;
import com.finalproject.bankapp.data.repository.MockAccountRepositoryImpl;
import com.finalproject.bankapp.data.repository.MockUserRepositoryImpl;
import com.finalproject.bankapp.utils.DataCallback;

import java.util.ArrayList;
import java.util.List;

public class OfficerCustomerManagerActivity extends BaseActivity {

    private RecyclerView rvCustomers;
    private Button btnAddCustomer;
    private CustomerAdapter adapter;
    private List<User> customerList = new ArrayList<>();

    private IUserRepository userRepository;
    private IAccountRepository accountRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_customer_manager);


        Toolbar toolbar = findViewById(R.id.toolbarOfficer);
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(Color.WHITE); // Đổi màu mũi tên sang trắng
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvCustomers = findViewById(R.id.rvCustomers);
        btnAddCustomer = findViewById(R.id.btnAddNewCustomer);

        userRepository = new MockUserRepositoryImpl();
        accountRepository = new MockAccountRepositoryImpl();
        btnAddCustomer.setOnClickListener(v -> showCustomerDialog(null));

        rvCustomers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter(customerList, new CustomerAdapter.OnCustomerActionListener() {
            @Override
            public void onEditCustomer(User customer) {
                showCustomerDialog(customer);
            }

            @Override
            public void onCreateAccount(User customer) {
                showCreateAccountDialog(customer);
            }
        });
        rvCustomers.setAdapter(adapter);

        loadCustomers();
    }

    private void loadCustomers() {
        userRepository.getAllCustomers(new DataCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                customerList.clear();
                customerList.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    // Hộp thoại Tạo mới hoặc Chỉnh sửa thông tin khách hàng (Yêu cầu 1)
    private void showCustomerDialog(User existingCustomer) {
        boolean isEdit = (existingCustomer != null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Sửa hồ sơ: " + existingCustomer.getUserId() : "Tạo hồ sơ Khách hàng mới");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 8);

        EditText edtName = new EditText(this);
        edtName.setHint("Họ và tên khách hàng");
        layout.addView(edtName);

        EditText edtEmail = new EditText(this);
        edtEmail.setHint("Email");
        edtEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(edtEmail);

        EditText edtPhone = new EditText(this);
        edtPhone.setHint("Số điện thoại");
        edtPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        layout.addView(edtPhone);

        if (isEdit) {
            edtName.setText(existingCustomer.getFullName());
            edtEmail.setText(existingCustomer.getEmail());
            edtPhone.setText(existingCustomer.getPhoneNumber());
        }

        builder.setView(layout);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                showToast("Vui lòng nhập đầy đủ thông tin");
                return;
            }

            if (isEdit) {
                existingCustomer.setFullName(name);
                existingCustomer.setEmail(email);
                existingCustomer.setPhoneNumber(phone);
                userRepository.updateUserProfile(existingCustomer, new DataCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        showToast("Đã cập nhật hồ sơ khách hàng!");
                        loadCustomers();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        showToast(errorMessage);
                    }
                });
            } else {
                String newId = "UID_" + (System.currentTimeMillis() % 10000);
                User newCustomer = new User(newId, name, email, phone, "CUSTOMER");
                userRepository.createCustomer(newCustomer, new DataCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        showToast("Đã tạo khách hàng " + newId);
                        loadCustomers();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        showToast(errorMessage);
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    // Hộp thoại Mở tài khoản ngân hàng mới cho khách hàng (Yêu cầu 1)
    private void showCreateAccountDialog(User customer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mở tài khoản cho " + customer.getFullName());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 8);

        Spinner spnType = new Spinner(this);
        String[] types = {"CHECKING", "SAVING", "MORTGAGE"};
        spnType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));
        layout.addView(spnType);

        EditText edtInitialBalance = new EditText(this);
        edtInitialBalance.setHint("Số dư ban đầu / Khoản vay (VND)");
        edtInitialBalance.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(edtInitialBalance);

        builder.setView(layout);
        builder.setPositiveButton("Tạo tài khoản", (dialog, which) -> {
            String type = spnType.getSelectedItem().toString();
            double amount = 0;
            try {
                amount = Double.parseDouble(edtInitialBalance.getText().toString().trim());
            } catch (NumberFormatException ignored) {}

            String accId = type.substring(0, 3) + "-" + (1000 + (int) (Math.random() * 9000));
            double interest = "SAVING".equals(type) ? 6.5 : 0;
            double periodic = "MORTGAGE".equals(type) ? (amount / 24.0) : 0;
            String cycle = "MORTGAGE".equals(type) ? "MONTHLY" : null;

            Account newAcc = new Account(accId, customer.getUserId(), type, amount, interest, periodic, cycle);
            accountRepository.createAccount(newAcc, new DataCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    showToast("Đã mở tài khoản " + accId + " (" + type + ")");
                }

                @Override
                public void onError(String errorMessage) {
                    showToast(errorMessage);
                }
            });
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}