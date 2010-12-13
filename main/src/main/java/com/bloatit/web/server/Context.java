package com.bloatit.web.server;


public class Context {

    private static Session session;

    public static void setSession(final Session session) {
        Context.session = session;
    }

    public static Session getSession() {
        return session;
    }

    public static String tr(final String str) {
        return session.getLanguage().tr(str);
    }

}
