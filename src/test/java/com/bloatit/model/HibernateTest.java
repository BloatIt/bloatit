package com.bloatit.model;

import org.hibernate.HibernateException;
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
		return new TestSuite(HibernateTest.class);
	}

	public void testCreateMember() {
		HibernateUtil.beginWorkUnit();
		{
			Member theMember = Member.createAndPersiste("Thomas", "password", "tom@gmail.com");
			theMember.setFirstname("Thomas");
			theMember.setLastname("Guyard");
			HibernateUtil.flush();
		}
		{
			Member theMember = Member.createAndPersiste("Fred", "other", "fred@gmail.com");
			theMember.setFirstname("Frédéric");
			theMember.setLastname("Bertolus");
			HibernateUtil.flush();
		}
		{
			Member theMember = Member.createAndPersiste("Yo", "plop", "yo@gmail.com");
			theMember.setFirstname("Yoann");
			theMember.setLastname("Plénet");
			HibernateUtil.EndWorkUnitAndFlush();
		}

		
	}
	
	public void testMemberDuplicateCreation(){
		try {
			HibernateUtil.beginWorkUnit();
			Member.createAndPersiste("Yo", "plop", "yo@gmail.com"); // duplicate login
			HibernateUtil.EndWorkUnitAndFlush();
	        assert(false);
        } catch (HibernateException e) {
        	assert(true);
        }
	}
	
	public void testGetMemberByLogin()
	{
		HibernateUtil.beginWorkUnit();
		assert (Member.getByLogin("Fred").getLastname() == "Bertolus");
		assert (Member.getByLogin("Inexistant") == null);
		HibernateUtil.EndWorkUnitAndFlush();
	}
	
	public void testExistMemberByLogin()
	{
		HibernateUtil.beginWorkUnit();
		assert (Member.exist("Fred") == true);
		assert (Member.exist("Inexistant") == false);
		assert (Member.exist(null) == false);
		HibernateUtil.EndWorkUnitAndFlush();
	}
	
	public void testCreateGroup() {
		HibernateUtil.beginWorkUnit();
		Member yo = Member.getByLogin("Yo");
		Group.createAndPersiste("Other", yo, Group.Right.PUBLIC);
		Group.createAndPersiste("myGroup", yo, Group.Right.PUBLIC);
		HibernateUtil.EndWorkUnitAndFlush();
		
		HibernateUtil.getSessionFactory().close();
	}
}
