package com.bloatit.model.data;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

/**
 * Unit test for simple App.
 */
public class GroupMemberTest extends TestCase {

    public GroupMemberTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.reCreateSessionFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GroupMemberTest.class);
    }

    public void testCreateMember() {
        SessionManager.beginWorkUnit();
        {
            final DaoMember theMember = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com");
            theMember.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            final DaoMember theMember = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com");
            theMember.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            final DaoMember theMember = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
            theMember.setFullname("Yoann Plénet");
        }

        assertEquals(3, DBRequests.getAll(DaoMember.class).size());

        SessionManager.endWorkUnitAndFlush();

    }

    public void testMemberDuplicateCreation() {
        try {
            SessionManager.beginWorkUnit();
            DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
            SessionManager.flush();
            DaoMember.createAndPersist("Yo", "plip", "yoyo@gmail.com"); // duplicate
                                                                        // login
            SessionManager.endWorkUnitAndFlush();
            assertTrue(false);
        } catch (final HibernateException e) {
            assertTrue(true);
        }
    }

    public void testGetMemberByLogin() {
        testCreateMember();
        SessionManager.beginWorkUnit();
        assertNotNull(DaoMember.getByLogin("Fred"));
        assertNull(DaoMember.getByLogin("Inexistant"));
        SessionManager.endWorkUnitAndFlush();
    }

    public void testExistMemberByLogin() {
        testCreateMember();
        SessionManager.beginWorkUnit();
        assertTrue(DaoActor.exist("Fred"));
        assertFalse(DaoActor.exist("Inexistant"));
        assertFalse(DaoActor.exist(null));
        SessionManager.endWorkUnitAndFlush();
    }

    public void testCreateGroup() {
        testCreateMember();
        SessionManager.beginWorkUnit();
        DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("myGroup", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b218", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b217", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b216", "plop@plop.com", DaoGroup.Right.PUBLIC);
        SessionManager.endWorkUnitAndFlush();

    }

    public void testDuplicatedGroup() {
        testCreateGroup();
        try {
            SessionManager.beginWorkUnit();
            DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC);
            assertTrue(true);
            SessionManager.endWorkUnitAndFlush();
            assertTrue(false);
        } catch (final HibernateException e) {
            assertTrue(true);
        }
    }

    public void testGetGroupByName() {
        testCreateGroup();

        SessionManager.beginWorkUnit();
        // TODO correct me
        assertEquals("plop@plop.com", DaoGroup.getByName("b219").getEmail());
        assertNull(DaoGroup.getByName("Inexistant"));
        SessionManager.endWorkUnitAndFlush();
    }

    public void testAddUserToGroup() {
        testCreateGroup();

        SessionManager.beginWorkUnit();
        DaoMember.getByLogin("Fred").addToGroup(DaoGroup.getByName("b219"), false);
        DaoMember.getByLogin("Yo").addToGroup(DaoGroup.getByName("b219"), false);
        DaoMember.getByLogin("Yo").addToGroup(DaoGroup.getByName("b217"), false);
        DaoMember.getByLogin("Yo").addToGroup(DaoGroup.getByName("b218"), false);
        DaoMember.getByLogin("Yo").addToGroup(DaoGroup.getByName("b216"), false);
        SessionManager.endWorkUnitAndFlush();
    }

    public void testGetAllUserInGroup() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final PageIterable<DaoMember> members = DaoGroup.getByName("b219").getMembers();
        final Iterator<DaoMember> it = members.iterator();
        assertEquals(it.next().getFullname(), "Frédéric Bertolus");
        assertEquals(it.next().getFullname(), "Yoann Plénet");
        assertFalse(it.hasNext());

        assertEquals(2, members.size());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testGetAllGroupForUser() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final PageIterable<DaoGroup> groups = DaoMember.getByLogin("Yo").getGroups();
        final Iterator<DaoGroup> it = groups.iterator();
        assertEquals(it.next().getLogin(), "b216");
        assertEquals(it.next().getLogin(), "b217");
        assertEquals(it.next().getLogin(), "b218");
        assertEquals(it.next().getLogin(), "b219");
        assertFalse(it.hasNext());

        assertEquals(4, groups.size());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testRemoveGroup() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final DaoGroup b219 = DaoGroup.getByName("b219");
        final DaoMember yo = DaoMember.getByLogin("Yo");

        yo.removeFromGroup(b219);
        
        final Iterator<DaoGroup> it = yo.getGroups().iterator();
        assertEquals(it.next().getLogin(), "b216");
        assertEquals(it.next().getLogin(), "b217");
        assertEquals(it.next().getLogin(), "b218");
        assertFalse(it.hasNext());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testRemoveMember() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final DaoGroup b218 = DaoGroup.getByName("b218");
        final DaoMember yo = DaoMember.getByLogin("Yo");

        b218.removeMember(yo);

        final Iterator<DaoGroup> it = yo.getGroups().iterator();
        assertEquals(it.next().getLogin(), "b216");
        assertEquals(it.next().getLogin(), "b217");
        assertEquals(it.next().getLogin(), "b219");
        assertFalse(it.hasNext());

        SessionManager.endWorkUnitAndFlush();
    }

}
