package com.bloatit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;

public class AccountTest extends ModelTestUnit {

    @Test
    public void testCanAccessTransaction() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(tomAccount.canAccessTransaction());

        AuthToken.authenticate(memberTom);
        assertTrue(tomAccount.canAccessTransaction());

        AuthToken.authenticate(loser);
        assertFalse(tomAccount.canAccessTransaction());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(publicGroupAccount.canAccessTransaction());

        // Has the bank rights
        AuthToken.authenticate(memberTom);
        assertTrue(publicGroupAccount.canAccessTransaction());

        // Has the bank rights
        AuthToken.authenticate(memberYo);
        assertTrue(publicGroupAccount.canAccessTransaction());

        // Does not have the bank rights
        AuthToken.authenticate(memeberFred);
        assertFalse(publicGroupAccount.canAccessTransaction());

        // Does not have the bank rights
        AuthToken.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessTransaction());
    }

    @Test
    public void testCanAccessAmount() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(tomAccount.canAccessAmount());

        AuthToken.authenticate(memberTom);
        assertTrue(tomAccount.canAccessAmount());

        AuthToken.authenticate(loser);
        assertFalse(tomAccount.canAccessAmount());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(publicGroupAccount.canAccessAmount());

        // Has the bank rights
        AuthToken.authenticate(memberTom);
        assertTrue(publicGroupAccount.canAccessAmount());

        // Has the bank rights
        AuthToken.authenticate(memberYo);
        assertTrue(publicGroupAccount.canAccessAmount());

        // Does not have the bank rights
        AuthToken.authenticate(memeberFred);
        assertFalse(publicGroupAccount.canAccessAmount());

        // Does not have the bank rights
        AuthToken.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessAmount());
    }

    @Test
    public void testCanAccessActor() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(tomAccount.canAccessActor());

        AuthToken.authenticate(memberTom);
        assertTrue(tomAccount.canAccessActor());

        AuthToken.authenticate(loser);
        assertFalse(tomAccount.canAccessActor());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(publicGroupAccount.canAccessActor());

        // Has the bank rights
        AuthToken.authenticate(memberTom);
        assertTrue(publicGroupAccount.canAccessActor());

        // Has the bank rights
        AuthToken.authenticate(memberYo);
        assertTrue(publicGroupAccount.canAccessActor());

        // Does not have the bank rights
        AuthToken.authenticate(memeberFred);
        assertFalse(publicGroupAccount.canAccessActor());

        // Does not have the bank rights
        AuthToken.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessActor());
    }

    @Test
    public void testCanAccessCreationDate() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(tomAccount.canAccessCreationDate());

        AuthToken.authenticate(memberTom);
        assertTrue(tomAccount.canAccessCreationDate());

        AuthToken.authenticate(loser);
        assertFalse(tomAccount.canAccessCreationDate());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(publicGroupAccount.canAccessCreationDate());

        // Has the bank rights
        AuthToken.authenticate(memberTom);
        assertTrue(publicGroupAccount.canAccessCreationDate());

        // Has the bank rights
        AuthToken.authenticate(memberYo);
        assertTrue(publicGroupAccount.canAccessCreationDate());

        // Does not have the bank rights
        AuthToken.authenticate(memeberFred);
        assertFalse(publicGroupAccount.canAccessCreationDate());

        // Does not have the bank rights
        AuthToken.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessCreationDate());
    }

    @Test
    public void testCanAccessLastModificationDate() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(tomAccount.canAccessLastModificationDate());

        AuthToken.authenticate(memberTom);
        assertTrue(tomAccount.canAccessLastModificationDate());

        AuthToken.authenticate(loser);
        assertFalse(tomAccount.canAccessLastModificationDate());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        AuthToken.unAuthenticate();
        assertFalse(publicGroupAccount.canAccessLastModificationDate());

        // Has the bank rights
        AuthToken.authenticate(memberTom);
        assertTrue(publicGroupAccount.canAccessLastModificationDate());

        // Has the bank rights
        AuthToken.authenticate(memberYo);
        assertTrue(publicGroupAccount.canAccessLastModificationDate());

        // Does not have the bank rights
        AuthToken.authenticate(memeberFred);
        assertFalse(publicGroupAccount.canAccessLastModificationDate());

        // Does not have the bank rights
        AuthToken.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessLastModificationDate());
    }

    @Test
    public void testGetLastModificationDate() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            AuthToken.unAuthenticate();
            tomAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            AuthToken.authenticate(memberTom);
            tomAccount.getLastModificationDate();
            assertEquals(tomAccount.getLastModificationDate(), db.getTom().getInternalAccount().getLastModificationDate());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            AuthToken.authenticate(loser);
            tomAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            AuthToken.unAuthenticate();
            publicGroupAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            AuthToken.authenticate(memberTom);
            publicGroupAccount.getLastModificationDate();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            AuthToken.authenticate(memberYo);
            publicGroupAccount.getLastModificationDate();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(memeberFred);
            publicGroupAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(loser);
            publicGroupAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetAmount() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            AuthToken.unAuthenticate();
            tomAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            AuthToken.authenticate(memberTom);
            tomAccount.getAmount();
            assertEquals(tomAccount.getAmount(), db.getTom().getInternalAccount().getAmount());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            AuthToken.authenticate(loser);
            tomAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            AuthToken.unAuthenticate();
            publicGroupAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            AuthToken.authenticate(memberTom);
            publicGroupAccount.getAmount();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            AuthToken.authenticate(memberYo);
            publicGroupAccount.getAmount();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(memeberFred);
            publicGroupAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(loser);
            publicGroupAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetTransactions() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            AuthToken.unAuthenticate();
            tomAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            AuthToken.authenticate(memberTom);
            tomAccount.getTransactions();
            // It is easier to just test the size ...
            assertEquals(tomAccount.getTransactions().size(), db.getTom().getInternalAccount().getTransactions().size());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            AuthToken.authenticate(loser);
            tomAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            AuthToken.unAuthenticate();
            publicGroupAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            AuthToken.authenticate(memberTom);
            publicGroupAccount.getTransactions();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            AuthToken.authenticate(memberYo);
            publicGroupAccount.getTransactions();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(memeberFred);
            publicGroupAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(loser);
            publicGroupAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetActor() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            AuthToken.unAuthenticate();
            tomAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            AuthToken.authenticate(memberTom);
            tomAccount.getActor();
            // It is easier to just test the ids.
            assertEquals(tomAccount.getActor().getId(), db.getTom().getInternalAccount().getActor().getId());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            AuthToken.authenticate(loser);
            tomAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            AuthToken.unAuthenticate();
            publicGroupAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            AuthToken.authenticate(memberTom);
            publicGroupAccount.getActor();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            AuthToken.authenticate(memberYo);
            publicGroupAccount.getActor();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(memeberFred);
            publicGroupAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(loser);
            publicGroupAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetCreationDate() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            AuthToken.unAuthenticate();
            tomAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            AuthToken.authenticate(memberTom);
            tomAccount.getCreationDate();
            assertEquals(tomAccount.getCreationDate(), db.getTom().getInternalAccount().getCreationDate());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            AuthToken.authenticate(loser);
            tomAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            AuthToken.unAuthenticate();
            publicGroupAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            AuthToken.authenticate(memberTom);
            publicGroupAccount.getCreationDate();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            AuthToken.authenticate(memberYo);
            publicGroupAccount.getCreationDate();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(memeberFred);
            publicGroupAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            AuthToken.authenticate(loser);
            publicGroupAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

}
