package com.bloatit.data;

import org.hibernate.FlushMode;
import org.hibernate.classic.Session;

public final class DataManager {

    private DataManager() {
        // disactivate ctor;
    }

    public static void launch() {
        // For now do nothing.
    }

    public static void shutdown() {
        // For now do nothing.
    }

    public static void setReadOnly() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        session.setDefaultReadOnly(true);
        session.setFlushMode(FlushMode.MANUAL);
    }

    public static void open() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();

        session.beginTransaction();
        session.setDefaultReadOnly(false);
        session.setFlushMode(FlushMode.AUTO);
    }

    public static void close() {
        SessionManager.endWorkUnitAndFlush();
    }

}
