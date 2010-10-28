package com.bloatit.model.data;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;

import com.bloatit.common.PageIterable;
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
            DaoMember theMember = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com");
            theMember.setFirstname("Thomas");
            theMember.setLastname("Guyard");
            SessionManger.flush();
        }
        {
            DaoMember theMember = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com");
            theMember.setFirstname("Frédéric");
            theMember.setLastname("Bertolus");
            SessionManger.flush();
        }
        {
            DaoMember theMember = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
            theMember.setFirstname("Yoann");
            theMember.setLastname("Plénet");
            SessionManger.endWorkUnitAndFlush();
        }

    }

    public void testMemberDuplicateCreation() {
        try {
            SessionManger.beginWorkUnit();
            DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
            SessionManger.flush();
            DaoMember.createAndPersist("Yo", "plip", "yoyo@gmail.com"); // duplicate login
            SessionManger.endWorkUnitAndFlush();
            assertTrue(false);
        } catch (HibernateException e) {
            assertTrue(true);
        }
    }

    public void testGetMemberByLogin() {
        testCreateMember();
        SessionManger.beginWorkUnit();
        assertEquals("Bertolus", DaoMember.getByLogin("Fred").getLastname());
        assertNull(DaoMember.getByLogin("Inexistant"));
        SessionManger.endWorkUnitAndFlush();
    }

    public void testExistMemberByLogin() {
        testCreateMember();
        SessionManger.beginWorkUnit();
        assertTrue(DaoMember.exist("Fred"));
        assertFalse(DaoMember.exist("Inexistant"));
        assertFalse(DaoMember.exist(null));
        SessionManger.endWorkUnitAndFlush();
    }

    public void testCreateGroup() {
        testCreateMember();
        SessionManger.beginWorkUnit();
        DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("myGroup", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b218", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b217", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b216", "plop@plop.com", DaoGroup.Right.PUBLIC);
        SessionManger.endWorkUnitAndFlush();

    }

    public void testDuplicatedGroup() {
        testCreateGroup();
        try {
            SessionManger.beginWorkUnit();
            DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC);
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
        assertEquals("plop@plop.com", DaoGroup.getByName("b219").getEmail());
        assertNull(DaoGroup.getByName("Inexistant"));
        SessionManger.endWorkUnitAndFlush();
    }

    public void testAddUserToGroup() {
        testCreateGroup();

        SessionManger.beginWorkUnit();
        DaoMember.getByLogin("Fred").addToGroup(DaoGroup.getByName("b219"), false);
        DaoMember.getByLogin("Yo").addToGroup(DaoGroup.getByName("b219"), false);
        DaoMember.getByLogin("Yo").addToGroup(DaoGroup.getByName("b217"), false);
        DaoMember.getByLogin("Yo").addToGroup(DaoGroup.getByName("b218"), false);
        DaoMember.getByLogin("Yo").addToGroup(DaoGroup.getByName("b216"), false);
        SessionManger.endWorkUnitAndFlush();
    }

    public void testGetAllUserInGroup() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        PageIterable<DaoMember> Members = DaoGroup.getByName("b219").getMembers();
        Iterator<DaoMember> it = Members.iterator();
        assertEquals(it.next().getFirstname(), "Frédéric");
        assertEquals(it.next().getFirstname(), "Yoann");
        assertFalse(it.hasNext());
        SessionManger.endWorkUnitAndFlush();
    }

    public void testGetAllGroupForUser() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        PageIterable<DaoGroup> Groups = DaoMember.getByLogin("Yo").getGroups();
        Iterator<DaoGroup> it = Groups.iterator();
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
        DaoGroup b219 = DaoGroup.getByName("b219");
        DaoMember yo = DaoMember.getByLogin("Yo");

        yo.removeFromGroup(b219);

        Iterator<DaoGroup> it = yo.getGroups().iterator();
        assertEquals(it.next().getLogin(), "b216");
        assertEquals(it.next().getLogin(), "b217");
        assertEquals(it.next().getLogin(), "b218");
        assertFalse(it.hasNext());

        SessionManger.endWorkUnitAndFlush();
    }

    public void testRemoveMember() {
        testAddUserToGroup();

        SessionManger.beginWorkUnit();
        DaoGroup b218 = DaoGroup.getByName("b218");
        DaoMember yo = DaoMember.getByLogin("Yo");

        b218.removeMember(yo);

        Iterator<DaoGroup> it = yo.getGroups().iterator(); 
        assertEquals(it.next().getLogin(), "b216");
        assertEquals(it.next().getLogin(), "b217");
        assertEquals(it.next().getLogin(), "b219");
        assertFalse(it.hasNext());

        SessionManger.endWorkUnitAndFlush();
    }

}
