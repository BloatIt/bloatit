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
package com.bloatit.framework.webprocessor.context;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.bloatit.framework.utils.i18n.Localizator;

/**
 * <p>
 * A class that stores <b>all</b> the information about the current request..
 * The data are local to a thread.
 * </p>
 */
public class Context {
    private static final int MILLISECOND_DIV = 1000;

    private static String globalNotification;

    static class ContextData {
        private Session session = null;
        private Localizator localizator = null;
        private WebHeader header = null;
    }

    static class UniqueThreadContext {
        private static final ThreadLocal<ContextData> uniqueContextData = new ThreadLocal<ContextData>() {
            @Override
            protected ContextData initialValue() {
                return new ContextData();
            }
        };

        private static ContextData getContext() {
            return uniqueContextData.get();
        }
    } // UniqueThreadContext

    // Realy static
    private static AtomicLong requestTime = new AtomicLong(getCurrentTime());

    private Context() {
        // disable CTOR.
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

    public static WebHeader getHeader() {
        return UniqueThreadContext.getContext().header;
    }

    protected static long getResquestTime() {
        return Context.requestTime.get();
    }

    public static void reInitializeContext(final WebHeader header, final Session session) {
        updateTime();
        setHeader(header);
        setSession(session);
        setLocalizator(new Localizator(header.getLanguage(), header.getHttpHeader().getHttpAcceptLanguage()));
    }

    private static void updateTime() {
        Context.requestTime.set(getCurrentTime());
    }

    private static long getCurrentTime() {
        return new Date().getTime() / MILLISECOND_DIV;
    }

    private static void setHeader(final WebHeader header) {
        UniqueThreadContext.getContext().header = header;
    }

    private static void setLocalizator(final Localizator localizator) {
        UniqueThreadContext.getContext().localizator = localizator;
    }

    private static void setSession(final Session session) {
        UniqueThreadContext.getContext().session = session;
    }

    public static synchronized void setGlobalNotification(String globalNotification) {
        Context.globalNotification = globalNotification;
    }

    public static synchronized String getGlobalNotification() {
        return Context.globalNotification;
    }
}
