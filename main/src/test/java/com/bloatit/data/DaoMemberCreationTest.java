/*
 * 
 */
package com.bloatit.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.hibernate.HibernateException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.exceptions.lowlevel.MalformedArgumentException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

public class DaoMemberCreationTest {

    @BeforeClass
    public static void createDB() {
        SessionManager.generateTestSessionFactory();
    }
    
    @AfterClass
    public static void closeDB() {
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }
    
    @Before
    public void setUp() throws Exception {
        SessionManager.beginWorkUnit();
    }

    @After
    public void tearDown() throws Exception {
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.rollback();
        }
    }

    @Test
    public void testCreateMember() {
        final DaoMember theMember = DaoMember.createAndPersist("Thomas", "password", "salt", "tom@gmail.com", Locale.FRANCE);

        assertEquals(theMember.getEmail(), "tom@gmail.com");
        assertEquals(theMember.getFullname(), "");
        assertEquals(theMember.getLogin(), "Thomas");
        assertEquals(theMember.getPassword(), "password");
        assertEquals(theMember.getKarma().intValue(), 0);
        assertEquals(theMember.getRole(), Role.NORMAL);
        assertEquals(theMember.getLocale(), Locale.FRANCE);

        theMember.setFullname("Thomas Guyard");
        assertEquals(theMember.getFullname(), "Thomas Guyard");
        theMember.setEmail("Test@nowhere.com");
        assertEquals(theMember.getEmail(), "Test@nowhere.com");
        theMember.setPassword("Hello");
        assertEquals(theMember.getPassword(), "Hello");
        theMember.setRole(Role.ADMIN);
        assertEquals(theMember.getRole(), Role.ADMIN);
        theMember.setLocale(Locale.CHINA);
        assertEquals(theMember.getLocale(), Locale.CHINA);
    }

    @Test
    public void testCreateTreeMembers() {
        {
            final DaoMember theMember = DaoMember.createAndPersist("Thomas", "password", "salt", "tom@gmail.com", Locale.FRANCE);
            theMember.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            final DaoMember theMember = DaoMember.createAndPersist("Fred", "other", "salt", "fred@gmail.com", Locale.FRANCE);
            theMember.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            final DaoMember theMember = DaoMember.createAndPersist("Yoann", "plop", "salt", "yo@gmail.com", Locale.FRANCE);
            theMember.setFullname("Yoann Plénet");
        }

        assertEquals(3, DBRequests.getAll(DaoMember.class).size());
    }

    @Test
    public void testCreateMemberLimit() {
        try {
            DaoMember.createAndPersist(null, "pass", "salt", "mail@nowhere.com", Locale.FRANCE);
            assertTrue(false);
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", null, "salt", "mail@nowhere.com", Locale.FRANCE);
            assertTrue(false);
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", "pass", "salt", null, Locale.FRANCE);
            assertTrue(false);
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", "pass", "salt", "ZDQSDV", null);
            assertTrue(false);
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("", "pass", "salt", "mail@nowhere.com", Locale.FRANCE);
            assertTrue(false);
        } catch (final MalformedArgumentException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", "", "salt", "mail@nowhere.com", Locale.FRANCE);
            assertTrue(false);
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoMember.createAndPersist("login", "pass", "salt", "", Locale.FRANCE);
            assertTrue(false);
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testMemberDuplicateCreation() {
        try {
            DaoMember.createAndPersist("Yoann", "plop", "salt", "yo@gmail.com", Locale.FRANCE);
            SessionManager.flush();
            // duplicate login
            DaoMember.createAndPersist("Yoann", "plip", "salt", "yoyo@gmail.com", Locale.FRANCE);
            SessionManager.endWorkUnitAndFlush();
            fail();
        } catch (final HibernateException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetMemberByLogin() {
        testCreateTreeMembers();
        assertNotNull(DaoMember.getByLogin("Fred"));
        assertNull(DaoMember.getByLogin("Inexistant"));
        assertNull(DaoMember.getByLogin(null));
    }

    @Test
    public void testGetMemberByLoginAndPassword() {
        testCreateTreeMembers();
        assertNotNull(DaoMember.getByLoginAndPassword("Fred", "other"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "notTheGoodPassword"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "other "));
        assertNull(DaoMember.getByLoginAndPassword("Fred", " other"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "other\n"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "othe"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "o"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", ""));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "o*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "[a-z]*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "othe?"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "othe."));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "\\*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", ".*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "\\.*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "\\.\\*"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", "' OR 1=1; --"));
        assertNull(DaoMember.getByLoginAndPassword("Fred", null));
        assertNull(DaoMember.getByLoginAndPassword(null, "' OR 1=1; --"));
        assertNull(DaoMember.getByLoginAndPassword("Inexistant", "' OR 1=1; --"));
    }

    @Test
    public void testExistMemberByLogin() {
        testCreateTreeMembers();
        assertTrue(DaoActor.loginExists("Fred"));
        assertFalse(DaoActor.loginExists("Inexistant"));
        assertFalse(DaoActor.loginExists(null));
    }

}
