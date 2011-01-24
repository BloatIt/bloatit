package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.PageIterable;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.lists.TransactionList;
import com.bloatit.framework.right.AccountRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoMember;

/**
 * An account represent a way to store money. To transfer money from an account to an
 * other you have to use {@link Transaction}. When you create a transaction, the two
 * accounts are update. There are two types of accounts : the internals and externals. The
 * {@link InternalAccount} account is for the money we store for a user, the
 * {@link ExternalAccount} is an account in a bank.
 */
public abstract class Account extends Identifiable {

    /**
     * Since the Account class is abstract we need a way to get the daoAccount for this
     * account.
     * 
     * @return the {@link DaoAccount} for this {@link Account}.
     */
    protected abstract DaoAccount getDaoAccount();

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
        return getDaoAccount().getLastModificationDate();
    }

    /**
     * @return the quantity of money available on this account.
     * @throws UnauthorizedOperationException if you have not the right to access the
     *         <code>Amount</code> property in this class.
     */
    public final BigDecimal getAmount() throws UnauthorizedOperationException {
        new AccountRight.Amount().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getDaoAccount().getAmount();
    }

    /**
     * @return All the transactions involving this account.
     * @throws UnauthorizedOperationException if you have not the right to access the
     *         <code>Transaction</code> property in this class.
     */
    public final PageIterable<Transaction> getTransactions() throws UnauthorizedOperationException {
        new AccountRight.Transaction().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return new TransactionList(getDaoAccount().getTransactions());
    }

    /**
     * The actor is the person that possess this account.
     * 
     * @throws UnauthorizedOperationException if you have not the right to access the
     *         <code>Actor</code> property in this class.
     */
    public final Actor getActor() throws UnauthorizedOperationException {
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
        return getDaoAccount().getCreationDate();
    }

    /**
     * This method is used only in the authentication process. You should never used it
     * anywhere else.
     * 
     * @see getActor;
     */
    protected final Actor getActorUnprotected() {
        if (getDaoAccount().getActor().getClass() == DaoMember.class) {
            return Member.create((DaoMember) getDaoAccount().getActor());
        } else if (getDaoAccount().getActor().getClass() == DaoGroup.class) {
            return Group.create((DaoGroup) getDaoAccount().getActor());
        }
        throw new FatalErrorException("Cannot find the right Actor child class.", null);
    }

    /**
     * @see Identifiable#getId();
     */
    @Override
    public final int getId() {
        return getDaoAccount().getId();
    }

}
