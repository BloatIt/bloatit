package com.bloatit.model;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;

public class AccountTest extends ModelTestUnit {

    public void testCanAccessTransaction() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        tomAccount.authenticate(null);
        assertFalse(tomAccount.canAccessTransaction());

        tomAccount.authenticate(tomAuthToken);
        assertTrue(tomAccount.canAccessTransaction());

        tomAccount.authenticate(loser);
        assertFalse(tomAccount.canAccessTransaction());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        publicGroupAccount.authenticate(null);
        assertFalse(publicGroupAccount.canAccessTransaction());

        // Has the bank rights
        publicGroupAccount.authenticate(tomAuthToken);
        assertTrue(publicGroupAccount.canAccessTransaction());

        // Has the bank rights
        publicGroupAccount.authenticate(yoAuthToken);
        assertTrue(publicGroupAccount.canAccessTransaction());

        // Does not have the bank rights
        publicGroupAccount.authenticate(fredAuthToken);
        assertFalse(publicGroupAccount.canAccessTransaction());

        // Does not have the bank rights
        publicGroupAccount.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessTransaction());
    }

    public void testCanAccessAmount() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        tomAccount.authenticate(null);
        assertFalse(tomAccount.canAccessAmount());

        tomAccount.authenticate(tomAuthToken);
        assertTrue(tomAccount.canAccessAmount());

        tomAccount.authenticate(loser);
        assertFalse(tomAccount.canAccessAmount());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        publicGroupAccount.authenticate(null);
        assertFalse(publicGroupAccount.canAccessAmount());

        // Has the bank rights
        publicGroupAccount.authenticate(tomAuthToken);
        assertTrue(publicGroupAccount.canAccessAmount());

        // Has the bank rights
        publicGroupAccount.authenticate(yoAuthToken);
        assertTrue(publicGroupAccount.canAccessAmount());

        // Does not have the bank rights
        publicGroupAccount.authenticate(fredAuthToken);
        assertFalse(publicGroupAccount.canAccessAmount());

        // Does not have the bank rights
        publicGroupAccount.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessAmount());
    }

    public void testCanAccessActor() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        tomAccount.authenticate(null);
        assertFalse(tomAccount.canAccessActor());

        tomAccount.authenticate(tomAuthToken);
        assertTrue(tomAccount.canAccessActor());

        tomAccount.authenticate(loser);
        assertFalse(tomAccount.canAccessActor());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        publicGroupAccount.authenticate(null);
        assertFalse(publicGroupAccount.canAccessActor());

        // Has the bank rights
        publicGroupAccount.authenticate(tomAuthToken);
        assertTrue(publicGroupAccount.canAccessActor());

        // Has the bank rights
        publicGroupAccount.authenticate(yoAuthToken);
        assertTrue(publicGroupAccount.canAccessActor());

        // Does not have the bank rights
        publicGroupAccount.authenticate(fredAuthToken);
        assertFalse(publicGroupAccount.canAccessActor());

        // Does not have the bank rights
        publicGroupAccount.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessActor());
    }

    public void testCanAccessCreationDate() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        tomAccount.authenticate(null);
        assertFalse(tomAccount.canAccessCreationDate());

        tomAccount.authenticate(tomAuthToken);
        assertTrue(tomAccount.canAccessCreationDate());

        tomAccount.authenticate(loser);
        assertFalse(tomAccount.canAccessCreationDate());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        publicGroupAccount.authenticate(null);
        assertFalse(publicGroupAccount.canAccessCreationDate());

        // Has the bank rights
        publicGroupAccount.authenticate(tomAuthToken);
        assertTrue(publicGroupAccount.canAccessCreationDate());

        // Has the bank rights
        publicGroupAccount.authenticate(yoAuthToken);
        assertTrue(publicGroupAccount.canAccessCreationDate());

        // Does not have the bank rights
        publicGroupAccount.authenticate(fredAuthToken);
        assertFalse(publicGroupAccount.canAccessCreationDate());

        // Does not have the bank rights
        publicGroupAccount.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessCreationDate());
    }

    public void testCanAccessLastModificationDate() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());
        tomAccount.authenticate(null);
        assertFalse(tomAccount.canAccessLastModificationDate());

        tomAccount.authenticate(tomAuthToken);
        assertTrue(tomAccount.canAccessLastModificationDate());

        tomAccount.authenticate(loser);
        assertFalse(tomAccount.canAccessLastModificationDate());

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        publicGroupAccount.authenticate(null);
        assertFalse(publicGroupAccount.canAccessLastModificationDate());

        // Has the bank rights
        publicGroupAccount.authenticate(tomAuthToken);
        assertTrue(publicGroupAccount.canAccessLastModificationDate());

        // Has the bank rights
        publicGroupAccount.authenticate(yoAuthToken);
        assertTrue(publicGroupAccount.canAccessLastModificationDate());

        // Does not have the bank rights
        publicGroupAccount.authenticate(fredAuthToken);
        assertFalse(publicGroupAccount.canAccessLastModificationDate());

        // Does not have the bank rights
        publicGroupAccount.authenticate(loser);
        assertFalse(publicGroupAccount.canAccessLastModificationDate());
    }

    public void testGetLastModificationDate() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            tomAccount.authenticate(null);
            tomAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            tomAccount.authenticate(tomAuthToken);
            tomAccount.getLastModificationDate();
            assertEquals(tomAccount.getLastModificationDate(), db.getTom().getInternalAccount().getLastModificationDate());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            tomAccount.authenticate(loser);
            tomAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            publicGroupAccount.authenticate(null);
            publicGroupAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            publicGroupAccount.authenticate(tomAuthToken);
            publicGroupAccount.getLastModificationDate();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            publicGroupAccount.authenticate(yoAuthToken);
            publicGroupAccount.getLastModificationDate();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(fredAuthToken);
            publicGroupAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(loser);
            publicGroupAccount.getLastModificationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

    public void testGetAmount() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            tomAccount.authenticate(null);
            tomAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            tomAccount.authenticate(tomAuthToken);
            tomAccount.getAmount();
            assertEquals(tomAccount.getAmount(), db.getTom().getInternalAccount().getAmount());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            tomAccount.authenticate(loser);
            tomAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            publicGroupAccount.authenticate(null);
            publicGroupAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            publicGroupAccount.authenticate(tomAuthToken);
            publicGroupAccount.getAmount();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            publicGroupAccount.authenticate(yoAuthToken);
            publicGroupAccount.getAmount();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(fredAuthToken);
            publicGroupAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(loser);
            publicGroupAccount.getAmount();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

    public void testGetTransactions() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            tomAccount.authenticate(null);
            tomAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            tomAccount.authenticate(tomAuthToken);
            tomAccount.getTransactions();
            // It is easier to just test the size ...
            assertEquals(tomAccount.getTransactions().size(), db.getTom().getInternalAccount().getTransactions().size());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            tomAccount.authenticate(loser);
            tomAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            publicGroupAccount.authenticate(null);
            publicGroupAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            publicGroupAccount.authenticate(tomAuthToken);
            publicGroupAccount.getTransactions();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            publicGroupAccount.authenticate(yoAuthToken);
            publicGroupAccount.getTransactions();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(fredAuthToken);
            publicGroupAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(loser);
            publicGroupAccount.getTransactions();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

    public void testGetActor() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            tomAccount.authenticate(null);
            tomAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            tomAccount.authenticate(tomAuthToken);
            tomAccount.getActor();
            // It is easier to just test the ids.
            assertEquals(tomAccount.getActor().getId(), db.getTom().getInternalAccount().getActor().getId());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            tomAccount.authenticate(loser);
            tomAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            publicGroupAccount.authenticate(null);
            publicGroupAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            publicGroupAccount.authenticate(tomAuthToken);
            publicGroupAccount.getActor();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            publicGroupAccount.authenticate(yoAuthToken);
            publicGroupAccount.getActor();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(fredAuthToken);
            publicGroupAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(loser);
            publicGroupAccount.getActor();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

    public void testGetCreationDate() {
        // A user account
        final InternalAccount tomAccount = InternalAccount.create(db.getTom().getInternalAccount());

        try {
            tomAccount.authenticate(null);
            tomAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            tomAccount.authenticate(tomAuthToken);
            tomAccount.getCreationDate();
            assertEquals(tomAccount.getCreationDate(), db.getTom().getInternalAccount().getCreationDate());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        try {
            tomAccount.authenticate(loser);
            tomAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // A Team account
        final InternalAccount publicGroupAccount = InternalAccount.create(db.getPublicGroup().getInternalAccount());
        try {
            publicGroupAccount.authenticate(null);
            publicGroupAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            // Has the bank rights
            publicGroupAccount.authenticate(tomAuthToken);
            publicGroupAccount.getCreationDate();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Has the bank rights
        try {
            publicGroupAccount.authenticate(yoAuthToken);
            publicGroupAccount.getCreationDate();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(fredAuthToken);
            publicGroupAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        // Does not have the bank rights
        try {
            publicGroupAccount.authenticate(loser);
            publicGroupAccount.getCreationDate();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }
    }

}
