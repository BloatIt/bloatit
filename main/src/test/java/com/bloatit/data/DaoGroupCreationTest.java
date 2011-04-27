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

import junit.framework.TestCase;

import org.hibernate.HibernateException;

import com.bloatit.framework.exceptions.lowlevel.MalformedArgumentException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

public class DaoGroupCreationTest extends TestCase {
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

    public void testCreateGroup() {
        SessionManager.beginWorkUnit();
        DaoTeam.createAndPersiste("Other", "plop@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        DaoTeam.createAndPersiste("myGroup", "plop1@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        DaoTeam.createAndPersiste("b218", "plop3@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        DaoTeam.createAndPersiste("b217", "plop4@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        DaoTeam.createAndPersiste("b216", "plop5@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        SessionManager.endWorkUnitAndFlush();
    }

    public void testCreateGroupLimite() {
        SessionManager.beginWorkUnit();
        try {
            DaoTeam.createAndPersiste("b217", "", "A group description", DaoTeam.Right.PUBLIC);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoTeam.createAndPersiste("", "plop3@plop.com", "A group description", DaoTeam.Right.PUBLIC);
            fail();
        } catch (final MalformedArgumentException e) {
            assertTrue(true);
        }
        try {
            DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoTeam.createAndPersiste("myGroup", null, "A group description", DaoTeam.Right.PUBLIC);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoTeam.createAndPersiste(null, "plop@plop.com", "A group description", DaoTeam.Right.PUBLIC);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        SessionManager.endWorkUnitAndFlush();
    }

    public void testDuplicatedGroup() {
        testCreateGroup();
        try {
            SessionManager.beginWorkUnit();
            DaoTeam.createAndPersiste("Other", "plop@plop.com", "A group description", DaoTeam.Right.PUBLIC);
            assertTrue(true);
            SessionManager.endWorkUnitAndFlush();
            assertTrue(false);
        } catch (final HibernateException e) {
            assertTrue(true);
        }
    }

    public void testGetGroupByName() {
        SessionManager.beginWorkUnit();
        final DaoTeam b219 = DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoTeam.Right.PUBLIC);

        assertEquals(b219.getId(), DaoTeam.getByName("b219").getId());
        assertNull(DaoTeam.getByName("Inexistant"));
        assertNull(DaoTeam.getByName(null));
        SessionManager.endWorkUnitAndFlush();
    }

}
