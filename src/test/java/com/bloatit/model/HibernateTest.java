package com.bloatit.model;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.bloatit.model.data.Group;
import com.bloatit.model.data.Member;
import com.bloatit.model.util.HibernateUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class HibernateTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public HibernateTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		
//		SQLQuery insertNobodyMember = session.createSQLQuery("insert INTO " +
//							   "bloatit_member (id, datejoin, email , firstname, lastname, login, password)" +
//				               "values         (0, now(), 'nobody@nowhere.com','John', 'Doe', 'nobody', 'pass')");
//		SQLQuery insertEverybodyGroup= session.createSQLQuery("insert INTO " +
//				"bloatit_group (id, creationdate, author_id, logo, name, group_right)" +
//				"values        (0, now(), 0,NULL , 'everybody', 0)");
//		
//		insertNobodyMember.executeUpdate();
//		insertEverybodyGroup.executeUpdate();
//		
//		session.getTransaction().commit();
		
		return new TestSuite(HibernateTest.class);
	}

	public void testCreateMember() {
		{
			HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			Member theMember = Member.createAndPersiste("Thomas", "password", "tom@gmail.com");
			theMember.setFirstname("Thomas");
			theMember.setLastname("Guyard");
			HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		}
		{
			HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			Member theMember = Member.createAndPersiste("Fred", "other", "fred@gmail.com");
			theMember.setFirstname("Frédéric");
			theMember.setLastname("Bertolus");
			HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		}
		{
			HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			Member theMember = Member.createAndPersiste("Yo", "plop", "yo@gmail.com");
			theMember.setFirstname("Yoann");
			theMember.setLastname("Plénet");
			Group.createAndPersiste("Other", theMember, Group.Right.PUBLIC);
			HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
			
			HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			Group.createAndPersiste("myGroup", theMember, Group.Right.PUBLIC);
			HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		}

		assert (Member.getByLogin("Fred").getLastname() == "Bertolus");
		assert (Member.getByLogin("Inexistant") == null);

		HibernateUtil.getSessionFactory().close();
	}
}
