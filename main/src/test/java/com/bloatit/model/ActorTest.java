package com.bloatit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPublicReadOnlyAccessException;

public class ActorTest extends ModelTestUnit {

    @Test
    public final void testGetLogin() {
        final Member tom = Member.create(db.getTom());
        assertEquals(tom.getLogin(), db.getTom().getLogin());

        final Team publicTeam = Team.create(db.getPublicGroup());
        assertEquals(publicTeam.getLogin(), db.getPublicGroup().getLogin());
    }

    @Test
    public final void testGetDateCreation() {
        final Member tom = Member.create(db.getTom());
        try {
            assertEquals(tom.getDateCreation().getTime(), db.getTom().getDateCreation().getTime());
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

    @Test
    public final void testGetInternalAccount() {
        final Member tom = Member.create(db.getTom());

        try {
            AuthToken.unAuthenticate();
            tom.getInternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            AuthToken.authenticate(memeberFred);
            tom.getInternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            AuthToken.authenticate(loser);
            tom.getInternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            AuthToken.authenticate(memberTom);
            assertEquals(tom.getInternalAccount().getId(), db.getTom().getInternalAccount().getId());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        final Team publicTeam = Team.create(db.getPublicGroup());
        try {
            AuthToken.unAuthenticate();
            publicTeam.getInternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            // Bank rights
            AuthToken.authenticate(memberYo);
            publicTeam.getInternalAccount();
            assertTrue(true);
        } catch (final UnauthorizedOperationException e1) {
            fail();
        }
        try {
            // no bank right
            AuthToken.authenticate(loser);
            publicTeam.getInternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            // no bank right
            AuthToken.authenticate(memeberFred);
            publicTeam.getInternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            // bank right
            AuthToken.authenticate(memberTom);
            assertEquals(publicTeam.getInternalAccount().getId(), db.getPublicGroup().getInternalAccount().getId());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
    }

    @Test
    public final void testGetExternalAccount() {
        final Member tom = Member.create(db.getTom());

        try {
            AuthToken.unAuthenticate();
            tom.getExternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            AuthToken.authenticate(memeberFred);
            tom.getExternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            AuthToken.authenticate(loser);
            tom.getExternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            AuthToken.authenticate(memberTom);
            assertEquals(tom.getExternalAccount().getId(), db.getTom().getExternalAccount().getId());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        final Team publicTeam = Team.create(db.getPublicGroup());
        try {
            AuthToken.unAuthenticate();
            publicTeam.getExternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            // Bank rights
            AuthToken.authenticate(memberYo);
            publicTeam.getExternalAccount();
            assertTrue(true);
        } catch (final UnauthorizedOperationException e1) {
            fail();
        }
        try {
            // no bank right
            AuthToken.authenticate(loser);
            publicTeam.getExternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            // no bank right
            AuthToken.authenticate(memeberFred);
            publicTeam.getExternalAccount();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            // bank right
            AuthToken.authenticate(memberTom);
            assertEquals(publicTeam.getExternalAccount().getId(), db.getPublicGroup().getExternalAccount().getId());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
    }

    @Test
    public final void testGetBankTransactions() {
        final Member tom = Member.create(db.getTom());

        try {
            AuthToken.unAuthenticate();
            tom.getBankTransactions();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            AuthToken.authenticate(memeberFred);
            tom.getBankTransactions();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            AuthToken.authenticate(loser);
            tom.getBankTransactions();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            AuthToken.authenticate(memberTom);
            assertEquals(tom.getBankTransactions().size(), db.getTom().getBankTransactions().size());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        final Team publicTeam = Team.create(db.getPublicGroup());
        try {
            AuthToken.unAuthenticate();
            publicTeam.getBankTransactions();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            // Bank rights
            AuthToken.authenticate(memberYo);
            publicTeam.getBankTransactions();
            assertTrue(true);
        } catch (final UnauthorizedOperationException e1) {
            fail();
        }
        try {
            // no bank right
            AuthToken.authenticate(loser);
            publicTeam.getBankTransactions();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            // no bank right
            AuthToken.authenticate(memeberFred);
            publicTeam.getBankTransactions();
            fail();
        } catch (final UnauthorizedOperationException e1) {
            assertTrue(true);
        }
        try {
            // bank right
            AuthToken.authenticate(memberTom);
            assertEquals(publicTeam.getBankTransactions().size(), db.getPublicGroup().getBankTransactions().size());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
    }

    @Test
    public final void testGetContributions() {
        final Member tom = Member.create(db.getTom());
        assertEquals(tom.getContributions().size(), db.getTom().getContributions(true).size());

        final Team publicTeam = Team.create(db.getPublicGroup());
        assertEquals(publicTeam.getContributions().size(), db.getPublicGroup().getContributions().size());
    }

    @Test
    public final void testGetDisplayName() {
        final Member tom = Member.create(db.getTom());
        assertEquals(tom.getDisplayName(), db.getTom().getFullname());
    }

    @Test
    public final void testCanAccessDateCreation() {
        final Member tom = Member.create(db.getTom());
        assertTrue(tom.canAccessDateCreation());
    }

    @Test
    public final void testCanGetInternalAccount() {
        final Member tom = Member.create(db.getTom());
        AuthToken.unAuthenticate();
        assertFalse(tom.canGetInternalAccount());

        AuthToken.authenticate(memeberFred);
        assertFalse(tom.canGetInternalAccount());

        AuthToken.authenticate(memberTom);
        assertTrue(tom.canGetInternalAccount());

        final Team publicTeam = Team.create(db.getPublicGroup());
        AuthToken.unAuthenticate();
        assertFalse(publicTeam.canGetInternalAccount());

        AuthToken.authenticate(memeberFred);
        assertFalse(publicTeam.canGetInternalAccount());

        AuthToken.authenticate(loser);
        assertFalse(publicTeam.canGetInternalAccount());

        AuthToken.authenticate(memberTom);
        assertTrue(publicTeam.canGetInternalAccount());

        AuthToken.authenticate(memberYo);
        assertTrue(publicTeam.canGetInternalAccount());
    }

    @Test
    public final void testCanGetExternalAccount() {
        final Member tom = Member.create(db.getTom());
        AuthToken.unAuthenticate();
        assertFalse(tom.canGetExternalAccount());

        AuthToken.authenticate(memeberFred);
        assertFalse(tom.canGetExternalAccount());

        AuthToken.authenticate(memberTom);
        assertTrue(tom.canGetExternalAccount());

        final Team publicTeam = Team.create(db.getPublicGroup());
        AuthToken.unAuthenticate();
        assertFalse(publicTeam.canGetExternalAccount());

        AuthToken.authenticate(memeberFred);
        assertFalse(publicTeam.canGetExternalAccount());

        AuthToken.authenticate(loser);
        assertFalse(publicTeam.canGetExternalAccount());

        AuthToken.authenticate(memberTom);
        assertTrue(publicTeam.canGetExternalAccount());

        AuthToken.authenticate(memberYo);
        assertTrue(publicTeam.canGetExternalAccount());
    }

    @Test
    public final void testCanGetBankTransactionAccount() {
        final Member tom = Member.create(db.getTom());
        AuthToken.unAuthenticate();
        assertFalse(tom.canGetBankTransactionAccount());

        AuthToken.authenticate(memeberFred);
        assertFalse(tom.canGetBankTransactionAccount());

        AuthToken.authenticate(memberTom);
        assertTrue(tom.canGetBankTransactionAccount());

        final Team publicTeam = Team.create(db.getPublicGroup());
        AuthToken.unAuthenticate();
        assertFalse(publicTeam.canGetBankTransactionAccount());

        AuthToken.authenticate(memeberFred);
        assertFalse(publicTeam.canGetBankTransactionAccount());

        AuthToken.authenticate(loser);
        assertFalse(publicTeam.canGetBankTransactionAccount());

        AuthToken.authenticate(memberTom);
        assertTrue(publicTeam.canGetBankTransactionAccount());

        AuthToken.authenticate(memberYo);
        assertTrue(publicTeam.canGetBankTransactionAccount());
    }

    @Test
    public final void testCanGetContributions() {
        final Member tom = Member.create(db.getTom());
        AuthToken.unAuthenticate();
        assertTrue(tom.canGetContributions());

        final Team publicTeam = Team.create(db.getPublicGroup());
        AuthToken.unAuthenticate();
        assertTrue(publicTeam.canGetContributions());
    }

}
