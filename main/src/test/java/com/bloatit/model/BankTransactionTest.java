package com.bloatit.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bloatit.model.right.AuthToken;

public class BankTransactionTest extends ModelTestUnit {

    @Test
    public final void testCanGetMessage() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        AuthToken.authenticate(memeberFred);
        assertFalse(bankTransaction.canGetMessage());

        AuthToken.authenticate(memberYo);
        assertTrue(bankTransaction.canGetMessage());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        AuthToken.authenticate(memeberFred);
        assertFalse(groupBankTransaction.canGetMessage());
        
        AuthToken.authenticate(loser);
        assertFalse(groupBankTransaction.canGetMessage());
        
        AuthToken.authenticate(memberYo);
        assertTrue(groupBankTransaction.canGetMessage());
        
        AuthToken.authenticate(memberTom);
        assertTrue(groupBankTransaction.canGetMessage());
    }

    @Test
    public final void testCanGetValuePaid() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        AuthToken.authenticate(memeberFred);
        assertFalse(bankTransaction.canGetValuePaid());

        AuthToken.authenticate(memberYo);
        assertTrue(bankTransaction.canGetValuePaid());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        AuthToken.authenticate(memeberFred);
        assertFalse(groupBankTransaction.canGetValuePaid());
        
        AuthToken.authenticate(loser);
        assertFalse(groupBankTransaction.canGetValuePaid());
        
        AuthToken.authenticate(memberYo);
        assertTrue(groupBankTransaction.canGetValuePaid());
        
        AuthToken.authenticate(memberTom);
        assertTrue(groupBankTransaction.canGetValuePaid());
    }

    @Test
    public final void testCanGetValue() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        AuthToken.authenticate(memeberFred);
        assertFalse(bankTransaction.canGetValue());

        AuthToken.authenticate(memberYo);
        assertTrue(bankTransaction.canGetValue());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        AuthToken.authenticate(memeberFred);
        assertFalse(groupBankTransaction.canGetValue());
        
        AuthToken.authenticate(loser);
        assertFalse(groupBankTransaction.canGetValue());
        
        AuthToken.authenticate(memberYo);
        assertTrue(groupBankTransaction.canGetValue());
        
        AuthToken.authenticate(memberTom);
        assertTrue(groupBankTransaction.canGetValue());
    }

    @Test
    public final void testCanGetState() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        AuthToken.authenticate(memeberFred);
        assertFalse(bankTransaction.canGetState());

        AuthToken.authenticate(memberYo);
        assertTrue(bankTransaction.canGetState());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        AuthToken.authenticate(memeberFred);
        assertFalse(groupBankTransaction.canGetState());
        
        AuthToken.authenticate(loser);
        assertFalse(groupBankTransaction.canGetState());
        
        AuthToken.authenticate(memberYo);
        assertTrue(groupBankTransaction.canGetState());
        
        AuthToken.authenticate(memberTom);
        assertTrue(groupBankTransaction.canGetState());
    }

    @Test
    public final void testCanGetCreationDate() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        AuthToken.authenticate(memeberFred);
        assertFalse(bankTransaction.canGetCreationDate());

        AuthToken.authenticate(memberYo);
        assertTrue(bankTransaction.canGetCreationDate());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        AuthToken.authenticate(memeberFred);
        assertFalse(groupBankTransaction.canGetCreationDate());
        
        AuthToken.authenticate(loser);
        assertFalse(groupBankTransaction.canGetCreationDate());
        
        AuthToken.authenticate(memberYo);
        assertTrue(groupBankTransaction.canGetCreationDate());
        
        AuthToken.authenticate(memberTom);
        assertTrue(groupBankTransaction.canGetCreationDate());
    }

    @Test
    public final void testCanGetModificationDate() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        AuthToken.authenticate(memeberFred);
        assertFalse(bankTransaction.canGetModificationDate());

        AuthToken.authenticate(memberYo);
        assertTrue(bankTransaction.canGetModificationDate());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        AuthToken.authenticate(memeberFred);
        assertFalse(groupBankTransaction.canGetModificationDate());
        
        AuthToken.authenticate(loser);
        assertFalse(groupBankTransaction.canGetModificationDate());
        
        AuthToken.authenticate(memberYo);
        assertTrue(groupBankTransaction.canGetModificationDate());
        
        AuthToken.authenticate(memberTom);
        assertTrue(groupBankTransaction.canGetModificationDate());
    }

    @Test
    public final void testCanGetReference() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        AuthToken.authenticate(memeberFred);
        assertFalse(bankTransaction.canGetReference());

        AuthToken.authenticate(memberYo);
        assertTrue(bankTransaction.canGetReference());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        AuthToken.authenticate(memeberFred);
        assertFalse(groupBankTransaction.canGetReference());
        
        AuthToken.authenticate(loser);
        assertFalse(groupBankTransaction.canGetReference());
        
        AuthToken.authenticate(memberYo);
        assertTrue(groupBankTransaction.canGetReference());
        
        AuthToken.authenticate(memberTom);
        assertTrue(groupBankTransaction.canGetReference());
    }

    @Test
    public final void testCanGetAuthor() {
        final BankTransaction bankTransaction = BankTransaction.create(db.getYoBankTransaction());

        AuthToken.authenticate(memeberFred);
        assertFalse(bankTransaction.canGetAuthor());

        AuthToken.authenticate(memberYo);
        assertTrue(bankTransaction.canGetAuthor());
        
        final BankTransaction groupBankTransaction = BankTransaction.create(db.getPublicGroupBankTransaction());
        AuthToken.authenticate(memeberFred);
        assertFalse(groupBankTransaction.canGetAuthor());
        
        AuthToken.authenticate(loser);
        assertFalse(groupBankTransaction.canGetAuthor());
        
        AuthToken.authenticate(memberYo);
        assertTrue(groupBankTransaction.canGetAuthor());
        
        AuthToken.authenticate(memberTom);
        assertTrue(groupBankTransaction.canGetAuthor());
    }

}
