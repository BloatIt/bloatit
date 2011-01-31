package com.bloatit.framework.webserver;

import java.util.Date;

import com.bloatit.framework.scgiserver.HttpHeader;
import com.bloatit.framework.utils.i18n.Localizator;

/**
 * <p>
 * A class that stores <b>all</b> the information about the current request
 * </p>
 */
public class Context {
    private static final int MILLISECOND_DIV = 1000;

    private static Session session = null;
    private static Localizator localizator = null;
    private static HttpHeader header = null;
    private static long currentTime = 0;

    private Context() {
        // desactivate CTOR.
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
    public static String tr(final String str, final Object... parameters) {
        return localizator.tr(str, parameters);
    }

    /**
     * @see Localizator#trn(String, String, long)
     */
    public static String trn(final String singular, final String plural, final long amount) {
        return localizator.trn(singular, plural, amount);
    }

    /**
     * @see Localizator#trn(String, String, long, Object...)
     */
    public static String trn(final String singular, final String plural, final long amount, final Object... parameters) {
        return localizator.trn(singular, plural, amount, parameters);
    }

    /**
     * Returns the localizator for the current context
     */
    public static Localizator getLocalizator() {
        return localizator;
    }

    public static HttpHeader getHeader() {
        return Context.header;
    }

    public static long getTime() {
        return Context.currentTime;
    }

    static void reInitializeContext(final HttpHeader header, final Session session) {
        updateTime();
        setHeader(header);
        setSession(session);
        setLocalizator(new Localizator(header.getQueryString().getLanguage(), header.getHttpAcceptLanguage()));
    }

    private static void updateTime() {
        Context.currentTime = new Date().getTime() / MILLISECOND_DIV;
    }

    private static void setHeader(final HttpHeader header) {
        Context.header = header;
    }

    private static void setLocalizator(final Localizator localizator) {
        Context.localizator = localizator;
    }

    private static void setSession(final Session session) {
        Context.session = session;
    }
}
