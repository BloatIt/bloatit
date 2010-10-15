package com.bloatit.model.util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.bloatit.model.data.Group;
import com.bloatit.model.data.Group.Right;

/**
 * some utils to manage hibernate
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();
	private static final Group everybodyGroup = buildEveryBodyGroup();

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

	private static Group buildEveryBodyGroup() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Group group = Group.createAndPersiste("everybody", null, Right.PUBLIC);
		session.getTransaction().commit();
	    return group;
    }

	/**
	 * singleton pattern implementation.
	 * @return the current session.
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Group getEverybodyGroup() {
	    return everybodyGroup;
    }
}
