package com.example.smartpantry.util;

public final class FormatUtils {

    private FormatUtils() {
    }

    /** 2.0 -> "2", 0.5 -> "0.5". */
    public static String formatQuantity(double quantity) {
        if (quantity == Math.rint(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }
}
