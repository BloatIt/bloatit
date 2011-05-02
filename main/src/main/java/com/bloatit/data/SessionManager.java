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
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jasypt.hibernate.encryptor.HibernatePBEEncryptorRegistry;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.Log;

/**
 * These are some simple static utils to manage Hibernate sessions (and
 * hibernate Search)
 */
public class SessionManager {

    /**
     * Desactivate the default constructor;
     */
    private SessionManager() { // Desactivate
    }

    // SHOULD BE FINAL see generateTestSessionFactory
    /** The session factory. */
    private static SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Builds the session factory; Update the lucene index.
     * 
     * @return the session factory
     */
    private static SessionFactory buildSessionFactory() {
        try {
            final Configuration configuration = createConfiguration().setProperty("hibernate.hbm2ddl.auto", "validate");
            final SessionFactory buildSessionFactory = configuration.buildSessionFactory();

            if (System.getProperty("lucene") == null || System.getProperty("lucene").equals("1")) {
                Search.getFullTextSession(buildSessionFactory.getCurrentSession()).createIndexer(DaoFeature.class).startAndWait();
            }

            return buildSessionFactory;
        } catch (final Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            Log.data().fatal("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Create the configuration for hibernate sessionFactory. Handle the
     * encryptor to encrypt the db password in the hibernate.cfg.xml conf file.
     * 
     * @return
     */
    private static Configuration createConfiguration() {
        final HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
        registry.registerPBEStringEncryptor("strongHibernateStringEncryptor", ConfigurationManager.getEncryptor());
        final Configuration configuration = new Configuration().configure();
        return configuration;
    }

    /**
     * Shortener to create a HQL query.
     * 
     * @param str the Hql query string.
     * @return the query
     * @see org.hibernate.Session#createQuery(String)
     */
    public static Query createQuery(final String str) {
        return getSessionFactory().getCurrentSession().createQuery(str);
    }

    /**
     * Shortener to create a Hql filter
     * 
     * @param collection a mapped collection
     * @param str the filter
     * @return the query corresponding to that filter
     * @see org.hibernate.Session#createFilter(Object, String)
     */
    protected static Query createFilter(final Object collection, final String str) {
        return getSessionFactory().getCurrentSession().createFilter(collection, str);
    }

    /**
     * Shortener to get a HQL query by name.
     * 
     * @param name the query name
     * @return the HQL query having this name.
     * @see org.hibernate.Session#getNamedQuery(String)
     */
    public static Query getNamedQuery(final String name) {
        return getSessionFactory().getCurrentSession().getNamedQuery(name);
    }

    /**
     * singleton pattern implementation.
     * 
     * @return the current hibernate session.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Singleton pattern implementation.
     * 
     * @return the current hibernate search session.
     */
    public static FullTextSession getCurrentFullTextSession() {
        return Search.getFullTextSession(sessionFactory.getCurrentSession());
    }

    /**
     * Begin a work unit. You have to make sure this method is called before
     * using any hibernate related features. It begins a new transaction.
     * 
     * @see org.hibernate.Session#beginTransaction()
     */
    public static void beginWorkUnit() {
        sessionFactory.getCurrentSession().beginTransaction();
    }

    /**
     * Close the current workunit. Try to commit the data to the db. If there is
     * an error everything is rollback.
     * 
     * @see org.hibernate.Transaction#commit()
     * @see org.hibernate.Transaction#rollback()
     */
    public static void endWorkUnitAndFlush() {
        try {
            sessionFactory.getCurrentSession().getTransaction().commit();
        } catch (final HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Shortener for flushing the hibernate session.
     */
    public static void flush() {
        sessionFactory.getCurrentSession().flush();
    }

    /**
     * Shortener for clearing the hiberate session.
     */
    public static void clear() {
        sessionFactory.getCurrentSession().clear();
    }

    /**
     * Shortener to rollback the current hibernate session.
     */
    public static void rollback() {
        sessionFactory.getCurrentSession().getTransaction().rollback();
    }

    /**
     * DO NOT USE ! THIS IS FOR TESTS ONLY !!
     */
    public static void generateTestSessionFactory() {
        try {

            final Configuration configuration = createConfiguration().setProperty("hibernate.hbm2ddl.auto", "create-drop")
                                                                     .setProperty("hibernate.cache.use_second_level_cache", "false")
                                                                     .setProperty("hibernate.cache.use_query_cache", "false")
                                                                     .setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider")
                                                                     .setProperty(Environment.SHOW_SQL, "false")
                                                                     // .setProperty(Environment.DRIVER,
                                                                     // "org.hsqldb.jdbcDriver")
                                                                     // .setProperty(Environment.DIALECT,
                                                                     // HSQLDialect.class.getName())
                                                                     // .setProperty(Environment.USER,
                                                                     // "sa")
                                                                     // .setProperty(Environment.PASS,
                                                                     // "")
                                                                     // .setProperty(Environment.URL,
                                                                     // "jdbc:hsqldb:mem:testdb")
                                                                     .setProperty(Environment.URL, "jdbc:postgresql://localhost/bloatit_test");
            sessionFactory = configuration.buildSessionFactory();

        } catch (final Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            Log.data().fatal("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
