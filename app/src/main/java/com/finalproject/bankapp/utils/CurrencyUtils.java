package com.finalproject.bankapp.utils;

import java.text.DecimalFormat;

public class CurrencyUtils {
    public static String formatVND(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " VND";
    }
}
