package com.bloatit.model;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateAccessException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPublicReadOnlyAccessException;

public class ActorTest extends ModelTestUnit {

    public final void testGetLogin() {
        final Member tom = Member.create(db.getTom());
        assertEquals(tom.getLogin(), db.getTom().getLogin());

        final Team publicTeam = Team.create(db.getPublicGroup());
        assertEquals(publicTeam.getLogin(), db.getPublicGroup().getLogin());
    }

    public final void testGetDateCreation() {
        final Member tom = Member.create(db.getTom());
        try {
            assertEquals(tom.getDateCreation(), db.getTom().getDateCreation());
        } catch (final UnauthorizedPublicReadOnlyAccessException e) {
            fail();
        }

        final Team publicTeam = Team.create(db.getPublicGroup());
        try {
            assertEquals(publicTeam.getDateCreation(), db.getPublicGroup().getDateCreation());
        } catch (final UnauthorizedPublicReadOnlyAccessException e) {
            fail();
        }
    }

    public final void testGetInternalAccount() {
        final Member tom = Member.create(db.getTom());

        try {
            tom.authenticate(null);
            tom.getInternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            tom.authenticate(fredAuthToken);
            tom.getInternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            tom.authenticate(loser);
            tom.getInternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            tom.authenticate(tomAuthToken);
            assertEquals(tom.getInternalAccount().getId(), db.getTom().getInternalAccount().getId());
        } catch (final UnauthorizedPrivateAccessException e) {
            fail();
        }

        final Team publicTeam = Team.create(db.getPublicGroup());
        try {
            publicTeam.authenticate(null);
            publicTeam.getInternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            // Bank rights
            publicTeam.authenticate(yoAuthToken);
            publicTeam.getInternalAccount();
            assertTrue(true);
        } catch (final UnauthorizedPrivateAccessException e1) {
            fail();
        }
        try {
            // no bank right
            publicTeam.authenticate(loser);
            publicTeam.getInternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            // no bank right
            publicTeam.authenticate(fredAuthToken);
            publicTeam.getInternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            // bank right
            publicTeam.authenticate(tomAuthToken);
            assertEquals(publicTeam.getInternalAccount().getId(), db.getPublicGroup().getInternalAccount().getId());
        } catch (final UnauthorizedPrivateAccessException e) {
            fail();
        }
    }

    public final void testGetExternalAccount() {
        final Member tom = Member.create(db.getTom());

        try {
            tom.authenticate(null);
            tom.getExternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            tom.authenticate(fredAuthToken);
            tom.getExternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            tom.authenticate(loser);
            tom.getExternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            tom.authenticate(tomAuthToken);
            assertEquals(tom.getExternalAccount().getId(), db.getTom().getExternalAccount().getId());
        } catch (final UnauthorizedPrivateAccessException e) {
            fail();
        }

        final Team publicTeam = Team.create(db.getPublicGroup());
        try {
            publicTeam.authenticate(null);
            publicTeam.getExternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            // Bank rights
            publicTeam.authenticate(yoAuthToken);
            publicTeam.getExternalAccount();
            assertTrue(true);
        } catch (final UnauthorizedPrivateAccessException e1) {
            fail();
        }
        try {
            // no bank right
            publicTeam.authenticate(loser);
            publicTeam.getExternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            // no bank right
            publicTeam.authenticate(fredAuthToken);
            publicTeam.getExternalAccount();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            // bank right
            publicTeam.authenticate(tomAuthToken);
            assertEquals(publicTeam.getExternalAccount().getId(), db.getPublicGroup().getExternalAccount().getId());
        } catch (final UnauthorizedPrivateAccessException e) {
            fail();
        }
    }

    public final void testGetBankTransactions() {
        final Member tom = Member.create(db.getTom());

        try {
            tom.authenticate(null);
            tom.getBankTransactions();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            tom.authenticate(fredAuthToken);
            tom.getBankTransactions();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            tom.authenticate(loser);
            tom.getBankTransactions();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            tom.authenticate(tomAuthToken);
            assertEquals(tom.getBankTransactions().size(), db.getTom().getBankTransactions().size());
        } catch (final UnauthorizedPrivateAccessException e) {
            fail();
        }

        final Team publicTeam = Team.create(db.getPublicGroup());
        try {
            publicTeam.authenticate(null);
            publicTeam.getBankTransactions();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            // Bank rights
            publicTeam.authenticate(yoAuthToken);
            publicTeam.getBankTransactions();
            assertTrue(true);
        } catch (final UnauthorizedPrivateAccessException e1) {
            fail();
        }
        try {
            // no bank right
            publicTeam.authenticate(loser);
            publicTeam.getBankTransactions();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            // no bank right
            publicTeam.authenticate(fredAuthToken);
            publicTeam.getBankTransactions();
            fail();
        } catch (final UnauthorizedPrivateAccessException e1) {
            assertTrue(true);
        }
        try {
            // bank right
            publicTeam.authenticate(tomAuthToken);
            assertEquals(publicTeam.getBankTransactions().size(), db.getPublicGroup().getBankTransactions().size());
        } catch (final UnauthorizedPrivateAccessException e) {
            fail();
        }
    }

