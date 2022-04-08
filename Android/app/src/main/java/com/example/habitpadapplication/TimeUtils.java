package com.example.habitpadapplication;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    private static final byte SECONDS_IN_MINUTE = 60;
    private static final int MILLIS_IN_SECOND = 1000;
    private static final byte MINUTES_IN_HOUR = 60;
    private static final int MILLIS_IN_HOUR = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
    static final int MILLIS_IN_MINUTE = MILLIS_IN_SECOND * SECONDS_IN_MINUTE;

    static long getStartTimeOfTodayInUtc() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTimeInMillis(System.currentTimeMillis());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);

        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    static String getDisplayDuration(long duration) {
        boolean isMinus = false;
        if (duration < 0) {
            isMinus = true;
            duration = 0 - duration;
        }

        int hour = (int) (duration / MILLIS_IN_HOUR);
        duration -= ((long) hour * MILLIS_IN_HOUR);
        int minute = (int) (duration / MILLIS_IN_MINUTE);

        StringBuilder timeBuffer = new StringBuilder();


        if (hour == 1) {
            if (minute == 1) {
                timeBuffer.append(String.format(Locale.US,"%1$d hr %2$d min", hour, minute));
            } else if (minute > 1) {
                timeBuffer.append(String.format(Locale.US, "%1$d hr %2$d mins", hour, minute));
            } else {
                timeBuffer.append(String.format(Locale.US, "%d hr", hour));
            }
        } else if (hour > 1) {
            if (minute == 1) {
                timeBuffer.append(String.format(Locale.US, "%1$d hrs %2$d min", hour, minute));
            } else if (minute > 1) {
                timeBuffer.append(String.format(Locale.US, "%1$d hrs %2$d mins", hour, minute));
            } else {
                timeBuffer.append(String.format(Locale.US, "%d hrs", hour));
            }
        } else {
            if (minute == 1) {
                timeBuffer.append(String.format(Locale.US, "%d min", minute));
            } else {
                timeBuffer.append(String.format(Locale.US, "%d mins", minute));
            }
        }

        return (isMinus ? "-" : "") + timeBuffer.toString();
    }

}
