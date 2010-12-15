package com.bloatit.model.data.util;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.bloatit.model.data.DaoDemand;

/**
 * These are some simple static utils to manage Hibernate sessions (and
 * hibernate Search)
 */
public class SessionManager {
    // SHOULD BE FINAL see reCreateSessionFactory
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            final SessionFactory buildSessionFactory = new AnnotationConfiguration().configure().setProperty("hibernate.hbm2ddl.auto", "update")
                    .buildSessionFactory();

            if (System.getProperty("lucene") == null || System.getProperty("lucene").equals("1")) {
                Search.getFullTextSession(buildSessionFactory.getCurrentSession()).createIndexer(DaoDemand.class).startAndWait();
            }

            return buildSessionFactory;
        } catch (final Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Query createQuery(final String str) {
        return getSessionFactory().getCurrentSession().createQuery(str);
    }

    /**
     * singleton pattern implementation.
     * 
     * @return the current session.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static FullTextSession getCurrentFullTextSession() {
        return Search.getFullTextSession(sessionFactory.getCurrentSession());
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
            sessionFactory = new AnnotationConfiguration().configure().setProperty("hibernate.hbm2ddl.auto", "create-drop").buildSessionFactory();
        } catch (final Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
