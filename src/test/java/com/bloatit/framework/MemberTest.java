package com.bloatit.framework;

import javassist.NotFoundException;

import com.bloatit.framework.managers.GroupManager;
import com.bloatit.framework.managers.MemberManager;

public class MemberTest extends SuperTest {

    public void testAddToGroup() {
        // TODO correct the right management in groups
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.addToGroup(GroupManager.getByName("ubuntuUsers"), false);

        assertTrue(yo.isInGroup(GroupManager.getByName("ubuntuUsers")));

        try {
            yo.authenticate(fredAuthToken);
            yo.addToGroup(GroupManager.getByName("ubuntuUsers"), false);
            fail();
        } catch (Exception e) {
        }

    }

    public void testRemoveFromGroup() {
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.removeFromGroup(GroupManager.getByName("b219"));
        assertFalse(yo.isInGroup(GroupManager.getByName("b219")));

        try {
            yo.authenticate(fredAuthToken);
            yo.removeFromGroup(GroupManager.getByName("b219"));
            fail();
        } catch (Exception e) {
        }
    }

    public void testGetGroups() {
        // Here the right thing would be to show all
        // the public and protected group to everyone
        // the private group to the member of the private group

        fail("Not yet implemented");
    }

    public void testGetKarma() {
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(fredAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(tomAuthToken);
        assertEquals(0, yo.getKarma());
    }

    public void testSetFullName() {
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(fredAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(tomAuthToken);
        assertEquals(0, yo.getKarma());
    }

    public void testGetFullname() {
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
        yo.authenticate(fredAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
        yo.authenticate(tomAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
    }

    public void testSetFullname() {
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.setFullname("Plénet Yoann");

        try {
            yo.authenticate(fredAuthToken);
            yo.setFullname("plop");
            fail();
        } catch (Exception e) {}

        assertEquals("Plénet Yoann", yo.getFullname());
    }

    public void testSetPassword() {
        Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.setPassword("Coucou");

        try {
            new AuthToken("Yo", "Coucou");
        } catch (NotFoundException e) {
            fail();
        }
    }

    public void testGetDemands() {
        Member yo = MemberManager.getMemberByLogin("Yo");

        assertEquals("Mon titre", yo.getDemands().iterator().next().getTitle());

        yo.authenticate(yoAuthToken);
        assertEquals("Mon titre", yo.getDemands().iterator().next().getTitle());

        yo.authenticate(fredAuthToken);
        assertEquals("Mon titre", yo.getDemands().iterator().next().getTitle());
    }

    public void testGetKudos() {
        Member yo = MemberManager.getMemberByLogin("Yo");

        assertEquals(1, yo.getKudos().size());

        yo.authenticate(yoAuthToken);
        assertEquals(1, yo.getKudos().size());

        yo.authenticate(fredAuthToken);
        assertEquals(1, yo.getKudos().size());
    }

    // public void testGetSpecifications() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetContributions() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetComments() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetOffers() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetTranslations() {
    // fail("Not yet implemented");
    // }
    //
    // public void testIsInGroup() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetEmail() {
    // fail("Not yet implemented");
    // }
    //
    // public void testSetEmail() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetLogin() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetDateCreation() {
    // fail("Not yet implemented");
    // }
    //
    // public void testCanGetInternalAccount() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetInternalAccount() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetExternalAccount() {
    // fail("Not yet implemented");
    // }
    //
    // public void testSetExternalAccount() {
    // fail("Not yet implemented");
    // }

}
