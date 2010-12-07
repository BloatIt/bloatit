package test;

import com.bloatit.web.server.Session;

public class Context {

    private static Session session;

    public static void setSession(Session session) {
        Context.session = session;
    }

    public static Session getSession() {
        return session;
    }

    public static String tr(String str) {
        return session.getLanguage().tr(str);
    }

}
