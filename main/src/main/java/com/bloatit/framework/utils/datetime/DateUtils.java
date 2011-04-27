//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.utils.datetime;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    protected static final int SECOND_PER_MINUTE = 60;
    protected static final int SECOND_PER_HOUR = 3600;
    public static final int SECOND_PER_DAY = SECOND_PER_HOUR * 24;
    public static final int SECOND_PER_WEEK = SECOND_PER_DAY * 7;
    public static final int MILLISECOND_PER_SECOND = 1000;

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
     * Return the date in <code>n</code> days. for example tomorrow =
     * nowPlusSomeDays(1).
     */
    public static Date nowPlusSomeDays(final int n) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, n);
        return calendar.getTime();
    }

    public static Date nowMinusSomeDays(final int n) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -n);
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

    public static Date now() {
        return new Date();
    }

    public static Date flyingPigDate() {
        return nowPlusSomeYears(1000);
    }

    /**
     * In millisecond
     * 
     * @param from
     * @param to
     */
    public static long elapsed(final Date from, final Date to) {
        return to.getTime() - from.getTime();
    }

    public static boolean isInTheFuture(final Date date) {
        return date.after(now());
    }
}
