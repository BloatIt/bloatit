package com.bloatit.web.server;

import java.util.Date;

import com.bloatit.web.utils.i18n.Localizator;

public class Context {
    private static Session session = null;
    private static Localizator localizator = null;
    private static HttpHeader header = null;
    private static long currentTime = 0;

    public static void setSession(final Session session) {
        Context.session = session;
    }

    public static Session getSession() {
        return session;
    }

    /**
     * @see Localizator#tr(String)
     */
    public static String tr(final String str) {
        return localizator.tr(str);
    }

    /**
     * @see Localizator#trc(String, String)
     */
    public static String trc(final String context, final String str) {
        return localizator.trc(context, str);
    }

    /**
     * @see Localizator#tr(String, Object...)
     */
    public static String tr(final String str, Object... parameters) {
        return localizator.tr(str, parameters);
    }

    /**
     * @see Localizator#trn(String, String, long)
     */
    public static String trn(String singular, String plural, long amount) {
        return localizator.trn(singular, plural, amount);
    }

    /**
     * @see Localizator#trn(String, String, long, Object...)
     */
    public static String trn(String singular, String plural, long amount, Object... parameters) {
        return localizator.trn(singular, plural, amount, parameters);
    }

    public static void setLocalizator(Localizator localizator) {
        Context.localizator = localizator;
    }

    public static Localizator getLocalizator() {
        return localizator;
    }

    public static void setHeader(HttpHeader header) {
        Context.header = header;
    }

    public static HttpHeader getHeader() {
        return Context.header;
    }

    public static long getTime() {
        return currentTime;
    }

    public static void updateTime() {
        currentTime = new Date().getTime()/1000;
    }


}
