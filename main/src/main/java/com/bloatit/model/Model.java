package com.bloatit.model;

import org.hibernate.FlushMode;
import org.hibernate.classic.Session;

import com.bloatit.model.data.util.SessionManager;

public final class Model {

    private Model() {
        // disactivate ctor;
    }

    public static void launch() {
        // For now do nothing.
    }

    public static void shutdown() {
        // For now do nothing.
    }

    public static void openReadOnly() {
        Session session = SessionManager.getSessionFactory().getCurrentSession();

        session.setDefaultReadOnly(true);
        session.setFlushMode(FlushMode.MANUAL);
        session.beginTransaction();
    }

    public static void open() {
        Session session = SessionManager.getSessionFactory().getCurrentSession();

        session.setDefaultReadOnly(false);
        session.setFlushMode(FlushMode.AUTO);
        session.beginTransaction();
    }

    public static void close() {
        SessionManager.endWorkUnitAndFlush();
    }

}
