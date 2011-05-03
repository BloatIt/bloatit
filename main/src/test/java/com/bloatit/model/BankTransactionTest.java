package com.bloatit.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BankTransactionTest extends ModelTestUnit {

    @Test
    public final void testCanGetMessage() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        bankTransaction.authenticate(fredAuthToken);
        assertFalse(bankTransaction.canGetMessage());

        bankTransaction.authenticate(yoAuthToken);
        assertTrue(bankTransaction.canGetMessage());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        groupBankTransaction.authenticate(fredAuthToken);
        assertFalse(groupBankTransaction.canGetMessage());
        
        groupBankTransaction.authenticate(loser);
        assertFalse(groupBankTransaction.canGetMessage());
        
        groupBankTransaction.authenticate(yoAuthToken);
        assertTrue(groupBankTransaction.canGetMessage());
        
        groupBankTransaction.authenticate(tomAuthToken);
        assertTrue(groupBankTransaction.canGetMessage());
    }

    @Test
    public final void testCanGetValuePaid() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        bankTransaction.authenticate(fredAuthToken);
        assertFalse(bankTransaction.canGetValuePaid());

        bankTransaction.authenticate(yoAuthToken);
        assertTrue(bankTransaction.canGetValuePaid());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        groupBankTransaction.authenticate(fredAuthToken);
        assertFalse(groupBankTransaction.canGetValuePaid());
        
        groupBankTransaction.authenticate(loser);
        assertFalse(groupBankTransaction.canGetValuePaid());
        
        groupBankTransaction.authenticate(yoAuthToken);
        assertTrue(groupBankTransaction.canGetValuePaid());
        
        groupBankTransaction.authenticate(tomAuthToken);
        assertTrue(groupBankTransaction.canGetValuePaid());
    }

    @Test
    public final void testCanGetValue() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        bankTransaction.authenticate(fredAuthToken);
        assertFalse(bankTransaction.canGetValue());

        bankTransaction.authenticate(yoAuthToken);
        assertTrue(bankTransaction.canGetValue());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        groupBankTransaction.authenticate(fredAuthToken);
        assertFalse(groupBankTransaction.canGetValue());
        
        groupBankTransaction.authenticate(loser);
        assertFalse(groupBankTransaction.canGetValue());
        
        groupBankTransaction.authenticate(yoAuthToken);
        assertTrue(groupBankTransaction.canGetValue());
        
        groupBankTransaction.authenticate(tomAuthToken);
        assertTrue(groupBankTransaction.canGetValue());
    }

    @Test
    public final void testCanGetState() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        bankTransaction.authenticate(fredAuthToken);
        assertFalse(bankTransaction.canGetState());

        bankTransaction.authenticate(yoAuthToken);
        assertTrue(bankTransaction.canGetState());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        groupBankTransaction.authenticate(fredAuthToken);
        assertFalse(groupBankTransaction.canGetState());
        
        groupBankTransaction.authenticate(loser);
        assertFalse(groupBankTransaction.canGetState());
        
        groupBankTransaction.authenticate(yoAuthToken);
        assertTrue(groupBankTransaction.canGetState());
        
        groupBankTransaction.authenticate(tomAuthToken);
        assertTrue(groupBankTransaction.canGetState());
    }

    @Test
    public final void testCanGetCreationDate() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        bankTransaction.authenticate(fredAuthToken);
        assertFalse(bankTransaction.canGetCreationDate());

        bankTransaction.authenticate(yoAuthToken);
        assertTrue(bankTransaction.canGetCreationDate());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        groupBankTransaction.authenticate(fredAuthToken);
        assertFalse(groupBankTransaction.canGetCreationDate());
        
        groupBankTransaction.authenticate(loser);
        assertFalse(groupBankTransaction.canGetCreationDate());
        
        groupBankTransaction.authenticate(yoAuthToken);
        assertTrue(groupBankTransaction.canGetCreationDate());
        
        groupBankTransaction.authenticate(tomAuthToken);
        assertTrue(groupBankTransaction.canGetCreationDate());
    }

    @Test
    public final void testCanGetModificationDate() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        bankTransaction.authenticate(fredAuthToken);
        assertFalse(bankTransaction.canGetModificationDate());

        bankTransaction.authenticate(yoAuthToken);
        assertTrue(bankTransaction.canGetModificationDate());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        groupBankTransaction.authenticate(fredAuthToken);
        assertFalse(groupBankTransaction.canGetModificationDate());
        
        groupBankTransaction.authenticate(loser);
        assertFalse(groupBankTransaction.canGetModificationDate());
        
        groupBankTransaction.authenticate(yoAuthToken);
        assertTrue(groupBankTransaction.canGetModificationDate());
        
        groupBankTransaction.authenticate(tomAuthToken);
        assertTrue(groupBankTransaction.canGetModificationDate());
    }

    @Test
    public final void testCanGetReference() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        bankTransaction.authenticate(fredAuthToken);
        assertFalse(bankTransaction.canGetReference());

        bankTransaction.authenticate(yoAuthToken);
        assertTrue(bankTransaction.canGetReference());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        groupBankTransaction.authenticate(fredAuthToken);
        assertFalse(groupBankTransaction.canGetReference());
        
        groupBankTransaction.authenticate(loser);
        assertFalse(groupBankTransaction.canGetReference());
        
        groupBankTransaction.authenticate(yoAuthToken);
        assertTrue(groupBankTransaction.canGetReference());
        
        groupBankTransaction.authenticate(tomAuthToken);
        assertTrue(groupBankTransaction.canGetReference());
    }

    @Test
    public final void testCanGetAuthor() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        bankTransaction.authenticate(fredAuthToken);
        assertFalse(bankTransaction.canGetAuthor());

        bankTransaction.authenticate(yoAuthToken);
        assertTrue(bankTransaction.canGetAuthor());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        groupBankTransaction.authenticate(fredAuthToken);
        assertFalse(groupBankTransaction.canGetAuthor());
        
        groupBankTransaction.authenticate(loser);
        assertFalse(groupBankTransaction.canGetAuthor());
        
        groupBankTransaction.authenticate(yoAuthToken);
        assertTrue(groupBankTransaction.canGetAuthor());
        
        groupBankTransaction.authenticate(tomAuthToken);
        assertTrue(groupBankTransaction.canGetAuthor());
    }

}
