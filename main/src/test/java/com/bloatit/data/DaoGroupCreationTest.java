package com.bloatit.data;

import junit.framework.TestCase;

import org.hibernate.HibernateException;

import com.bloatit.framework.exceptions.specific.NonOptionalParameterException;

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
        } catch (final NonOptionalParameterException e) {
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
