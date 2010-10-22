package com.bloatit.model;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;

import com.bloatit.model.data.Group;
import com.bloatit.model.data.Member;
import com.bloatit.model.data.util.SessionManger;

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
		SessionManger.beginWorkUnit();
		{
			Member theMember = Member.createAndPersist("Thomas", "password", "tom@gmail.com");
			theMember.setFirstname("Thomas");
			theMember.setLastname("Guyard");
			SessionManger.flush();
		}
		{
			Member theMember = Member.createAndPersist("Fred", "other", "fred@gmail.com");
			theMember.setFirstname("Frédéric");
			theMember.setLastname("Bertolus");
			SessionManger.flush();
		}
		{
			Member theMember = Member.createAndPersist("Yo", "plop", "yo@gmail.com");
			theMember.setFirstname("Yoann");
			theMember.setLastname("Plénet");
			SessionManger.EndWorkUnitAndFlush();
		}

	}

	public void testMemberDuplicateCreation() {
		try {
			SessionManger.beginWorkUnit();
			Member.createAndPersist("Yo", "plop", "yo@gmail.com"); // duplicate
			                                                        // login
			SessionManger.EndWorkUnitAndFlush();
			assert (false);
		} catch (HibernateException e) {
			assert (true);
		}
	}

	public void testGetMemberByLogin() {
		SessionManger.beginWorkUnit();
		assert (Member.getByLogin("Fred").getLastname().equals("Bertolus"));
		assert (Member.getByLogin("Inexistant") == null);
		SessionManger.EndWorkUnitAndFlush();
	}

	public void testExistMemberByLogin() {
		SessionManger.beginWorkUnit();
		assert (Member.exist("Fred") == true);
		assert (Member.exist("Inexistant") == false);
		assert (Member.exist(null) == false);
		SessionManger.EndWorkUnitAndFlush();
	}

	public void testCreateGroup() {
		SessionManger.beginWorkUnit();
		Member yo = Member.getByLogin("Yo");
		Member fred = Member.getByLogin("Fred");
		Group.createAndPersiste("Other", yo, Group.Right.PUBLIC);
		Group.createAndPersiste("myGroup", yo, Group.Right.PUBLIC);
		Group.createAndPersiste("b219", yo, Group.Right.PUBLIC);
		Group.createAndPersiste("b218", fred, Group.Right.PUBLIC);
		Group.createAndPersiste("b217", fred, Group.Right.PUBLIC);
		Group.createAndPersiste("b216", Member.getByLogin("Thomas"), Group.Right.PUBLIC);
		SessionManger.EndWorkUnitAndFlush();

	}

	public void testDuplicatedGroup() {
		try {
			SessionManger.beginWorkUnit();
			Member fred = Member.getByLogin("Fred");
			Group.createAndPersiste("Other", fred, Group.Right.PUBLIC);
			assert (true);
			SessionManger.EndWorkUnitAndFlush();
			assert (false);
		} catch (HibernateException e) {
			assert (true);
		}
	}

	public void testGetGroupByName() {
		SessionManger.beginWorkUnit();
		assert (Group.getByName("b219").getAuthor().getLastname().equals("Plénet"));
		assert (Group.getByName("Inexistant") == null);
		SessionManger.EndWorkUnitAndFlush();
	}

	public void testAddUserToGroup() {
		SessionManger.beginWorkUnit();
		Member.getByLogin("Fred").addToGroup(Group.getByName("b219"), false);
		Member.getByLogin("Yo").addToGroup(Group.getByName("b219"), false);
		Member.getByLogin("Yo").addToGroup(Group.getByName("b217"), false);
		Member.getByLogin("Yo").addToGroup(Group.getByName("b218"), false);
		Member.getByLogin("Yo").addToGroup(Group.getByName("b216"), false);
		SessionManger.EndWorkUnitAndFlush();
	}

	public void testGetAllUserInGroup() {
		SessionManger.beginWorkUnit();
		List<Member> members = Group.getByName("b219").getMembers();
		assertEquals(members.size(), 2);
		assertEquals(members.get(0).getFirstname(), "Frédéric");
		assertEquals(members.get(1).getFirstname(), "Yoann");
		SessionManger.EndWorkUnitAndFlush();
	}
	
	public void testGetAllGroupForUser(){
		SessionManger.beginWorkUnit();
		List<Group> groups = Member.getByLogin("Yo").getGroups();
		assertEquals(groups.size(), 4);
		assertEquals(groups.get(0).getName(), "b219");
		assertEquals(groups.get(1).getName(), "b218");
		assertEquals(groups.get(2).getName(), "b217");
		assertEquals(groups.get(3).getName(), "b216");
		SessionManger.EndWorkUnitAndFlush();
	}
	
	public void testRemoveGroup(){
		SessionManger.beginWorkUnit();
		Group b219 = Group.getByName("b219");
		Member yo = Member.getByLogin("Yo");
		
		yo.removeFromGroup(b219);
		
		List<Group> groups = Member.getByLogin("Yo").getGroups();
		assertEquals(groups.size(), 3);
		assertEquals(groups.get(0).getName(), "b218");
		assertEquals(groups.get(1).getName(), "b217");
		assertEquals(groups.get(2).getName(), "b216");
		
		SessionManger.EndWorkUnitAndFlush();
	}

	public void end() {
		SessionManger.getSessionFactory().close();
	}

}