    public final void testGetContributions() {
        final Member tom = Member.create(db.getTom());
        try {
            assertEquals(tom.getContributions().size(), db.getTom().getContributions(true).size());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        final Team publicTeam = Team.create(db.getPublicGroup());
        try {
            assertEquals(publicTeam.getContributions().size(), db.getPublicGroup().getContributions().size());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
    }

    public final void testGetDisplayName() {
        final Member tom = Member.create(db.getTom());
        assertEquals(tom.getDisplayName(), db.getTom().getFullname());
    }

    // TODO
    // public final void testGetAvatar() {
    // fail("Not yet implemented");
    // }

    public final void testCanAccessDateCreation() {
        final Member tom = Member.create(db.getTom());
        assertTrue(tom.canAccessDateCreation());
    }

    public final void testCanGetInternalAccount() {
        final Member tom = Member.create(db.getTom());
        tom.authenticate(null);
        assertFalse(tom.canGetInternalAccount());

        tom.authenticate(fredAuthToken);
        assertFalse(tom.canGetInternalAccount());

        tom.authenticate(tomAuthToken);
        assertTrue(tom.canGetInternalAccount());

        final Team publicTeam = Team.create(db.getPublicGroup());
        publicTeam.authenticate(null);
        assertFalse(publicTeam.canGetInternalAccount());

        publicTeam.authenticate(fredAuthToken);
        assertFalse(publicTeam.canGetInternalAccount());

        publicTeam.authenticate(loser);
        assertFalse(publicTeam.canGetInternalAccount());

        publicTeam.authenticate(tomAuthToken);
        assertTrue(publicTeam.canGetInternalAccount());

        publicTeam.authenticate(yoAuthToken);
        assertTrue(publicTeam.canGetInternalAccount());
    }

    public final void testCanGetExternalAccount() {
        final Member tom = Member.create(db.getTom());
        tom.authenticate(null);
        assertFalse(tom.canGetExternalAccount());

        tom.authenticate(fredAuthToken);
        assertFalse(tom.canGetExternalAccount());

        tom.authenticate(tomAuthToken);
        assertTrue(tom.canGetExternalAccount());

        final Team publicTeam = Team.create(db.getPublicGroup());
        publicTeam.authenticate(null);
        assertFalse(publicTeam.canGetExternalAccount());

        publicTeam.authenticate(fredAuthToken);
        assertFalse(publicTeam.canGetExternalAccount());

        publicTeam.authenticate(loser);
        assertFalse(publicTeam.canGetExternalAccount());

        publicTeam.authenticate(tomAuthToken);
        assertTrue(publicTeam.canGetExternalAccount());

        publicTeam.authenticate(yoAuthToken);
        assertTrue(publicTeam.canGetExternalAccount());
    }

    public final void testCanGetBankTransactionAccount() {
        final Member tom = Member.create(db.getTom());
        tom.authenticate(null);
        assertFalse(tom.canGetBankTransactionAccount());

        tom.authenticate(fredAuthToken);
        assertFalse(tom.canGetBankTransactionAccount());

        tom.authenticate(tomAuthToken);
        assertTrue(tom.canGetBankTransactionAccount());

        final Team publicTeam = Team.create(db.getPublicGroup());
        publicTeam.authenticate(null);
        assertFalse(publicTeam.canGetBankTransactionAccount());

        publicTeam.authenticate(fredAuthToken);
        assertFalse(publicTeam.canGetBankTransactionAccount());

        publicTeam.authenticate(loser);
        assertFalse(publicTeam.canGetBankTransactionAccount());

        publicTeam.authenticate(tomAuthToken);
        assertTrue(publicTeam.canGetBankTransactionAccount());

        publicTeam.authenticate(yoAuthToken);
        assertTrue(publicTeam.canGetBankTransactionAccount());
    }

    public final void testCanGetContributions() {
        final Member tom = Member.create(db.getTom());
        tom.authenticate(null);
        assertTrue(tom.canGetContributions());

        final Team publicTeam = Team.create(db.getPublicGroup());
        publicTeam.authenticate(null);
        assertTrue(publicTeam.canGetContributions());
    }

}
