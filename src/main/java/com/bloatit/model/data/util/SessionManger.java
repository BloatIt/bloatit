package com.bloatit.model.data.util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class SessionManger {

	// SHOULD BE FINAL see reCreateSessionFactory
	private static SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			return new AnnotationConfiguration().configure().buildSessionFactory();
		} catch (Throwable ex) {
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
		// Set a 3 second timeout.
		sessionFactory.getCurrentSession().getTransaction().setTimeout(3);

	}

	public static void endWorkUnitAndFlush() {
		try {
			sessionFactory.getCurrentSession().getTransaction().commit();
		} catch (HibernateException e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw e;
		} finally {
			sessionFactory.getCurrentSession().close();
		}
	}

	public static void flush() {
		sessionFactory.getCurrentSession().flush();
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
			sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
