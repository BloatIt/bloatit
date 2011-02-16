package com.bloatit.data;

import junit.framework.TestCase;

import org.hibernate.HibernateException;

import com.bloatit.framework.exceptions.NonOptionalParameterException;

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
        DaoGroup.createAndPersiste("Other", "plop@plop.com", "A group description", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("myGroup", "plop1@plop.com", "A group description", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b218", "plop3@plop.com", "A group description", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b217", "plop4@plop.com", "A group description", DaoGroup.Right.PUBLIC);
        DaoGroup.createAndPersiste("b216", "plop5@plop.com", "A group description", DaoGroup.Right.PUBLIC);
        SessionManager.endWorkUnitAndFlush();
    }

    public void testCreateGroupLimite() {
        SessionManager.beginWorkUnit();
        try {
            DaoGroup.createAndPersiste("b217", "", "A group description", DaoGroup.Right.PUBLIC);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoGroup.createAndPersiste("", "plop3@plop.com", "A group description", DaoGroup.Right.PUBLIC);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoGroup.createAndPersiste("b219", "plop2@plop.com", "A group description", null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoGroup.createAndPersiste("myGroup", null, "A group description", DaoGroup.Right.PUBLIC);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoGroup.createAndPersiste(null, "plop@plop.com", "A group description", DaoGroup.Right.PUBLIC);
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
            DaoGroup.createAndPersiste("Other", "plop@plop.com", "A group description" ,DaoGroup.Right.PUBLIC);
            assertTrue(true);
            SessionManager.endWorkUnitAndFlush();
            assertTrue(false);
        } catch (final HibernateException e) {
            assertTrue(true);
        }
    }

    public void testGetGroupByName() {
        SessionManager.beginWorkUnit();
        final DaoGroup b219 = DaoGroup.createAndPersiste("b219", "plop2@plop.com", "A group description" ,DaoGroup.Right.PUBLIC);

        assertEquals(b219.getId(), DaoGroup.getByName("b219").getId());
        assertNull(DaoGroup.getByName("Inexistant"));
        assertNull(DaoGroup.getByName(null));
        SessionManager.endWorkUnitAndFlush();
    }

}
