package com.bloatit.model;

import javassist.NotFoundException;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.managers.GroupManager;
import com.bloatit.model.managers.MemberManager;

public class MemberTest extends FrameworkTestUnit {

    public void testAddToPublicGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.addToPublicGroup(GroupManager.getByName("ubuntuUsers"));

        assertTrue(yo.isInGroup(GroupManager.getByName("ubuntuUsers")));

        try {
            yo.authenticate(fredAuthToken);
            // A user can only add himself to a public group.
            yo.addToPublicGroup(GroupManager.getByName("ubuntuUsers"));
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }

    }

    public void testRemoveFromGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.removeFromGroup(GroupManager.getByName("b219"));
        assertFalse(yo.isInGroup(GroupManager.getByName("b219")));

        try {
            yo.authenticate(fredAuthToken);
            // A user can only remove himself from a group.
            yo.removeFromGroup(GroupManager.getByName("b219"));
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }
    }

    public void testInviteIntoProtectedGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");
        final Member fred = MemberManager.getMemberByLogin("Fred");

        yo.authenticate(yoAuthToken);
        yo.invite(fred, GroupManager.getByName("other"));
        assertFalse(fred.isInGroup(GroupManager.getByName("other")));

        fred.authenticate(fredAuthToken);
        fred.acceptInvitation(GroupManager.getInvitation(GroupManager.getByName("other"), fred));
        assertTrue(fred.isInGroup(GroupManager.getByName("other")));
    }

    public void testInviteIntoProtectedAndRefuseGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");
        final Member fred = MemberManager.getMemberByLogin("Fred");

        yo.authenticate(yoAuthToken);
        yo.invite(fred, GroupManager.getByName("other"));
        assertFalse(fred.isInGroup(GroupManager.getByName("other")));

        fred.authenticate(fredAuthToken);
        fred.refuseInvitation(GroupManager.getInvitation(GroupManager.getByName("other"), fred));
        assertFalse(fred.isInGroup(GroupManager.getByName("other")));
    }

    public void testGetKarma() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(fredAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(tomAuthToken);
        assertEquals(0, yo.getKarma());
    }

    public void testSetFullName() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(fredAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(tomAuthToken);
        assertEquals(0, yo.getKarma());
    }

    public void testGetFullname() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
        yo.authenticate(fredAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
        yo.authenticate(tomAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
    }

    public void testSetFullname() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.setFullname("Plénet Yoann");

        try {
            yo.authenticate(fredAuthToken);
            yo.setFullname("plop");
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }

        assertEquals("Plénet Yoann", yo.getFullname());
    }

    public void testSetPassword() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.setPassword("Coucou");

        try {
            new AuthToken("Yo", "Coucou");
        } catch (final NotFoundException e) {
            fail();
        }
    }

    public void testGetDemands() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        assertEquals("Mon titre", yo.getDemands().iterator().next().getTitle());

        yo.authenticate(yoAuthToken);
        assertEquals("Mon titre", yo.getDemands().iterator().next().getTitle());

        yo.authenticate(fredAuthToken);
        assertEquals("Mon titre", yo.getDemands().iterator().next().getTitle());
    }

    public void testGetKudos() {
        final Member yo = MemberManager.getMemberByLogin("Yo");

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