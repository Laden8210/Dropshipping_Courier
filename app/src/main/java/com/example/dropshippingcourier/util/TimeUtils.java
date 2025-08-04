package com.example.dropshippingcourier.util;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static String getRelativeTime(Date date) {
        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;

        // Convert to minutes
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + "m ago";
        }

        // Convert to hours
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        if (hours < 24) {
            return hours + "h ago";
        }

        // Convert to days
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        if (days < 7) {
            return days + "d ago";
        }

        // For older dates, use absolute format
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date);
    }
}