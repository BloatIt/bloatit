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

import com.bloatit.data.DaoAccount;
import com.bloatit.data.DaoGroup;
import com.bloatit.data.DaoMember;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.TransactionList;
import com.bloatit.model.right.AccountRight;
import com.bloatit.model.right.RightManager.Action;

/**
 * An account represent a way to store money. To transfer money from an account to an
 * other you have to use {@link Transaction}. When you create a transaction, the two
 * accounts are update. There are two types of accounts : the internals and externals. The
 * {@link InternalAccount} account is for the money we store for a user, the
 * {@link ExternalAccount} is an account in a bank.
 */
public abstract class Account<T extends DaoAccount> extends Identifiable<T> {

    protected Account(final T id) {
        super(id);
    }

    /**
     * @return true if the authenticated user can access the <code>Transaction</code>
     *         property (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessTransaction() {
        return new AccountRight.Transaction().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * @return true if the authenticated user can access the <code>Amount</code> property
     *         (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessAmount() {
        return new AccountRight.Amount().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * @return true if the authenticated user can access the <code>Comment</code> property
     *         (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessComment() {
        return new AccountRight.Comment().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * @return true if the authenticated user can access the <code>Actor</code> property
     *         (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessActor() {
        return new AccountRight.Actor().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * @return true if the authenticated user can access the <code>CreationDate</code>
     *         property (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessCreationDate() {
        return new AccountRight.CreationDate().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * @return true if the authenticated user can access the
     *         <code>LastModificationDate</code> property (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessLastModificationDate() {
        return new AccountRight.LastModificationDate().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * Every time a new transaction is done the modification date is update. This can be
     * used for security purpose.
     *
     * @throws UnauthorizedOperationException if you have not the right to access the
     *         <code>LastModificationDate</code> property in this class.
     */
    public final Date getLastModificationDate() throws UnauthorizedOperationException {
        new AccountRight.LastModificationDate().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getDao().getLastModificationDate();
    }

    /**
     * @return the quantity of money available on this account.
     * @throws UnauthorizedOperationException if you have not the right to access the
     *         <code>Amount</code> property in this class.
     */
    public final BigDecimal getAmount() throws UnauthorizedOperationException {
        new AccountRight.Amount().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getDao().getAmount();
    }

    /**
     * @return All the transactions involving this account.
     * @throws UnauthorizedOperationException if you have not the right to access the
     *         <code>Transaction</code> property in this class.
     */
    public final PageIterable<Transaction> getTransactions() throws UnauthorizedOperationException {
        new AccountRight.Transaction().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return new TransactionList(getDao().getTransactions());
    }

    /**
     * The actor is the person that possess this account.
     *
     * @throws UnauthorizedOperationException if you have not the right to access the
     *         <code>Actor</code> property in this class.
     */
    public final Actor<?> getActor() throws UnauthorizedOperationException {
        new AccountRight.Actor().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getActorUnprotected();
    }

    /**
     * @return The date of creation of this account (Amazing !)
     * @throws UnauthorizedOperationException if you have not the right to access the
     *         <code>CreationDate</code> property in this class.
     */
    public final Date getCreationDate() throws UnauthorizedOperationException {
        new AccountRight.CreationDate().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getDao().getCreationDate();
    }

    /**
     * This method is used only in the authentication process. You should never used it
     * anywhere else.
     *
     * @see getActor;
     */
    protected final Actor<?> getActorUnprotected() {
        if (getDao().getActor().getClass() == DaoMember.class) {
            return Member.create((DaoMember) getDao().getActor());
        } else if (getDao().getActor().getClass() == DaoGroup.class) {
            return Group.create((DaoGroup) getDao().getActor());
        }
        throw new FatalErrorException("Cannot find the right Actor child class.", null);
    }

}
