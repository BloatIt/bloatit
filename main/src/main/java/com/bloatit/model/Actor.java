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
import com.bloatit.model.right.ActorRight;
import com.bloatit.model.right.RightManager.Action;

/**
 * @see DaoActor
 */
public abstract class Actor<T extends DaoActor> extends Identifiable<T> {

    protected Actor(final T id) {
        super(id);
    }

    /**
     * Tells if a user can access the <code>Email</code> property. You have to unlock this
     * Actor using the {@link Actor#authenticate(AuthToken)} method.
     * 
     * @param action can be read/write/delete. for example use <code>READ</code> to know
     * if you can use {@link Member#getGroups()}.
     * @return true if you can access the parameter <code>Email</code>.
     * @see Actor#getEmail()
     * @see Actor#setEmail()
     * @see Actor#authenticate(AuthToken)
     */
    public final boolean canAccessEmail(final Action action) {
        return new ActorRight.Email().canAccess(calculateRole(getLoginUnprotected()), action);
    }

    /**
     * @see DaoActor#getContact()
     * @throws UnauthorizedOperationException if you don't have the <code>READ</code>
     * right on the <code>Email</code> property
     */
    public final String getEmail() throws UnauthorizedOperationException {
        new ActorRight.Email().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return getDao().getContact();
    }

    /**
     * @see DaoActor#setEmail()
     * @throws UnauthorizedOperationException if you don't have the <code>WRITE</code>
     * right on the <code>Email</code> property
     */
    public final void setEmail(final String email) throws UnauthorizedOperationException {
        new ActorRight.Email().tryAccess(calculateRole(getLoginUnprotected()), Action.WRITE);
        getDao().setContact(email);
    }

    protected final String getLoginUnprotected() {
        return getDao().getLogin();
    }

    /**
     * @return true if you have the <code>READ</code> right on the Login property.
     * @see Actor#getEmail()
     * @see Actor#authenticate(AuthToken)
     */
    public final boolean canAccessLogin() {
        return new ActorRight.Login().canAccess(calculateRole(getLoginUnprotected()), Action.READ);
    }

    /**
     * @see DaoActor#getLogin()
     * @throws UnauthorizedOperationException if you don't have the <code>READ</code>
     * right on the <code>Login</code> property
     */
    public final String getLogin() throws UnauthorizedOperationException {
        new ActorRight.Login().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return getLoginUnprotected();
    }

    /**
     * @return true if you can access the DateCreation property.
     * @see Actor#getDateCreation()
     */
    public final boolean canAccessDateCreation() {
        return new ActorRight.DateCreation().canAccess(calculateRole(getLoginUnprotected()), Action.READ);
    }

    /**
     * @see DaoActor#getDateCreation()
     * @throws UnauthorizedOperationException if you don't have the right to access the
     * DateCreation property.
     */
    public final Date getDateCreation() throws UnauthorizedOperationException {
        new ActorRight.DateCreation().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return getDao().getDateCreation();
    }

    /**
     * @return true if you can access the <code>InternalAccount</code> property.
     * @see Actor#getInternalAccount()
     * @see Actor#authenticate(AuthToken)
     */
    public final boolean canGetInternalAccount() {
        return new ActorRight.InternalAccount().canAccess(calculateRole(getLoginUnprotected()), Action.READ);
    }

    /**
     * The internal account is the account we manage internally. Users can add/get money
     * to/from it, and can use this money to contribute on projects.
     * 
     * @throw UnauthorizedOperationException if you do not have the right to access the
     * <code>InternalAccount</code> property.
     */
    public final InternalAccount getInternalAccount() throws UnauthorizedOperationException {
        new ActorRight.InternalAccount().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return InternalAccount.create(getDao().getInternalAccount());
    }

    /**
     * @return true if you can access the <code>ExternalAccount</code> property.
     */
    public final boolean canGetExternalAccount() {
        return new ActorRight.ExternalAccount().canAccess(calculateRole(getLoginUnprotected()), Action.READ);
    }

    /**
     * @throws UnauthorizedOperationException if you haven't the right to access the
     * <code>ExtenralAccount</code> property.
     */
    public final ExternalAccount getExternalAccount() throws UnauthorizedOperationException {
        new ActorRight.ExternalAccount().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return ExternalAccount.create(getDao().getExternalAccount());
    }

    /**
     * @return true if you can access the <code>ExternalAccount</code> property.
     * @see #getBankTransactions()
     */
    public final boolean canAccessBankTransaction() {
        return new ActorRight.BankTransaction().canAccess(calculateRole(getLoginUnprotected()), Action.READ);
    }

    /**
     * @return all the bank transactions this actor has done.
     * @throws UnauthorizedOperationException if you haven't the right to access the
     * <code>ExtenralAccount</code> property.
     * @see DaoActor#getBankTransactions()
     */
    public final PageIterable<BankTransaction> getBankTransactions() throws UnauthorizedOperationException {
        new ActorRight.BankTransaction().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return new BankTransactionList(getDao().getBankTransactions());
    }
}
