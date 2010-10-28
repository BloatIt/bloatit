package com.bloatit.framework;

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
        yoAuthToken = new AuthToken(MemberManager.getMemberByLogin("Yo"), "plop");
        tomAuthToken = new AuthToken(MemberManager.getMemberByLogin("Thomas"), "plip");
        fredAuthToken = new AuthToken(MemberManager.getMemberByLogin("Fred"), "plap");
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
        SessionManager.beginWorkUnit();
        Member yo = MemberManager.getMemberByLogin("Yo");
        
        yo.unLock(yoAuthToken);
        yo.addToGroup(GroupManager.getByName("ubuntuUsers"), false);
        yo.lock();
        
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
        fail("Not yet implemented");
    }

    public void testGetGroups() {
        fail("Not yet implemented");
    }

    public void testGetKarma() {
        fail("Not yet implemented");
    }

    public void testAddToKarma() {
        fail("Not yet implemented");
    }

    public void testGetFirstname() {
        fail("Not yet implemented");
    }

    public void testSetFirstname() {
        fail("Not yet implemented");
    }

    public void testGetFullName() {
        fail("Not yet implemented");
    }

    public void testGetLastname() {
        fail("Not yet implemented");
    }

    public void testSetLastname() {
        fail("Not yet implemented");
    }

    public void testGetPassword() {
        fail("Not yet implemented");
    }

    public void testSetPassword() {
        fail("Not yet implemented");
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
