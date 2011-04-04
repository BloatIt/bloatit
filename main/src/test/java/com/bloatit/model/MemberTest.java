package com.bloatit.model;

import javassist.NotFoundException;

import com.bloatit.framework.exceptions.specific.UnauthorizedOperationException;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.right.AuthToken;

public class MemberTest extends ModelTestUnit {

    public void testAddToPublicGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.addToPublicTeam(TeamManager.getByName("ubuntuUsers"));

        assertTrue(yo.isInTeam(TeamManager.getByName("ubuntuUsers")));

        try {
            yo.authenticate(fredAuthToken);
            // A user can only add himself to a public group.
            yo.addToPublicTeam(TeamManager.getByName("ubuntuUsers"));
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }

    }

    public void testRemoveFromGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        yo.authenticate(yoAuthToken);
        yo.removeFromTeam(TeamManager.getByName("b219"));
        assertFalse(yo.isInTeam(TeamManager.getByName("b219")));

        try {
            yo.authenticate(fredAuthToken);
            // A user can only remove himself from a group.
            yo.removeFromTeam(TeamManager.getByName("b219"));
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }
    }

    public void testInviteIntoProtectedGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");
        final Member fred = MemberManager.getMemberByLogin("Fred");
        // TODO
        // yo.authenticate(yoAuthToken);
        // yo.sendInvitation(fred, GroupManager.getByName("other"));
        // assertFalse(fred.isInGroup(GroupManager.getByName("other")));
        //
        // fred.authenticate(fredAuthToken);
        // fred.acceptInvitation(GroupManager.getInvitation(GroupManager.getByName("other"),
        // fred));
        // assertTrue(fred.isInGroup(GroupManager.getByName("other")));
    }

    public void testInviteIntoProtectedAndRefuseGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");
        final Member fred = MemberManager.getMemberByLogin("Fred");

        // TODO
        // yo.authenticate(yoAuthToken);
        // yo.sendInvitation(fred, GroupManager.getByName("other"));
        // assertFalse(fred.isInGroup(GroupManager.getByName("other")));
        //
        // fred.authenticate(fredAuthToken);
        // fred.refuseInvitation(GroupManager.getInvitation(GroupManager.getByName("other"),
        // fred));
        // assertFalse(fred.isInGroup(GroupManager.getByName("other")));
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

    public void testGetFeatures() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yo");

        assertEquals("Mon titre", yo.getFeatures().iterator().next().getTitle());

        yo.authenticate(yoAuthToken);
        assertEquals("Mon titre", yo.getFeatures().iterator().next().getTitle());

        yo.authenticate(fredAuthToken);
        assertEquals("Mon titre", yo.getFeatures().iterator().next().getTitle());
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
