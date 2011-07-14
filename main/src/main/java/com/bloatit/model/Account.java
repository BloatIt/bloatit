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
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.TransactionList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.RgtAccount;
import com.bloatit.model.right.UnauthorizedOperationException;

/**
 * An account represent a way to store money. To transfer money from an account
 * to an other you have to use {@link Transaction}. When you create a
 * transaction, the two accounts are update. There are two types of accounts :
 * the internals and externals. The
 * 
 * @param <T> is the Dao object corresponding to this model layer object.
 *            {@link InternalAccount} account is for the money we store for a
 *            user, the {@link ExternalAccount} is an account in a bank.
 */
public abstract class Account<T extends DaoAccount> extends Identifiable<T> {

    /**
     * Instantiates a new account.
     * 
     * @param id the id
     */
    protected Account(final T id) {
        super(id);
    }

    // ///////////////////////////
    // Can ...

    /**
     * Can access transaction.
     * 
     * @return true if the authenticated user can access the
     *         <code>Transaction</code> property (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessTransaction() {
        return canAccess(new RgtAccount.Transaction(), Action.READ);
    }

    /**
     * Can access amount.
     * 
     * @return true if the authenticated user can access the <code>Amount</code>
     *         property (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessAmount() {
        return canAccess(new RgtAccount.Amount(), Action.READ);
    }

    /**
     * Can access actor.
     * 
     * @return true if the authenticated user can access the <code>Actor</code>
     *         property (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessActor() {
        return canAccess(new RgtAccount.Actor(), Action.READ);
    }

    /**
     * Can access creation date.
     * 
     * @return true if the authenticated user can access the
     *         <code>CreationDate</code> property (It is a read only property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessCreationDate() {
        return canAccess(new RgtAccount.CreationDate(), Action.READ);
    }

    /**
     * Can access last modification date.
     * 
     * @return true if the authenticated user can access the
     *         <code>LastModificationDate</code> property (It is a read only
     *         property).
     * @see #authenticate(AuthToken)
     */
    public final boolean canAccessLastModificationDate() {
        return canAccess(new RgtAccount.LastModificationDate(), Action.READ);
    }

    // ///////////////////////////
    // get / set ...

    /**
     * Every time a new transaction is done the modification date is update.
     * This can be used for security purpose.
     * 
     * @return the last modification date.
     * @throws UnauthorizedOperationException if you have not the right to
     *             access the <code>LastModificationDate</code> property in this
     *             class.
     */
    public final Date getLastModificationDate() throws UnauthorizedOperationException {
        tryAccess(new RgtAccount.LastModificationDate(), Action.READ);
        return getDao().getLastModificationDate();
    }

    /**
     * Gets the amount.
     * 
     * @return the quantity of money available on this account.
     * @throws UnauthorizedOperationException if you have not the right to
     *             access the <code>Amount</code> property in this class.
     */
    public final BigDecimal getAmount() throws UnauthorizedOperationException {
        tryAccess(new RgtAccount.Amount(), Action.READ);
        return getDao().getAmount();
    }

    /**
     * Gets the transactions.
     * 
     * @return All the transactions involving this account.
     * @throws UnauthorizedOperationException if you have not the right to
     *             access the <code>Transaction</code> property in this class.
     */
    public final PageIterable<Transaction> getTransactions() throws UnauthorizedOperationException {
        tryAccess(new RgtAccount.Transaction(), Action.READ);
        return new TransactionList(getDao().getTransactions());
    }

    /**
     * The actor is the person that possess this account.
     * 
     * @return the actor
     * @throws UnauthorizedOperationException if you have not the right to
     *             access the <code>Actor</code> property in this class.
     */
    public final Actor<?> getActor() throws UnauthorizedOperationException {
        tryAccess(new RgtAccount.Actor(), Action.READ);
        return getActorUnprotected();
    }

    /**
     * Gets the creation date.
     * 
     * @return The date of creation of this account (Amazing !)
     * @throws UnauthorizedOperationException if you have not the right to
     *             access the <code>CreationDate</code> property in this class.
     */
    public final Date getCreationDate() throws UnauthorizedOperationException {
        tryAccess(new RgtAccount.CreationDate(), Action.READ);
        return getDao().getCreationDate();
    }

    // ///////////////////////////
    // Unprotected methods

    /**
     * This method is used only in the authentication process. You should never
     * used it anywhere else.
     * 
     * @return the actor unprotected
     * @see #getActor()
     */
    final Actor<?> getActorUnprotected() {
        return (Actor<?>) getDao().getActor().accept(new DataVisitorConstructor());
    }

}
