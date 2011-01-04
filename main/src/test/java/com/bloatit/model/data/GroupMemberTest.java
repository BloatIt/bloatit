package com.bloatit.model.data;

import java.util.Iterator;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.DaoMember.Role;
import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;

/**
 * Unit test for Member and groups
 */
public class GroupMemberTest extends TestCase {

    public GroupMemberTest(final String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();
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
        final DaoMember theMember = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com", Locale.FRANCE);

        assertEquals(theMember.getEmail(), "tom@gmail.com");
        assertEquals(theMember.getFullname(), "");
        assertEquals(theMember.getLogin(), "Thomas");
        assertEquals(theMember.getPassword(), "password");
        assertEquals(theMember.getKarma().intValue(), 0);
        assertEquals(theMember.getRole(), Role.NORMAL);

        theMember.setFullname("Thomas Guyard");
        assertEquals(theMember.getFullname(), "Thomas Guyard");
        theMember.setEmail("Test@nowhere.com");
        assertEquals(theMember.getEmail(), "Test@nowhere.com");
        theMember.setPassword("Hello");
        assertEquals(theMember.getPassword(), "Hello");
        theMember.setRole(Role.ADMIN);
        assertEquals(theMember.getRole(), Role.ADMIN);

        SessionManager.endWorkUnitAndFlush();
    }

    public void testCreateTreeMembers() {
        SessionManager.beginWorkUnit();
        {
            final DaoMember theMember = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com", Locale.FRANCE);
            theMember.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            final DaoMember theMember = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com", Locale.FRANCE);
            theMember.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            final DaoMember theMember = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com", Locale.FRANCE);
            theMember.setFullname("Yoann Plénet");
        }

        assertEquals(3, DBRequests.getAll(DaoMember.class).size());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testCreateMemberLimit() {
        SessionManager.beginWorkUnit();
        try {
            DaoMember.createAndPersist(null, "pass", "mail@nowhere.com", Locale.FRANCE);
            assertTrue(false);
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", null, "mail@nowhere.com", Locale.FRANCE);
            assertTrue(false);
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", "pass", null, Locale.FRANCE);
            assertTrue(false);
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", "pass", "ZDQSDV", null);
            assertTrue(false);
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("", "pass", "mail@nowhere.com", Locale.FRANCE);
            assertTrue(false);
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", "", "mail@nowhere.com", Locale.FRANCE);
            assertTrue(false);
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", "pass", "", Locale.FRANCE);
            assertTrue(false);
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    public void testMemberDuplicateCreation() {
        try {
            SessionManager.beginWorkUnit();
            DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com", Locale.FRANCE);
            SessionManager.flush();
            // duplicate login
            DaoMember.createAndPersist("Yo", "plip", "yoyo@gmail.com", Locale.FRANCE);
            SessionManager.endWorkUnitAndFlush();
            assertTrue(false);
        } catch (final HibernateException e) {
            assertTrue(true);
        }
    }

    public void testGetMemberByLogin() {
        testCreateTreeMembers();
        SessionManager.beginWorkUnit();
        assertNotNull(DaoMember.getByLogin("Fred"));
        assertNull(DaoMember.getByLogin("Inexistant"));
        assertNull(DaoMember.getByLogin(null));
        SessionManager.endWorkUnitAndFlush();
    }

    public void testGetMemberByLoginAndPassword() {
        testCreateTreeMembers();
        SessionManager.beginWorkUnit();
        assertNotNull(DaoMember.getByLoginAndPassword("Fred", "other"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "notTheGoodPassword"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "other "));
        assertNull(DaoMember.getByLoginAndPassword("Fred", " other"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "other\n"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "othe"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "o"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", ""));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "\\*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "\\\\*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "\\\\\\*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "\\\\\\\\*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", ".*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "\\.*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "\\.\\*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "' OR 1=1; --"));
        assertNull(DaoMember.getByLoginAndPassword(null, "' OR 1=1; --"));
        assertNull(DaoMember.getByLoginAndPassword("Inexistant", "' OR 1=1; --"));
        SessionManager.endWorkUnitAndFlush();
    }

    public void testExistMemberByLogin() {
        testCreateTreeMembers();
        SessionManager.beginWorkUnit();
        assertTrue(DaoActor.exist("Fred"));
        assertFalse(DaoActor.exist("Inexistant"));
        assertFalse(DaoActor.exist(null));
        SessionManager.endWorkUnitAndFlush();
    }

    public void testCreateGroup() {
        testCreateTreeMembers();
        SessionManager.beginWorkUnit();
        DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("myGroup", "plop1@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b219", "plop2@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b218", "plop3@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b217", "plop4@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b216", "plop5@plop.com", DaoGroup.Right.PUBLIC);
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
        assertEquals("plop2@plop.com", DaoGroup.getByName("b219").getEmail());
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
