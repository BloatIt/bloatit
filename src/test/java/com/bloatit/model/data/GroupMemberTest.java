package com.bloatit.model.data;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;

import com.bloatit.model.data.util.SessionManger;

/**
 * Unit test for simple App.
 */
public class GroupMemberTest extends TestCase {

    public GroupMemberTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
        SessionManger.reCreateSessionFactory();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManger.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManger.endWorkUnitAndFlush();
        }
        SessionManger.getSessionFactory().close();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GroupMemberTest.class);
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
            SessionManger.endWorkUnitAndFlush();
        }

    }

    public void testMemberDuplicateCreation() {
        try {
            SessionManger.beginWorkUnit();
            Member.createAndPersist("Yo", "plop", "yo@gmail.com");
            SessionManger.flush();
            Member.createAndPersist("Yo", "plip", "yoyo@gmail.com"); // duplicate login
            SessionManger.endWorkUnitAndFlush();
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
        SessionManger.endWorkUnitAndFlush();
    }

    public void testExistMemberByLogin() {
        testCreateMember();
        SessionManger.beginWorkUnit();
        assertTrue(Member.exist("Fred"));
        assertFalse(Member.exist("Inexistant"));
        assertFalse(Member.exist(null));
        SessionManger.endWorkUnitAndFlush();
    }

    public void testCreateGroup() {
        testCreateMember();
        SessionManger.beginWorkUnit();
        Group.createAndPersiste("Other", "plop@plop.com", Group.Right.PUBLIC);
        Group.createAndPersiste("myGroup", "plop@plop.com", Group.Right.PUBLIC);
        Group.createAndPersiste("b219", "plop@plop.com", Group.Right.PUBLIC);
        Group.createAndPersiste("b218", "plop@plop.com", Group.Right.PUBLIC);
        Group.createAndPersiste("b217", "plop@plop.com", Group.Right.PUBLIC);
        Group.createAndPersiste("b216", "plop@plop.com", Group.Right.PUBLIC);
        SessionManger.endWorkUnitAndFlush();

    }

    public void testDuplicatedGroup() {
        testCreateGroup();
        try {
            SessionManger.beginWorkUnit();
            Group.createAndPersiste("Other", "plop@plop.com", Group.Right.PUBLIC);
            assertTrue(true);
            SessionManger.endWorkUnitAndFlush();
            assertTrue(false);
        } catch (HibernateException e) {
            assertTrue(true);
        }
    }

    public void testGetGroupByName() {
        testCreateGroup();

        SessionManger.beginWorkUnit();
        // TODO correct me
        assertEquals("plop@plop.com", Group.getByName("b219").getEmail());
        assertNull(Group.getByName("Inexistant"));
        SessionManger.endWorkUnitAndFlush();
    }

    public void testAddUserToGroup() {
        testCreateGroup();

        SessionManger.beginWorkUnit();
        Member.getByLogin("Fred").addToGroup(Group.getByName("b219"), false);
        Member.getByLogin("Yo").addToGroup(Group.getByName("b219"), false);
        Member.getByLogin("Yo").addToGroup(Group.getByName("b217"), false);
        Member.getByLogin("Yo").addToGroup(Group.getByName("b218"), false);
        Member.getByLogin("Yo").addToGroup(Group.getByName("b216"), false);
        SessionManger.endWorkUnitAndFlush();
    }

    public void testGetAllUserInGroup() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        List<Member> members = Group.getByName("b219").getMembers();
        assertEquals(members.size(), 2);
        assertEquals(members.get(0).getFirstname(), "Frédéric");
        assertEquals(members.get(1).getFirstname(), "Yoann");
        SessionManger.endWorkUnitAndFlush();
    }

    public void testGetAllGroupForUser() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        QueryCollection<Group> groups = Member.getByLogin("Yo").getGroups();
        Iterator<Group> it = groups.iterator();
        assertEquals(it.next().getLogin(), "b216");
        assertEquals(it.next().getLogin(), "b217");
        assertEquals(it.next().getLogin(), "b218");
        assertEquals(it.next().getLogin(), "b219");
        assertFalse(it.hasNext());
        SessionManger.endWorkUnitAndFlush();
    }

    public void testRemoveGroup() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        Group b219 = Group.getByName("b219");
        Member yo = Member.getByLogin("Yo");

        yo.removeFromGroup(b219);

        Iterator<Group> it = yo.getGroups().iterator();
        assertEquals(it.next().getLogin(), "b216");
        assertEquals(it.next().getLogin(), "b217");
        assertEquals(it.next().getLogin(), "b218");
        assertFalse(it.hasNext());

        SessionManger.endWorkUnitAndFlush();
    }

    public void testRemoveMember() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        Group b218 = Group.getByName("b218");
        Member yo = Member.getByLogin("Yo");

        b218.removeMember(yo);

        Iterator<Group> it = yo.getGroups().iterator(); 
        assertEquals(it.next().getLogin(), "b216");
        assertEquals(it.next().getLogin(), "b217");
        assertEquals(it.next().getLogin(), "b219");
        assertFalse(it.hasNext());

        SessionManger.endWorkUnitAndFlush();
    }

}
