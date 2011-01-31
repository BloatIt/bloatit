package com.bloatit.data;

import java.util.Iterator;
import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.framework.utils.PageIterable;

/**
 * Unit test for Member and groups
 */
public class DaoGroupMemberTest extends TestCase {

    private DaoMember tom;
    private DaoMember fred;
    private DaoMember yo;
    private DaoGroup b216;
    private DaoGroup b217;
    private DaoGroup b219;

    public DaoGroupMemberTest(final String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();

        SessionManager.beginWorkUnit();
        tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com", Locale.FRANCE);
        tom.setFullname("Thomas Guyard");
        fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com", Locale.FRANCE);
        fred.setFullname("Frédéric Bertolus");
        yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com", Locale.FRANCE);
        yo.setFullname("Yoann Plénet");

        b219 = DaoGroup.createAndPersiste("b219", "plop2@plop.com", DaoGroup.Right.PUBLIC);
        b216 = DaoGroup.createAndPersiste("b216", "plop3@plop.com", DaoGroup.Right.PUBLIC);
        b217 = DaoGroup.createAndPersiste("b217", "plop4@plop.com", DaoGroup.Right.PUBLIC);

        SessionManager.endWorkUnitAndFlush();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }

    public void testAddUserToGroup() {
        SessionManager.beginWorkUnit();
        DaoMember.getByLogin(fred.getLogin()).addToGroup(DaoGroup.getByName(b219.getLogin()), false);
        DaoMember.getByLogin(yo.getLogin()).addToGroup(DaoGroup.getByName(b219.getLogin()), false);
        DaoMember.getByLogin(yo.getLogin()).addToGroup(DaoGroup.getByName(b217.getLogin()), false);
        DaoMember.getByLogin(yo.getLogin()).addToGroup(DaoGroup.getByName(b216.getLogin()), false);
        SessionManager.endWorkUnitAndFlush();
    }

    public void testGetAllUserInGroup() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final PageIterable<DaoMember> members = DaoGroup.getByName(b219.getLogin()).getMembers();
        final Iterator<DaoMember> it = members.iterator();
        assertEquals(it.next().getId(), fred.getId());
        assertEquals(it.next().getId(), yo.getId());
        assertFalse(it.hasNext());

        assertEquals(2, members.size());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testGetAllGroupForUser() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final PageIterable<DaoGroup> groups = DaoMember.getByLogin("Yo").getGroups();
        final Iterator<DaoGroup> it = groups.iterator();
        assertEquals(it.next().getId(), b216.getId());
        assertEquals(it.next().getId(), b217.getId());
        assertEquals(it.next().getId(), b219.getId());
        assertFalse(it.hasNext());

        assertEquals(3, groups.size());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testRemoveGroup() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final DaoGroup loacalB219 = DaoGroup.getByName("b219");
        final DaoMember localYo = DaoMember.getByLogin("Yo");

        localYo.removeFromGroup(loacalB219);

        final Iterator<DaoGroup> it = localYo.getGroups().iterator();
        assertEquals(it.next().getId(), b216.getId());
        assertEquals(it.next().getId(), b217.getId());
        assertFalse(it.hasNext());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testRemoveMember() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final DaoGroup localB216 = DaoGroup.getByName("b216");
        final DaoMember loaclYo = DaoMember.getByLogin("Yo");

        localB216.removeMember(loaclYo);

        final Iterator<DaoGroup> it = loaclYo.getGroups().iterator();
        assertEquals(it.next().getId(), b217.getId());
        assertEquals(it.next().getId(), b219.getId());
        assertFalse(it.hasNext());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testDuplicateAdd() {
        SessionManager.beginWorkUnit();

        DaoMember.getByLogin(fred.getLogin()).addToGroup(DaoGroup.getByName(b219.getLogin()), false);
        DaoMember.getByLogin(fred.getLogin()).addToGroup(DaoGroup.getByName(b219.getLogin()), false);

        try {
            SessionManager.flush();
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }

        SessionManager.endWorkUnitAndFlush();
    }

}
