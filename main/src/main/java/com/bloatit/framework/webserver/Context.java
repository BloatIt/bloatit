package com.bloatit.framework.webserver;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.xcgiserver.HttpHeader;

/**
 * <p>
 * A class that stores <b>all</b> the information about the current request..
 * The data are local to a thread.
 * </p>
 */
public class Context {
    private static final int MILLISECOND_DIV = 1000;

    static class ContextData {
        public Session session = null;
        public Localizator localizator = null;
        public HttpHeader header = null;
    }

    static class UniqueThreadContext {
        private static final ThreadLocal<ContextData> uniqueContextData = new ThreadLocal<ContextData>() {
            @Override
            protected ContextData initialValue() {
                return new ContextData();
            }
        };

        public static ContextData getContext() {
            return uniqueContextData.get();
        }
    } // UniqueThreadContext

    // Realy static
    private static AtomicLong requestTime = new AtomicLong(getCurrentTime());

    private Context() {
        // desactivate CTOR.
    }

    public static Session getSession() {
        return UniqueThreadContext.getContext().session;
    }

    /**
     * @see Localizator#tr(String)
     */
    public static String tr(final String str) {
        return getLocalizator().tr(str);
    }

    /**
     * @see Localizator#trc(String, String)
     */
    public static String trc(final String context, final String str) {
        return getLocalizator().trc(context, str);
    }

    /**
     * @see Localizator#tr(String, Object...)
     */
    public static String tr(final String str, final Object... parameters) {
        return getLocalizator().tr(str, parameters);
    }

    /**
     * @see Localizator#trn(String, String, long)
     */
    public static String trn(final String singular, final String plural, final long amount) {
        return getLocalizator().trn(singular, plural, amount);
    }

    /**
     * @see Localizator#trn(String, String, long, Object...)
     */
    public static String trn(final String singular, final String plural, final long amount, final Object... parameters) {
        return getLocalizator().trn(singular, plural, amount, parameters);
    }

    /**
     * Returns the localizator for the current context
     */
    public static Localizator getLocalizator() {
        return UniqueThreadContext.getContext().localizator;
    }

    public static HttpHeader getHeader() {
        return UniqueThreadContext.getContext().header;
    }

    public static long getResquestTime() {
        return Context.requestTime.get();
    }

    static void reInitializeContext(final HttpHeader header, final Session session) {
        updateTime();
        setHeader(header);
        setSession(session);
        setLocalizator(new Localizator(header.getQueryString().getLanguage(), header.getHttpAcceptLanguage()));
    }

    private static void updateTime() {
        Context.requestTime.set(getCurrentTime());
    }

    private static long getCurrentTime() {
        return new Date().getTime() / MILLISECOND_DIV;
    }

    private static void setHeader(final HttpHeader header) {
        UniqueThreadContext.getContext().header = header;
    }

    private static void setLocalizator(final Localizator localizator) {
        UniqueThreadContext.getContext().localizator = localizator;
    }

    private static void setSession(final Session session) {
        UniqueThreadContext.getContext().session = session;
    }
}
