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
import com.bloatit.data.exceptions.ElementNotFoundException;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.context.User.ActivationState;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;

public class MemberTest extends ModelTestUnit {

    @Test
    public void testAddToPublicGroup() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        final DaoMember daouser = new Member("User", "password", "user@gmail.com", Locale.FRANCE).getDao();
        daouser.setFullname("Thomas Guyard");
        daouser.setActivationState(ActivationState.ACTIVE);
        final Member user = Member.create(daouser);

        AuthToken.authenticate(user);
        user.addToPublicTeam(TeamManager.getByName("publicGroup"));
        assertTrue(user.isInTeam(TeamManager.getByName("publicGroup")));

        assertTrue(yo.isInTeam(TeamManager.getByName("publicGroup")));
        AuthToken.authenticate(memberYo);
        try {
            yo.addToPublicTeam(TeamManager.getByName("publicGroup"));
            fail();
        } catch (final BadProgrammerException e) {
            assertTrue(true);
        }

        try {
            AuthToken.authenticate(memeberFred);
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

        AuthToken.authenticate(memberYo);
        yo.kickFromTeam(TeamManager.getByName("publicGroup"), yo);
        assertFalse(yo.isInTeam(TeamManager.getByName("publicGroup")));

        try {
            AuthToken.authenticate(memeberFred);
            // A user can only remove himself from a group.
            yo.kickFromTeam(TeamManager.getByName("publicGroup"), memeberFred);
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
    public void testGetKarma() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        AuthToken.authenticate(memberYo);
        assertEquals(0, yo.getKarma());
        AuthToken.authenticate(memeberFred);
        assertEquals(0, yo.getKarma());
        AuthToken.authenticate(memberTom);
        assertEquals(0, yo.getKarma());
    }

    @Test
    public void testSetFullName() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        AuthToken.authenticate(memberYo);
        assertEquals(0, yo.getKarma());
        AuthToken.authenticate(memeberFred);
        assertEquals(0, yo.getKarma());
        AuthToken.authenticate(memberTom);
        assertEquals(0, yo.getKarma());
    }

    @Test
    public void testGetFullname() {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        AuthToken.authenticate(memberYo);
        assertEquals("Yoann Plénet", yo.getFullname());
        AuthToken.authenticate(memeberFred);
        assertEquals("Yoann Plénet", yo.getFullname());
        AuthToken.authenticate(memberTom);
        assertEquals("Yoann Plénet", yo.getFullname());
    }

    @Test
    public void testSetFullname() throws UnauthorizedOperationException {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        AuthToken.authenticate(memberYo);
        yo.setFullname("Plénet Yoann");

        try {
            AuthToken.authenticate(memeberFred);
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

        AuthToken.authenticate(memberYo);
        yo.setPassword("Coucou");

        try {
            AuthToken.authenticate("Yoann", "Coucou");
        } catch (final ElementNotFoundException e) {
            fail();
        }
    }

    @Test
    public void testGetFeatures() {
        final Member yo = MemberManager.getMemberByLogin("Yoann");

        assertEquals("Mon titre", yo.getFeatures(false).iterator().next().getTitle());

        AuthToken.authenticate(memberYo);
        assertEquals("Mon titre", yo.getFeatures(false).iterator().next().getTitle());

        AuthToken.authenticate(memeberFred);
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
            AuthToken.authenticate(memberYo);
            assertEquals(1, yo.getKudos().size());
        } catch (final UnauthorizedPrivateAccessException e) {
            fail();
        }

        try {
            AuthToken.authenticate(memeberFred);
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
