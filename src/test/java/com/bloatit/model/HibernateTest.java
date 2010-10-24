package com.bloatit.model;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;

import com.bloatit.model.data.Group;
import com.bloatit.model.data.Member;
import com.bloatit.model.data.QueryCollection;
import com.bloatit.model.data.util.SessionManger;

/**
 * Unit test for simple App.
 */
public class HibernateTest extends TestCase {

    public HibernateTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
        SessionManger.reCreateSessionFactory();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManger.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManger.EndWorkUnitAndFlush();
        }
        SessionManger.getSessionFactory().close();
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
            Member.createAndPersist("Yo", "plop", "yo@gmail.com");
            SessionManger.flush();
            Member.createAndPersist("Yo", "plip", "yoyo@gmail.com"); // duplicate login
            SessionManger.EndWorkUnitAndFlush();
            assertTrue(false);
        } catch (HibernateException e) {
            assertTrue(true);
        }
    }

    public void testGetMemberByLogin() {
        testCreateMember();
        SessionManger.beginWorkUnit();
        assertEquals("Bertolus", Member.getByLogin("Fred").getLastname());
        assertNull(Member.getByLogin("Inexistant"));
        SessionManger.EndWorkUnitAndFlush();
    }

    public void testExistMemberByLogin() {
        testCreateMember();
        SessionManger.beginWorkUnit();
        assertTrue(Member.exist("Fred"));
        assertFalse(Member.exist("Inexistant"));
        assertFalse(Member.exist(null));
        SessionManger.EndWorkUnitAndFlush();
    }

    public void testCreateGroup() {
        testCreateMember();
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
        testCreateGroup();
        try {
            SessionManger.beginWorkUnit();
            Member fred = Member.getByLogin("Fred");
            Group.createAndPersiste("Other", fred, Group.Right.PUBLIC);
            assertTrue(true);
            SessionManger.EndWorkUnitAndFlush();
            assertTrue(false);
        } catch (HibernateException e) {
            assertTrue(true);
        }
    }

    public void testGetGroupByName() {
        testCreateGroup();

        SessionManger.beginWorkUnit();
        assertEquals("Plénet", Group.getByName("b219").getAuthor().getLastname());
        assertNull(Group.getByName("Inexistant"));
        SessionManger.EndWorkUnitAndFlush();
    }

    public void testAddUserToGroup() {
        testCreateGroup();

        SessionManger.beginWorkUnit();
        Member.getByLogin("Fred").addToGroup(Group.getByName("b219"), false);
        Member.getByLogin("Yo").addToGroup(Group.getByName("b219"), false);
        Member.getByLogin("Yo").addToGroup(Group.getByName("b217"), false);
        Member.getByLogin("Yo").addToGroup(Group.getByName("b218"), false);
        Member.getByLogin("Yo").addToGroup(Group.getByName("b216"), false);
        SessionManger.EndWorkUnitAndFlush();
    }

    public void testGetAllUserInGroup() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        List<Member> members = Group.getByName("b219").getMembers();
        assertEquals(members.size(), 2);
        assertEquals(members.get(0).getFirstname(), "Frédéric");
        assertEquals(members.get(1).getFirstname(), "Yoann");
        SessionManger.EndWorkUnitAndFlush();
    }

    public void testGetAllGroupForUser() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        QueryCollection<Group> groups = Member.getByLogin("Yo").getGroups();
        Iterator<Group> it = groups.iterator();
        assertEquals(it.next().getName(), "b219");
        assertEquals(it.next().getName(), "b218");
        assertEquals(it.next().getName(), "b217");
        assertEquals(it.next().getName(), "b216");
        assertFalse(it.hasNext());
        SessionManger.EndWorkUnitAndFlush();
    }

    public void testRemoveGroup() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        Group b219 = Group.getByName("b219");
        Member yo = Member.getByLogin("Yo");

        yo.removeFromGroup(b219);

        Iterator<Group> it = yo.getGroups().iterator();
        assertEquals(it.next().getName(), "b218");
        assertEquals(it.next().getName(), "b217");
        assertEquals(it.next().getName(), "b216");
        assertFalse(it.hasNext());

        SessionManger.EndWorkUnitAndFlush();
    }

    public void testRemoveMember() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        Group b218 = Group.getByName("b218");
        Member yo = Member.getByLogin("Yo");

        b218.removeMember(yo);

        Iterator<Group> it = yo.getGroups().iterator(); 
        assertEquals(it.next().getName(), "b219");
        assertEquals(it.next().getName(), "b217");
        assertEquals(it.next().getName(), "b216");
        assertFalse(it.hasNext());

        SessionManger.EndWorkUnitAndFlush();
    }

}
