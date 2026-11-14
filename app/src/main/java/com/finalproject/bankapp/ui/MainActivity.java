package com.finalproject.bankapp.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.finalproject.bankapp.BaseActivity;
import com.finalproject.bankapp.R;
import com.finalproject.bankapp.ui.account.HomeFragment;
import com.finalproject.bankapp.ui.profile.ProfileFragment;
import com.finalproject.bankapp.ui.transfer.TransferFragment;
import com.finalproject.bankapp.ui.utilities.UtilitiesFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Load màn hình mặc định khi mới vào app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        // Bắt sự kiện chuyển tab
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_account) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_transfer) {
                selectedFragment = new TransferFragment();
            } else if (itemId == R.id.nav_utilities) {
                selectedFragment = new UtilitiesFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}