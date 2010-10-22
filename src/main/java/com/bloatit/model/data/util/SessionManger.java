package com.bloatit.model.data.util;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * some utils to manage hibernate
 */
public class SessionManger {

	private static final SessionFactory sessionFactory = buildSessionFactory();

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
	 * @return the current session.
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void beginWorkUnit() {
		sessionFactory.getCurrentSession().beginTransaction();
	}
	
	public static void EndWorkUnitAndFlush(){
		try {
			sessionFactory.getCurrentSession().getTransaction().commit();
        } catch (HibernateException e) {
        	sessionFactory.getCurrentSession().getTransaction().rollback();
        	throw e;
        }
	}
	
	public static void flush(){
		sessionFactory.getCurrentSession().flush();
	}
	
	public static void rollback(){
		sessionFactory.getCurrentSession().getTransaction().rollback();
	}
}
