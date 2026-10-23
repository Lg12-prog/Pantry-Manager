package com.example.smartpantry.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtils {

    private DateUtils() {
    }

    /**
     * Formats an expiry date. Dates are stored as UTC-midnight milliseconds (this is what
     * MaterialDatePicker returns), so they must be formatted in UTC or the day can shift.
     */
    public static String formatDate(long utcMillis) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(utcMillis));
    }
}
