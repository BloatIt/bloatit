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
package com.bloatit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import javassist.NotFoundException;

import org.junit.Test;

import com.bloatit.data.DaoMember;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateAccessException;
import com.bloatit.framework.webprocessor.context.User.ActivationState;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.model.right.AuthToken;

public class MemberTest extends ModelTestUnit {

    @Test
    public void testAddToPublicGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        final DaoMember daouser = new Member("User", "password", "user@gmail.com", Locale.FRANCE).getDao();
        daouser.setFullname("Thomas Guyard");
        daouser.setActivationState(ActivationState.ACTIVE);
        final Member user = Member.create(daouser);

        user.authenticate(new AuthToken(user));
        user.addToPublicTeam(TeamManager.getByName("publicGroup"));
        assertTrue(user.isInTeam(TeamManager.getByName("publicGroup")));

        assertTrue(yo.isInTeam(TeamManager.getByName("publicGroup")));
        yo.authenticate(yoAuthToken);
        try {
            yo.addToPublicTeam(TeamManager.getByName("publicGroup"));
            fail();
        } catch (final BadProgrammerException e) {
            assertTrue(true);
        }

        try {
            yo.authenticate(fredAuthToken);
            // A user can only add himself to a public group.
            yo.addToPublicTeam(TeamManager.getByName("publicGroup"));
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }

    }

    @Test
    public void testRemoveFromGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        yo.authenticate(yoAuthToken);
        yo.kickFromTeam(TeamManager.getByName("publicGroup"), yo);
        assertFalse(yo.isInTeam(TeamManager.getByName("publicGroup")));

        try {
            yo.authenticate(fredAuthToken);
            // A user can only remove himself from a group.
            yo.kickFromTeam(TeamManager.getByName("publicGroup"), fredAuthToken.getMember());
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testInviteIntoProtectedGroup() {
        MemberManager.getMemberByLogin("Yoann");
        MemberManager.getMemberByLogin("Fred");
    }

    @Test
    public void testInviteIntoProtectedAndRefuseGroup() {
        MemberManager.getMemberByLogin("Yoann");
        MemberManager.getMemberByLogin("Fred");

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

    @Test
    public void testGetKarma() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        yo.authenticate(yoAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(fredAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(tomAuthToken);
        assertEquals(0, yo.getKarma());
    }

    @Test
    public void testSetFullName() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        yo.authenticate(yoAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(fredAuthToken);
        assertEquals(0, yo.getKarma());
        yo.authenticate(tomAuthToken);
        assertEquals(0, yo.getKarma());
    }

    @Test
    public void testGetFullname() {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        yo.authenticate(yoAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
        yo.authenticate(fredAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
        yo.authenticate(tomAuthToken);
        assertEquals("Yoann Plénet", yo.getFullname());
    }

    @Test
    public void testSetFullname() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

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

    @Test
    public void testSetPassword() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        yo.authenticate(yoAuthToken);
        yo.setPassword("Coucou");

        try {
            new AuthToken("Yoann", "Coucou");
        } catch (final NotFoundException e) {
            fail();
        }
    }

    @Test
    public void testGetFeatures() {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        assertEquals("Mon titre", yo.getFeatures(false).iterator().next().getTitle());

        yo.authenticate(yoAuthToken);
        assertEquals("Mon titre", yo.getFeatures(false).iterator().next().getTitle());

        yo.authenticate(fredAuthToken);
        assertEquals("Mon titre", yo.getFeatures(false).iterator().next().getTitle());
    }

    @Test
    public void testGetKudos() {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        try {
            assertEquals(1, yo.getKudos().size());
            fail();
        } catch (final UnauthorizedPrivateAccessException e) {
            assertTrue(true);
        }

        try {
            yo.authenticate(yoAuthToken);
            assertEquals(1, yo.getKudos().size());
        } catch (final UnauthorizedPrivateAccessException e) {
            fail();
        }

        try {
            yo.authenticate(fredAuthToken);
            assertEquals(1, yo.getKudos().size());
            fail();
        } catch (final UnauthorizedPrivateAccessException e) {
            assertTrue(true);
        }
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
