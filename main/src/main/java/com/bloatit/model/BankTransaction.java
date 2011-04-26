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

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;

import com.bloatit.data.DaoBankTransaction;
import com.bloatit.data.DaoBankTransaction.State;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoTeam;

/**
 * The Class BankTransaction.
 * 
 * @see DaoBankTransaction
 */
@Entity
public final class BankTransaction extends Identifiable<DaoBankTransaction> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The Class MyCreator.
     */
    private static final class MyCreator extends Creator<DaoBankTransaction, BankTransaction> {

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.Creator#doCreate(com.bloatit.data.DaoIdentifiable)
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public BankTransaction doCreate(final DaoBankTransaction dao) {
            return new BankTransaction(dao);
        }
    }

    /**
     * Check the cache, if a corresponding BankTransaction exist return it,
     * otherwise create a BankTransaction using its dao representation. If the
     * dao == null return null;
     * 
     * @param dao the dao
     * @return the bank transaction
     */
    @SuppressWarnings("synthetic-access")
    public static BankTransaction create(final DaoBankTransaction dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Gets the by token.
     * 
     * @param token the token
     * @return the by token
     * @see DaoBankTransaction#getByToken(String)
     */
    public static BankTransaction getByToken(final String token) {
        return create(DaoBankTransaction.getByToken(token));
    }

    /**
     * Create a new BankTransaction.
     * 
     * @param message is the message from the bank. May be a Ok message or an
     *            error message.
     * @param token is a token to authenticate this transaction. The online bank
     *            service should give it during the transaction.
     * @param author it the person implied in this transaction (the one filling
     *            is account)
     * @param value is the quantity of money transfered.
     * @param valuePayed the really paid value.
     * @param orderReference is a reference we have to create and should be
     *            unique.
     */
    public BankTransaction(final String message,
                           final String token,
                           final Actor<?> author,
                           final BigDecimal value,
                           final BigDecimal valuePayed,
                           final String orderReference) {
        super(DaoBankTransaction.createAndPersist(message, token, author.getDao(), value, valuePayed, orderReference));
    }

    /**
     * Instantiates a new bank transaction.
     * 
     * @param dao the dao
     */
    private BankTransaction(final DaoBankTransaction dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters / setters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Sets authorized.
     * 
     * @see DaoBankTransaction#setAuthorized()
     */
    public void setAuthorized() {
        getDao().setAuthorized();
    }

    /**
     * Sets refused.
     * 
     * @see DaoBankTransaction#setRefused()
     */
    public void setRefused() {
        getDao().setRefused();
    }

    /**
     * Sets the validated.
     * 
     * @return true, if successful
     * @see DaoBankTransaction#setValidated()
     */
    public boolean setValidated() {
        return getDao().setValidated();
    }

    /**
     * Sets the process informations. The process informations are every kind of
     * information you might have during the process of making a bank
     * transaction. For example you can put here specific error messages.
     * 
     * @param processInformations the new process informations
     */
    public void setProcessInformations(final String processInformations) {
        getDao().setProcessInformations(processInformations);
    }

    /**
     * Gets the message. The message is the error (or not) message sent by the
     * bank during a transaction.
     * 
     * @return the message
     */
    public String getMessage() {
        return getDao().getMessage();
    }

    /**
     * Gets the paid value.
     * 
     * @return the value
     */
    public BigDecimal getValuePaid() {
        return getDao().getValuePaid();
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public BigDecimal getValue() {
        return getDao().getValue();
    }

    /**
     * Gets the state.
     * 
     * @return the state
     */
    public State getState() {
        return getDao().getState();
    }

    /**
     * Gets the creation date.
     * 
     * @return the creation date
     */
    public Date getCreationDate() {
        return getDao().getCreationDate();
    }

    /**
     * Gets the modification date.
     * 
     * @return the modification date
     */
    public Date getModificationDate() {
        return getDao().getModificationDate();
    }

    /**
     * Gets the reference. This is the generated purchase reference.
     * 
     * @return the reference
     */
    public String getReference() {
        return getDao().getReference();
    }

    /**
     * Gets the token. The token is a unique string identifying this
     * transaction.
     * 
     * @return the token
     */
    public String getToken() {
        return getDao().getToken();
    }

    /**
     * Gets the process informations. The process informations are every kind of
     * information you might have during the process of making a bank
     * transaction. For example you can put here error messages.
     * 
     * @return the process informations
     */
    public String getProcessInformations() {
        return getDao().getProcessInformations();
    }

    public Actor<?> getAuthor() {
        if (getDao().getAuthor() instanceof DaoTeam) {
            return Team.create((DaoTeam) getDao().getAuthor());
        }
        return Member.create((DaoMember) getDao().getAuthor());
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.right.RestrictedObject#isMine(com.bloatit.model.Member)
     */
    @Override
    protected boolean isMine(final Member member) {
        return getDao().getAuthor().getLogin().equals(member.getLoginUnprotected());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
