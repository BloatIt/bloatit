package com.bloatit.model;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;

import com.bloatit.model.data.Group;
import com.bloatit.model.data.Member;
import com.bloatit.model.util.HibernateUtil;

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

	public void testMemberDuplicateCreation() {
		try {
			HibernateUtil.beginWorkUnit();
			Member.createAndPersiste("Yo", "plop", "yo@gmail.com"); // duplicate
			                                                        // login
			HibernateUtil.EndWorkUnitAndFlush();
			assert (false);
		} catch (HibernateException e) {
			assert (true);
		}
	}

	public void testGetMemberByLogin() {
		HibernateUtil.beginWorkUnit();
		assert (Member.getByLogin("Fred").getLastname().equals("Bertolus"));
		assert (Member.getByLogin("Inexistant") == null);
		HibernateUtil.EndWorkUnitAndFlush();
	}

	public void testExistMemberByLogin() {
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
		Group.createAndPersiste("b219", yo, Group.Right.PUBLIC);
		HibernateUtil.EndWorkUnitAndFlush();

	}

	public void testDuplicatedGroup() {
		try {
			HibernateUtil.beginWorkUnit();
			Member fred = Member.getByLogin("Fred");
			Group.createAndPersiste("Other", fred, Group.Right.PUBLIC);
			assert (true);
			HibernateUtil.EndWorkUnitAndFlush();
			assert (false);
		} catch (HibernateException e) {
			assert (true);
		}
	}

	public void testGetGroupByName() {
		HibernateUtil.beginWorkUnit();
		assert (Group.getByName("b219").getAuthor().getLastname().equals("Plénet"));
		assert (Group.getByName("Inexistant") == null);
		HibernateUtil.EndWorkUnitAndFlush();
	}

	public void testAddUserToGroup() {
		HibernateUtil.beginWorkUnit();
		Member.getByLogin("Fred").addGroup(Group.getByName("b219"), false);
		Member.getByLogin("Yo").addGroup(Group.getByName("b219"), false);
		HibernateUtil.EndWorkUnitAndFlush();
	}

	public void testGetAllUserInGroup() {
		HibernateUtil.beginWorkUnit();
		List<Member> members = Group.getByName("b219").getMembers();
		assertEquals(members.size(), 2);
		assertEquals(members.get(0).getFirstname(), "Frédéric");
		assertEquals(members.get(1).getFirstname(), "Yoann");
		HibernateUtil.EndWorkUnitAndFlush();
	}
	
	public void testGetAllGroupForUser(){
		HibernateUtil.beginWorkUnit();
		List<Group> groups = Member.getByLogin("Yo").getGroups();
		assertEquals(groups.size(), 2);
		assertEquals(groups.get(0).getName(), "everybody");
		assertEquals(groups.get(1).getName(), "b219");
		HibernateUtil.EndWorkUnitAndFlush();
	}

	public void end() {
		HibernateUtil.getSessionFactory().close();
	}

}
