package com.bloatit.data;

import org.hibernate.FlushMode;
import org.hibernate.classic.Session;

public final class Data {

    private Data() {
        // disactivate ctor;
    }

    public static void launch() {
        // For now do nothing.
    }

    public static void shutdown() {
        // For now do nothing.
    }

    public static void openReadOnly() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();

        session.setDefaultReadOnly(true);
        session.setFlushMode(FlushMode.MANUAL);
        session.beginTransaction();
    }

    public static void open() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();

        session.setDefaultReadOnly(false);
        session.setFlushMode(FlushMode.AUTO);
        session.beginTransaction();
    }

    public static void close() {
        SessionManager.endWorkUnitAndFlush();
    }

}
