//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import java.util.Iterator;
import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.utils.PageIterable;

/**
 * Unit test for Member and groups
 */
public class DaoGroupMemberTest extends TestCase {

    private DaoMember tom;
    private DaoMember fred;
    private DaoMember yo;
    private DaoTeam b216;
    private DaoTeam b217;
    private DaoTeam b219;

    public DaoGroupMemberTest(final String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();

        SessionManager.beginWorkUnit();
        tom = DaoMember.createAndPersist("Thomas", "password", "salt", "tom@gmail.com", Locale.FRANCE);
        tom.setFullname("Thomas Guyard");
        fred = DaoMember.createAndPersist("Fred", "other", "salt", "fred@gmail.com", Locale.FRANCE);
        fred.setFullname("Frédéric Bertolus");
        yo = DaoMember.createAndPersist("Yoann", "plop", "salt", "yo@gmail.com", Locale.FRANCE);
        yo.setFullname("Yoann Plénet");

        b219 = DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        b216 = DaoTeam.createAndPersiste("b216", "plop3@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        b217 = DaoTeam.createAndPersiste("b217", "plop4@plop.com", "A group description", DaoTeam.Right.PUBLIC);

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
        DaoMember.getByLogin(fred.getLogin()).addToTeam(DaoTeam.getByName(b219.getLogin()));
        DaoMember.getByLogin(yo.getLogin()).addToTeam(DaoTeam.getByName(b219.getLogin()));
        DaoMember.getByLogin(yo.getLogin()).addToTeam(DaoTeam.getByName(b217.getLogin()));
        DaoMember.getByLogin(yo.getLogin()).addToTeam(DaoTeam.getByName(b216.getLogin()));
        SessionManager.endWorkUnitAndFlush();
    }

    public void testGetAllUserInGroup() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final PageIterable<DaoMember> members = DaoTeam.getByName(b219.getLogin()).getMembers();
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
        final PageIterable<DaoTeam> groups = DaoMember.getByLogin("Yoann").getTeams();
        final Iterator<DaoTeam> it = groups.iterator();
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
        final DaoTeam loacalB219 = DaoTeam.getByName("b219");
        final DaoMember localYo = DaoMember.getByLogin("Yoann");

        localYo.removeFromTeam(loacalB219);

        final Iterator<DaoTeam> it = localYo.getTeams().iterator();
        assertEquals(it.next().getId(), b216.getId());
        assertEquals(it.next().getId(), b217.getId());
        assertFalse(it.hasNext());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testRemoveMember() {
        testAddUserToGroup();

        SessionManager.beginWorkUnit();
        final DaoTeam localB216 = DaoTeam.getByName("b216");
        final DaoMember loaclYo = DaoMember.getByLogin("Yoann");

        localB216.removeMember(loaclYo);

        final Iterator<DaoTeam> it = loaclYo.getTeams().iterator();
        assertEquals(it.next().getId(), b217.getId());
        assertEquals(it.next().getId(), b219.getId());
        assertFalse(it.hasNext());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testDuplicateAdd() {
        SessionManager.beginWorkUnit();

        DaoMember.getByLogin(fred.getLogin()).addToTeam(DaoTeam.getByName(b219.getLogin()));
        try {
            DaoMember.getByLogin(fred.getLogin()).addToTeam(DaoTeam.getByName(b219.getLogin()));
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }

        SessionManager.endWorkUnitAndFlush();
    }

    public void testAddRight() {
        SessionManager.beginWorkUnit();

        final DaoMember fred = DaoMember.getByLogin(this.fred.getLogin());
        final DaoTeam b219 = DaoTeam.getByName(this.b219.getLogin());
        fred.addToTeam(b219);
        fred.addTeamRight(b219, UserTeamRight.CONSULT);
        fred.addTeamRight(b219, UserTeamRight.TALK);
        if (!(fred.getTeamRights(b219).contains(UserTeamRight.CONSULT) && fred.getTeamRights(b219).contains(UserTeamRight.TALK))) {
            fail();
        }

        SessionManager.endWorkUnitAndFlush();
    }

    public void testRemoveRight() {
        SessionManager.beginWorkUnit();

        final DaoMember fred = DaoMember.getByLogin(this.fred.getLogin());
        final DaoTeam b219 = DaoTeam.getByName(this.b219.getLogin());

        fred.addToTeam(b219);
        fred.addTeamRight(b219, UserTeamRight.CONSULT);
        fred.addTeamRight(b219, UserTeamRight.TALK);

        fred.removeTeamRight(b219, UserTeamRight.TALK);
        if (fred.getTeamRights(b219).contains(UserTeamRight.TALK) || !fred.getTeamRights(b219).contains(UserTeamRight.CONSULT)) {
            fail();
        }

        SessionManager.endWorkUnitAndFlush();
    }

}
