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

import java.util.Date;

import com.bloatit.data.DaoActor;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.BankTransactionList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.ActorRight;
import com.bloatit.model.right.AuthToken;

// TODO: Auto-generated Javadoc
/**
 * The Class Actor.
 *
 * @param <T> the Dao version of this model layer object.
 * @see DaoActor
 */
public abstract class Actor<T extends DaoActor> extends Identifiable<T> {

    /**
     * Instantiates a new actor.
     *
     * @param id the id
     */
    protected Actor(final T id) {
        super(id);
    }

    /**
     * Tells if a user can access the <code>Email</code> property. You have to
     * unlock this Actor using the {@link Actor#authenticate(AuthToken)} method.
     *
     * @param action can be read/write/delete. for example use <code>READ</code>
     *            to know if you can use {@link Member#getGroups()}.
     * @return true if you can access the parameter <code>Email</code>.
     * @see Actor#getEmail()
     * @see Actor#setEmail(String)
     * @see Actor#authenticate(AuthToken)
     */
    public final boolean canAccessEmail(final Action action) {
        return canAccess(new ActorRight.Email(), action);
    }

    /**
     * Gets the email.
     *
     * @return the email
     * @throws UnauthorizedOperationException if you don't have the
     *             <code>READ</code> right on the <code>Email</code> property
     * @see DaoActor#getContact()
     */
    public final String getEmail() throws UnauthorizedOperationException {
        tryAccess(new ActorRight.Email(), Action.READ);
        return getDao().getContact();
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     * @throws UnauthorizedOperationException if you don't have the
     *             <code>WRITE</code> right on the <code>Email</code> property
     */
    public final void setEmail(final String email) throws UnauthorizedOperationException {
        tryAccess(new ActorRight.Email(), Action.WRITE);
        getDao().setContact(email);
    }

    /**
     * Gets the login with no right protection.
     *
     * @return the login
     */
    protected final String getLoginUnprotected() {
        return getDao().getLogin();
    }

    /**
     * Can access login.
     *
     * @return true if you have the <code>READ</code> right on the Login
     *         property.
     * @see Actor#getEmail()
     * @see Actor#authenticate(AuthToken)
     */
    public final boolean canAccessLogin() {
        return canAccess(new ActorRight.Login(), Action.READ);
    }

    /**
     * Gets the login.
     *
     * @return the login
     * @throws UnauthorizedOperationException if you don't have the
     *             <code>READ</code> right on the <code>Login</code> property
     * @see DaoActor#getLogin()
     */
    public final String getLogin() throws UnauthorizedOperationException {
        tryAccess(new ActorRight.Login(), Action.READ);
        return getLoginUnprotected();
    }

    /**
     * Tells if the authenticated user can access date creation.
     *
     * @return true if you can access the DateCreation property.
     * @see Actor#getDateCreation()
     */
    public final boolean canAccessDateCreation() {
        return canAccess(new ActorRight.DateCreation(), Action.READ);
    }

    /**
     * Gets the creation date of this {@link Actor}.
     *
     * @return the creation date
     * @throws UnauthorizedOperationException if you don't have the right to
     *             access the DateCreation property.
     * @see DaoActor#getDateCreation()
     */
    public final Date getDateCreation() throws UnauthorizedOperationException {
        tryAccess(new ActorRight.DateCreation(), Action.READ);
        return getDao().getDateCreation();
    }

    /**
     * Tells if the authenticated user can get internal account.
     *
     * @return true if you can access the <code>InternalAccount</code> property.
     * @see Actor#getInternalAccount()
     * @see Actor#authenticate(AuthToken)
     */
    public final boolean canGetInternalAccount() {
        return canAccess(new ActorRight.InternalAccount(), Action.READ);
    }

    /**
     * The internal account is the account we manage internally. Users can
     * add/get money to/from it, and can use this money to contribute on
     * softwares.
     *
     * @return the internal account
     * @throws UnauthorizedOperationException the unauthorized operation
     *             exception
     * @throw UnauthorizedOperationException if you do not have the right to
     *        access the <code>InternalAccount</code> property.
     */
    public final InternalAccount getInternalAccount() throws UnauthorizedOperationException {
        tryAccess(new ActorRight.InternalAccount(), Action.READ);
        return InternalAccount.create(getDao().getInternalAccount());
    }

    /**
     * Tells if the authenticated user can get external account.
     *
     * @return true if you can access the <code>ExternalAccount</code> property.
     */
    public final boolean canGetExternalAccount() {
        return canAccess(new ActorRight.ExternalAccount(), Action.READ);
    }

    /**
     * Gets the external account.
     *
     * @return the external account
     * @throws UnauthorizedOperationException if you haven't the right to access
     *             the <code>ExtenralAccount</code> property.
     */
    public final ExternalAccount getExternalAccount() throws UnauthorizedOperationException {
        tryAccess(new ActorRight.ExternalAccount(), Action.READ);
        return ExternalAccount.create(getDao().getExternalAccount());
    }

    /**
     * Tells if the authenticated user can access bank transaction.
     *
     * @return true if you can access the <code>BankTransaction</code> property.
     * @see #getBankTransactions()
     */
    public final boolean canAccessBankTransaction() {
        return canAccess(new ActorRight.BankTransaction(), Action.READ);
    }

    /**
     * Gets the bank transactions.
     *
     * @return all the bank transactions this actor has done.
     * @throws UnauthorizedOperationException if you haven't the right to access
     *             the <code>ExtenralAccount</code> property.
     * @see DaoActor#getBankTransactions()
     */
    public final PageIterable<BankTransaction> getBankTransactions() throws UnauthorizedOperationException {
        tryAccess(new ActorRight.BankTransaction(), Action.READ);
        return new BankTransactionList(getDao().getBankTransactions());
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.right.RestrictedObject#isMine(com.bloatit.model.Member)
     */
    @Override
    protected boolean isMine(final Member member) {
        return this.equals(member);
    }
}
