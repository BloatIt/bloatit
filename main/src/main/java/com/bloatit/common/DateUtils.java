package com.bloatit.common;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    static Date tomorrow() {
        return nowPlusSomeDays(1);
    }

    static Date nowPlusSomeSeconds(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * Return the date in <code>n</code> days. for example tomorrow = nowPlusSomeDays(1).
     */
    static Date nowPlusSomeDays(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, n);
        return calendar.getTime();
    }

    static Date nowPlusSomeMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    static Date nowPlusSomeYears(int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

}
