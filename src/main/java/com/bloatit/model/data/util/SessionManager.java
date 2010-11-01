package com.bloatit.model.data.util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class SessionManager {

    // SHOULD BE FINAL see reCreateSessionFactory
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (final Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * singleton pattern implementation.
     * 
     * @return the current session.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void beginWorkUnit() {
        sessionFactory.getCurrentSession().beginTransaction();
    }

    public static void endWorkUnitAndFlush() {
        try {
            sessionFactory.getCurrentSession().getTransaction().commit();
        } catch (final HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }

    public static void flush() {
        sessionFactory.getCurrentSession().flush();
    }

    public static void clear() {
        sessionFactory.getCurrentSession().clear();
    }

    public static void rollback() {
        sessionFactory.getCurrentSession().getTransaction().rollback();
    }

    /**
     * DO NOT USE ! THIS IS FOR TESTS ONLY !!
     */
    public static void reCreateSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            final AnnotationConfiguration configure = new AnnotationConfiguration().configure();
            configure.setProperty("hbm2ddl.auto", "create-drop");
            sessionFactory = configure.buildSessionFactory();
        } catch (final Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
