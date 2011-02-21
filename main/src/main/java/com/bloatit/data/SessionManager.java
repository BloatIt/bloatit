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
package com.bloatit.data;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.bloatit.common.Log;

/**
 * These are some simple static utils to manage Hibernate sessions (and
 * hibernate Search)
 */
public final class SessionManager {

    /**
     * Desactivate the default constructor;
     */
    private SessionManager() { // Desactivate
    }

    // SHOULD BE FINAL see generateTestSessionFactory
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            final SessionFactory buildSessionFactory = new AnnotationConfiguration().configure()
                                                                                    .setProperty("hibernate.hbm2ddl.auto", "update")
                                                                                    .buildSessionFactory();

            if (System.getProperty("lucene") == null || System.getProperty("lucene").equals("1")) {
                Search.getFullTextSession(buildSessionFactory.getCurrentSession()).createIndexer(DaoDemand.class).startAndWait();
            }

            return buildSessionFactory;
        } catch (final Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            Log.data().fatal("Initial SessionFactory creation failed.", ex);
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
    public static void generateTestSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new AnnotationConfiguration().configure()
                                                          .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                                                          .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/bloatit_test")
                                                          .buildSessionFactory();
        } catch (final Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            Log.data().fatal("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
