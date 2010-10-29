package com.bloatit.framework;

import javassist.NotFoundException;

import com.bloatit.framework.managers.GroupManager;
import com.bloatit.framework.managers.MemberManager;
import com.bloatit.model.data.util.SessionManager;

import junit.framework.TestCase;

public class MemberTest extends TestCase {

    AuthToken yoAuthToken;
    AuthToken tomAuthToken;
    AuthToken fredAuthToken;
    TestDB db;

    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.reCreateSessionFactory();
        db = new TestDB();
        SessionManager.beginWorkUnit();
        yoAuthToken = new AuthToken("Yo", "plop");
        tomAuthToken = new AuthToken("Thomas", "password");
        fredAuthToken = new AuthToken("Fred", "other");
        SessionManager.endWorkUnitAndFlush();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }

    public void testAddToGroup() {
        // TODO correct the right management in groups
        SessionManager.beginWorkUnit();
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.unLock(yoAuthToken);
        yo.addToGroup(GroupManager.getByName("ubuntuUsers"), false);

        assertTrue(yo.isInGroup(GroupManager.getByName("ubuntuUsers")));

        try {
            yo.unLock(fredAuthToken);
            yo.addToGroup(GroupManager.getByName("ubuntuUsers"), false);
            fail();
        } catch (Exception e) {
            yo.lock();
        }

        SessionManager.endWorkUnitAndFlush();
    }

    public void testRemoveFromGroup() {
        SessionManager.beginWorkUnit();
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.unLock(yoAuthToken);
        yo.removeFromGroup(GroupManager.getByName("b219"));
        assertFalse(yo.isInGroup(GroupManager.getByName("b219")));

        try {
            yo.unLock(fredAuthToken);
            yo.removeFromGroup(GroupManager.getByName("b219"));
            fail();
        } catch (Exception e) {
            yo.lock();
        }

        SessionManager.endWorkUnitAndFlush();
    }

    public void testGetGroups() {
        // Here the right thing would be to show all
        // the public and protected group to everyone
        // the private group to the member of the private group

        fail("Not yet implemented");
    }

    public void testGetKarma() {
        SessionManager.beginWorkUnit();
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.unLock(yoAuthToken);
        assertEquals(0, yo.getKarma());
        yo.unLock(fredAuthToken);
        assertEquals(0, yo.getKarma());
        yo.unLock(tomAuthToken);
        assertEquals(0, yo.getKarma());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testSetFullName() {
        SessionManager.beginWorkUnit();
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.unLock(yoAuthToken);
        assertEquals(0, yo.getKarma());
        yo.unLock(fredAuthToken);
        assertEquals(0, yo.getKarma());
        yo.unLock(tomAuthToken);
        assertEquals(0, yo.getKarma());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testGetFullname() {
        SessionManager.beginWorkUnit();
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.unLock(yoAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
        yo.unLock(fredAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
        yo.unLock(tomAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testSetFullname() {
        SessionManager.beginWorkUnit();
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.unLock(yoAuthToken);
        yo.setFullname("Plénet Yoann");
        
        try {
            yo.unLock(fredAuthToken);
            yo.setFullname("plop");
            fail();
        } catch (Exception e) {}
        
        assertEquals("Yoann Plénet", yo.getFullname());
        
        SessionManager.endWorkUnitAndFlush();
    }

    public void testSetPassword() {
        SessionManager.beginWorkUnit();
        Member yo = MemberManager.getMemberByLogin("Yo");
        
        yo.unLock(yoAuthToken);
        yo.setPassword("Coucou");
        
        try {
            new AuthToken("Yo", "Coucou");
        } catch (NotFoundException e) {
            fail();
        }
    }

    public void testGetDemands() {
        fail("Not yet implemented");
    }

    public void testGetKudos() {
        fail("Not yet implemented");
    }

    public void testGetSpecifications() {
        fail("Not yet implemented");
    }

    public void testGetContributions() {
        fail("Not yet implemented");
    }

    public void testGetComments() {
        fail("Not yet implemented");
    }

    public void testGetOffers() {
        fail("Not yet implemented");
    }

    public void testGetTranslations() {
        fail("Not yet implemented");
    }

    public void testIsInGroup() {
        fail("Not yet implemented");
    }

    public void testGetEmail() {
        fail("Not yet implemented");
    }

    public void testSetEmail() {
        fail("Not yet implemented");
    }

    public void testGetLogin() {
        fail("Not yet implemented");
    }

    public void testGetDateCreation() {
        fail("Not yet implemented");
    }

    public void testCanGetInternalAccount() {
        fail("Not yet implemented");
    }

    public void testGetInternalAccount() {
        fail("Not yet implemented");
    }

    public void testGetExternalAccount() {
        fail("Not yet implemented");
    }

    public void testSetExternalAccount() {
        fail("Not yet implemented");
    }

}
