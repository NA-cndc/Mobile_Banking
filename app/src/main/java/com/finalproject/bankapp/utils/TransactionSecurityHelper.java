package com.finalproject.bankapp.utils;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.finalproject.bankapp.data.model.User;

import java.util.Random;
import java.util.concurrent.Executor;

public class TransactionSecurityHelper {

    public static final double HIGH_VALUE_THRESHOLD = 10000000.0; // Ngưỡng 10 triệu VND

    public interface SecurityCallback {
        void onVerified();
    }

    public static void verifyTransaction(FragmentActivity activity, double amount, SecurityCallback callback) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(activity, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Bước 1: Nếu là giao dịch giá trị cao (>= 10 triệu), bắt buộc xác thực eKYC / Sinh trắc học (Yêu cầu 2)
        if (amount >= HIGH_VALUE_THRESHOLD) {
            String ekycData = currentUser.getBiometricDataUrl();
            if (ekycData == null || ekycData.isEmpty()) {
                new AlertDialog.Builder(activity)
                        .setTitle("Yêu cầu xác thực eKYC")
                        .setMessage("Giao dịch từ 10.000.000 VND trở lên yêu cầu định danh khuôn mặt eKYC.\n\nVui lòng đăng nhập bằng tài khoản Nhân viên (admin) để đăng ký ảnh eKYC cho khách hàng này trước.")
                        .setPositiveButton("Đã hiểu", null)
                        .show();
                return;
            }
            verifyBiometricOrEkycFace(activity, ekycData, () -> promptTwoFactorOtp(activity, callback));
        } else {
            // Giao dịch thông thường -> đi thẳng tới bước 2FA (OTP)
            promptTwoFactorOtp(activity, callback);
        }
    }

    private static void verifyBiometricOrEkycFace(FragmentActivity activity, String base64Face, Runnable onBiometricSuccess) {
        BiometricManager biometricManager = BiometricManager.from(activity);
        int canAuth = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);

        if (canAuth == BiometricManager.BIOMETRIC_SUCCESS) {
            Executor executor = ContextCompat.getMainExecutor(activity);
            BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(activity, "Xác thực sinh trắc học eKYC thành công!", Toast.LENGTH_SHORT).show();
                    onBiometricSuccess.run();
                }

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(activity, "Hủy xác thực: " + errString, Toast.LENGTH_SHORT).show();
                }
            });

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Xác thực eKYC giao dịch giá trị cao")
                    .setSubtitle("Sử dụng khuôn mặt hoặc vân tay để xác minh danh tính")
                    .setNegativeButtonText("Hủy")
                    .build();

            biometricPrompt.authenticate(promptInfo);
        } else {
            // Fallback cho máy ảo chưa cài vân tay/khuôn mặt: Hiển thị ảnh eKYC đã đăng ký để đối chiếu
            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(48, 24, 48, 16);

            TextView tvInfo = new TextView(activity);
            tvInfo.setText("Hệ thống đối chiếu khuôn mặt eKYC đã lưu cho giao dịch giá trị cao:");
            layout.addView(tvInfo);

            try {
                byte[] decodedBytes = Base64.decode(base64Face, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if (bmp != null) {
                    ImageView ivFace = new ImageView(activity);
                    ivFace.setImageBitmap(bmp);
                    ivFace.setPadding(0, 24, 0, 24);
                    layout.addView(ivFace);
                }
            } catch (Exception ignored) {}

            new AlertDialog.Builder(activity)
                    .setTitle("Xác minh khuôn mặt eKYC")
                    .setView(layout)
                    .setPositiveButton("Xác nhận khớp khuôn mặt", (d, w) -> onBiometricSuccess.run())
                    .setNegativeButton("Hủy", null)
                    .show();
        }
    }

    private static void promptTwoFactorOtp(FragmentActivity activity, SecurityCallback callback) {
        // Sinh mã OTP 6 số ngẫu nhiên giả lập gửi về SĐT khách hàng
        String generatedOtp = String.format("%06d", new Random().nextInt(999999));
        Toast.makeText(activity, "[SMS OTP] Mã xác thực giao dịch của bạn là: " + generatedOtp, Toast.LENGTH_LONG).show();

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 8);

        TextView tvHint = new TextView(activity);
        tvHint.setText("Nhập mã OTP 6 số vừa gửi (Test nhanh: " + generatedOtp + ")");
        layout.addView(tvHint);

        EditText edtOtp = new EditText(activity);
        edtOtp.setHint("Nhập mã OTP");
        edtOtp.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(edtOtp);

        new AlertDialog.Builder(activity)
                .setTitle("Xác thực bảo mật 2FA")
                .setView(layout)
                .setCancelable(false)
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    String input = edtOtp.getText().toString().trim();
                    if (generatedOtp.equals(input)) {
                        callback.onVerified();
                    } else {
                        Toast.makeText(activity, "Mã OTP không chính xác! Giao dịch bị từ chối.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
