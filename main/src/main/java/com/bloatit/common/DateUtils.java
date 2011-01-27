package com.bloatit.common;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final int SECOND_PER_HOUR = 3600;
    public static final int SECOND_PER_DAY = SECOND_PER_HOUR * 24;
    public static final int SECOND_PER_WEEK = SECOND_PER_DAY * 7;

    public static Date yesterday() {
        return nowPlusSomeDays(-1);
    }

    public static Date tomorrow() {
        return nowPlusSomeDays(1);
    }

    public static Date nowPlusSomeSeconds(final int seconds) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * Return the date in <code>n</code> days. for example tomorrow = nowPlusSomeDays(1).
     */
    public static Date nowPlusSomeDays(final int n) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, n);
        return calendar.getTime();
    }

    public static Date nowPlusSomeMonth(final int month) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    public static Date nowPlusSomeYears(final int years) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

}
