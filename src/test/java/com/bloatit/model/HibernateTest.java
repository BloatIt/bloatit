package com.bloatit.model;

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
		{
			Member theMember = Member.createAndPersiste("Thomas", "password", "tom@gmail.com");
			theMember.setFirstname("Thomas");
			theMember.setLastname("Guyard");
		}
		{
			Member theMember = Member.createAndPersiste("Fred", "other", "fred@gmail.com");
			theMember.setFirstname("Frédéric");
			theMember.setLastname("Bertolus");
		}
		{
			Member theMember = Member.createAndPersiste("Yo", "plop", "yo@gmail.com");
			theMember.setFirstname("Yoann");
			theMember.setLastname("Plénet");
		}
		
		assert(Member.getMemberByLogin("Fred").getLastname() == "Bertolus");
		assert(Member.getMemberByLogin("Inexistant") == null);

		HibernateUtil.getSessionFactory().close();
	}
}
