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
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateAccessException;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtBankTransaction;

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
    protected void setAuthorized() {
        getDao().setAuthorized();
    }

    /**
     * Sets refused.
     * 
     * @see DaoBankTransaction#setRefused()
     */
    protected void setRefused() {
        getDao().setRefused();
    }

    /**
     * Sets the validated.
     * 
     * @return true, if successful
     * @see DaoBankTransaction#setValidated()
     */
    protected boolean setValidated() {
        return getDao().setValidated();
    }

    /**
     * Sets the process informations. The process informations are every kind of
     * information you might have during the process of making a bank
     * transaction. For example you can put here specific error messages.
     * 
     * @param processInformations the new process informations
     */
    protected void setProcessInformations(final String processInformations) {
        getDao().setProcessInformations(processInformations);
    }

    /**
     * Gets the process informations. The process informations are every kind of
     * information you might have during the process of making a bank
     * transaction. For example you can put here error messages.
     * 
     * @return the process informations
     */
    protected String getProcessInformations() {
        return getDao().getProcessInformations();
    }

    /**
     * Gets the message. The message is the error (or not) message sent by the
     * bank during a transaction.
     * 
     * @return the message
     * @throws UnauthorizedPrivateAccessException
     */
    public String getMessage() throws UnauthorizedPrivateAccessException {
        tryAccess(new RgtBankTransaction.Message(), Action.READ);
        return getDao().getMessage();
    }

    /**
     * Gets the paid value.
     * 
     * @return the value
     * @throws UnauthorizedPrivateAccessException
     */
    public BigDecimal getValuePaid() throws UnauthorizedPrivateAccessException {
        tryAccess(new RgtBankTransaction.ValuePaid(), Action.READ);
        return getDao().getValuePaid();
    }

    /**
     * Gets the value.
     * 
     * @return the value
     * @throws UnauthorizedPrivateAccessException
     */
    public BigDecimal getValue() throws UnauthorizedPrivateAccessException {
        tryAccess(new RgtBankTransaction.Value(), Action.READ);
        return getDao().getValue();
    }

    /**
     * Gets the state.
     * 
     * @return the state
     * @throws UnauthorizedPrivateAccessException
     */
    public State getState() throws UnauthorizedPrivateAccessException {
        tryAccess(new RgtBankTransaction.State(), Action.READ);
        return getDao().getState();
    }

    /**
     * Gets the creation date.
     * 
     * @return the creation date
     * @throws UnauthorizedOperationException
     */
    public Date getCreationDate() throws UnauthorizedOperationException {
        tryAccess(new RgtBankTransaction.CreationDate(), Action.READ);
        return getDao().getCreationDate();
    }

    /**
     * Gets the modification date.
     * 
     * @return the modification date
     * @throws UnauthorizedOperationException
     */
    public Date getModificationDate() throws UnauthorizedOperationException {
        tryAccess(new RgtBankTransaction.ModificationDate(), Action.READ);
        return getDao().getModificationDate();
    }

    /**
     * Gets the reference. This is the generated purchase reference.
     * 
     * @return the reference
     * @throws UnauthorizedPrivateAccessException
     */
    public String getReference() throws UnauthorizedPrivateAccessException {
        tryAccess(new RgtBankTransaction.Reference(), Action.READ);
        return getDao().getReference();
    }

    public Actor<?> getAuthor() throws UnauthorizedPrivateAccessException {
        tryAccess(new RgtBankTransaction.Author(), Action.READ);
        if (getDao().getAuthor() instanceof DaoTeam) {
            return Team.create((DaoTeam) getDao().getAuthor());
        }
        return Member.create((DaoMember) getDao().getAuthor());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Can ...
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Tells if the authenticated user can get the Message property.
     * 
     * @return true if you can get the Message property.
     */
    public final boolean canGetMessage() {
        return canAccess(new RgtBankTransaction.Message(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get the ValuePaid property.
     * 
     * @return true if you can get the ValuePaid property.
     */
    public final boolean canGetValuePaid() {
        return canAccess(new RgtBankTransaction.ValuePaid(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get the Value property.
     * 
     * @return true if you can get the <code>Value</code> property.
     */
    public final boolean canGetValue() {
        return canAccess(new RgtBankTransaction.Value(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get the State property.
     * 
     * @return true if you can get the State property.
     */
    public final boolean canGetState() {
        return canAccess(new RgtBankTransaction.State(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get the CreationDate property.
     * 
     * @return true if you can get the CreationDate property.
     */
    public final boolean canGetCreationDate() {
        return canAccess(new RgtBankTransaction.CreationDate(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get the ModificationDate property.
     * 
     * @return true if you can get the ModificationDate property.
     */
    public final boolean canGetModificationDate() {
        return canAccess(new RgtBankTransaction.ModificationDate(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get the Reference property.
     * 
     * @return true if you can get the Reference property.
     */
    public final boolean canGetReference() {
        return canAccess(new RgtBankTransaction.Reference(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get the Author property.
     * 
     * @return true if you can get the Author property.
     */
    public final boolean canGetAuthor() {
        return canAccess(new RgtBankTransaction.Author(), Action.READ);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
