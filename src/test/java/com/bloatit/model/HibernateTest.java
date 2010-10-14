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

	/**
	 * Rigorous Test :-)
	 */
	public void testApp() {
		Member theMember = Member.createAndPersiste("Thomas", "password", "tom@gmail.com");
		theMember.setFirstname("Thomas");
		theMember.setLastname("Guyard");
		
		HibernateUtil.getSessionFactory().close();
	}
}
