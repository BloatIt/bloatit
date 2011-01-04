package com.bloatit.model.data;

import junit.framework.TestCase;

import org.hibernate.HibernateException;

import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;

public class GroupCreationTest extends TestCase {
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
        DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("myGroup", "plop1@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b219", "plop2@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b218", "plop3@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b217", "plop4@plop.com", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b216", "plop5@plop.com", DaoGroup.Right.PUBLIC);
        SessionManager.endWorkUnitAndFlush();

    }

    public void testCreateGroupLimite() {
        SessionManager.beginWorkUnit();
        try {
            DaoGroup.createAndPersiste("b217", "", DaoGroup.Right.PUBLIC);
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoGroup.createAndPersiste("", "plop3@plop.com", DaoGroup.Right.PUBLIC);
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoGroup.createAndPersiste("b219", "plop2@plop.com", null);
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoGroup.createAndPersiste("myGroup", null, DaoGroup.Right.PUBLIC);
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoGroup.createAndPersiste(null, "plop@plop.com", DaoGroup.Right.PUBLIC);
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
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
        SessionManager.beginWorkUnit();
        DaoGroup b219 = DaoGroup.createAndPersiste("b219", "plop2@plop.com", DaoGroup.Right.PUBLIC);

        assertEquals(b219.getId(), DaoGroup.getByName("b219").getId());
        assertNull(DaoGroup.getByName("Inexistant"));
        assertNull(DaoGroup.getByName(null));
        SessionManager.endWorkUnitAndFlush();
    }

}
