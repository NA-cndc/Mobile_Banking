package com.finalproject.bankapp.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.finalproject.bankapp.R;
import com.finalproject.bankapp.data.model.Account;
import com.finalproject.bankapp.data.model.User;
import com.finalproject.bankapp.data.repository.IAccountRepository;
import com.finalproject.bankapp.data.repository.IUserRepository;
import com.finalproject.bankapp.data.repository.MockAccountRepositoryImpl;
import com.finalproject.bankapp.data.repository.MockUserRepositoryImpl;
import com.finalproject.bankapp.ui.auth.LoginActivity;
import com.finalproject.bankapp.utils.DataCallback;
import com.finalproject.bankapp.utils.SessionManager;

import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView tvRoleBadge, tvUserId;
    private EditText edtName, edtEmail, edtPhone;
    private Button btnSaveProfile, btnManageCustomers, btnEditRates, btnLogout;
    private CardView cardOfficerPanel;

    private IUserRepository userRepository;
    private IAccountRepository accountRepository;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvRoleBadge = view.findViewById(R.id.tvProfileRoleBadge);
        tvUserId = view.findViewById(R.id.tvProfileUserId);
        edtName = view.findViewById(R.id.edtProfileName);
        edtEmail = view.findViewById(R.id.edtProfileEmail);
        edtPhone = view.findViewById(R.id.edtProfilePhone);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        cardOfficerPanel = view.findViewById(R.id.cardOfficerPanel);
        btnManageCustomers = view.findViewById(R.id.btnOfficerManageCustomers);
        btnEditRates = view.findViewById(R.id.btnOfficerEditRates);
        btnLogout = view.findViewById(R.id.btnLogout);

        userRepository = new MockUserRepositoryImpl();
        accountRepository = new MockAccountRepositoryImpl();
        currentUser = SessionManager.getInstance().getCurrentUser();

        bindUserData();
        setupEvents();

        return view;
    }

    private void bindUserData() {
        if (currentUser == null) return;

        tvUserId.setText("Mã định danh: " + currentUser.getUserId());
        edtName.setText(currentUser.getFullName());
        edtEmail.setText(currentUser.getEmail());
        edtPhone.setText(currentUser.getPhoneNumber());

        // Phân quyền hiển thị giao diện theo vai trò (Yêu cầu 1)
        if (SessionManager.getInstance().isBankOfficer()) {
            tvRoleBadge.setText("NHÂN VIÊN NGÂN HÀNG (OFFICER)");
            cardOfficerPanel.setVisibility(View.VISIBLE);
        } else {
            tvRoleBadge.setText("KHÁCH HÀNG (CUSTOMER)");
            cardOfficerPanel.setVisibility(View.GONE);
        }
    }

    private void setupEvents() {
        // Lưu thông tin cá nhân tự chỉnh sửa (Yêu cầu 1)
        btnSaveProfile.setOnClickListener(v -> {
            if (currentUser == null) return;
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng không để trống thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.setFullName(name);
            currentUser.setEmail(email);
            currentUser.setPhoneNumber(phone);

            userRepository.updateUserProfile(currentUser, new DataCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    if (!isAdded()) return;
                    Toast.makeText(getContext(), "Đã cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String errorMessage) {
                    if (!isAdded()) return;
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Mở màn hình quản lý khách hàng dành cho Officer (Yêu cầu 1)
        btnManageCustomers.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OfficerCustomerManagerActivity.class);
            startActivity(intent);
        });

        // Điều chỉnh lãi suất tài khoản tiết kiệm dành cho Officer (Yêu cầu 3)
        btnEditRates.setOnClickListener(v -> showEditSavingRateDialog());

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            SessionManager.getInstance().logout();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void showEditSavingRateDialog() {
        accountRepository.getAllSavingAccounts(new DataCallback<List<Account>>() {
            @Override
            public void onSuccess(List<Account> savings) {
                if (!isAdded() || getContext() == null) return;
                if (savings.isEmpty()) {
                    Toast.makeText(getContext(), "Chưa có tài khoản tiết kiệm nào trong hệ thống", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] items = new String[savings.size()];
                for (int i = 0; i < savings.size(); i++) {
                    Account acc = savings.get(i);
                    items[i] = acc.getAccountId() + " (Chủ TK: " + acc.getUserId() + ") - Lãi hiện tại: " + acc.getInterestRate() + "%";
                }

                new AlertDialog.Builder(getContext())
                        .setTitle("Chọn tài khoản Saving cần đổi lãi suất")
                        .setItems(items, (dialog, which) -> promptNewRateInput(savings.get(which)))
                        .show();
            }

            @Override
            public void onError(String errorMessage) {
                if (isAdded()) Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void promptNewRateInput(Account savingAccount) {
        if (getContext() == null) return;
        LinearLayout container = new LinearLayout(getContext());
        container.setPadding(48, 24, 48, 8);
        EditText edtRate = new EditText(getContext());
        edtRate.setHint("Nhập lãi suất mới (%/năm), VD: 7.2");
        edtRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtRate.setText(String.valueOf(savingAccount.getInterestRate()));
        container.addView(edtRate);

        new AlertDialog.Builder(getContext())
                .setTitle("Cập nhật lãi suất cho " + savingAccount.getAccountId())
                .setView(container)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    try {
                        double newRate = Double.parseDouble(edtRate.getText().toString().trim());
                        accountRepository.updateSavingInterestRate(savingAccount.getAccountId(), newRate, new DataCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean data) {
                                if (isAdded()) Toast.makeText(getContext(), "Đã cập nhật lãi suất thành " + newRate + "%/năm", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                if (isAdded()) Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Lãi suất không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}