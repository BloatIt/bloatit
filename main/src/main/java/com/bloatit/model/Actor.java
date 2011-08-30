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

import org.hibernate.ObjectNotFoundException;

import com.bloatit.data.DaoActor;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.BankTransactionList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtActor;
import com.bloatit.model.right.RgtMember;
import com.bloatit.model.right.UnauthorizedBankDataAccessException;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.model.right.UnauthorizedPublicAccessException;
import com.bloatit.model.right.UnauthorizedPublicReadOnlyAccessException;

/**
 * The Class Actor.
 * 
 * @param <T> the Dao version of this model layer object.
 * @see DaoActor
 */
public abstract class Actor<T extends DaoActor> extends Identifiable<T> {

    public static Actor<?> getActorFromDao(final DaoActor dao) {
        final Integer id = dao.getId();
        try {
            Team team;
            team = Team.class.cast(GenericConstructor.create(Team.class, id));
            if (team != null) {
                return team;
            }
        } catch (final ClassNotFoundException e) {
            // This is not a team
        } catch (final ClassCastException e) {
            // This is not a team
        }
        try {
            Member member;
            member = Member.class.cast(GenericConstructor.create(Member.class, id));
            if (member != null) {
                return member;
            }
        } catch (final ClassNotFoundException e) {
            // not a member nether a team !
            throw new BadProgrammerException(e);
        } catch (final ClassCastException e) {
            throw new BadProgrammerException(e);
        }   
        return null;
    }

    /**
     * Instantiates a new actor.
     * 
     * @param id the id
     */
    protected Actor(final T id) {
        super(id);
    }

    // /////////////////
    // Get / set ...

    public final void setLogin(final String login) throws UnauthorizedPublicAccessException {
        tryAccess(new RgtActor.Login(), Action.WRITE);
        getDao().setLogin(login);
    }

    /**
     * Gets the login. This method is not protected by the right manager because
     * we think it is not needed, and it make our code not readable.
     * 
     * @return the login
     * @see DaoActor#getLogin()
     */
    public final String getLogin() {
        return getDao().getLogin();
    }

    /**
     * Gets the creation date of this {@link Actor}.
     * 
     * @return the creation date
     * @see DaoActor#getDateCreation()
     */
    public final Date getDateCreation() throws UnauthorizedPublicReadOnlyAccessException {
        tryAccess(new RgtActor.DateCreation(), Action.READ);
        return getDao().getDateCreation();
    }

    /**
     * The internal account is the account we manage internally. Users can
     * add/get money to/from it, and can use this money to contribute on
     * softwares.
     * 
     * @return the internal account
     * @throws UnauthorizedBankDataAccessException the unauthorized operation
     *             exception
     * @throw UnauthorizedOperationException if you do not have the right to
     *        access the <code>InternalAccount</code> property.
     */
    public final InternalAccount getInternalAccount() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtActor.InternalAccount(), Action.READ);
        return InternalAccount.create(getDao().getInternalAccount());
    }

    /**
     * Gets the external account.
     * 
     * @return the external account
     * @throws UnauthorizedBankDataAccessException if you haven't the right to
     *             access the <code>ExtenralAccount</code> property.
     */
    public final ExternalAccount getExternalAccount() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtActor.ExternalAccount(), Action.READ);
        return ExternalAccount.create(getDao().getExternalAccount());
    }

    /**
     * Gets the bank transactions.
     * 
     * @return all the bank transactions this actor has done.
     * @throws UnauthorizedBankDataAccessException if you haven't the right to
     *             access the <code>ExtenralAccount</code> property.
     * @see DaoActor#getBankTransactions()
     */
    public final PageIterable<BankTransaction> getBankTransactions() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtActor.BankTransaction(), Action.READ);
        return new BankTransactionList(getDao().getBankTransactions());
    }

    /**
     * Returns the contributions done by this actor.
     * 
     * @return the contributions done by this actor
     * @throws UnauthorizedOperationException
     */
    public PageIterable<Contribution> getContributions() throws UnauthorizedOperationException {
        tryAccess(new RgtActor.Contribution(), Action.READ);
        return doGetContributions();
    }

    public Contact getContact() throws UnauthorizedPrivateAccessException {
        tryAccess(new RgtMember.Contact(), Action.READ);
        return getContactUnprotected();
    }

    public Contact getContactUnprotected() {
        return Contact.create(getDao().getContact());
    }

    public abstract PageIterable<Contribution> doGetContributions() throws UnauthorizedOperationException;

    /**
     * Returns the money withdraw done by this actor.
     * 
     * @return the witdraws done by this actor
     * @throws UnauthorizedOperationException
     */
    public PageIterable<MoneyWithdrawal> getMoneyWithdrawals() throws UnauthorizedOperationException {
        tryAccess(new RgtActor.MoneyWithdrawal(), Action.READ);
        return doGetMoneyWithdrawals();
    }

    public abstract PageIterable<MoneyWithdrawal> doGetMoneyWithdrawals() throws UnauthorizedOperationException;

    /**
     * @return the display name
     */
    public abstract String getDisplayName();

    /**
     * @return the avatar.
     */
    public abstract Image getAvatar();

    // /////////////////
    // Can ...

    /**
     * Tells if the authenticated user can access date creation.
     * 
     * @return true if you can access the DateCreation property.
     * @see Actor#getDateCreation()
     */
    public final boolean canAccessDateCreation() {
        return canAccess(new RgtActor.DateCreation(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get internal account.
     * 
     * @return true if you can access the <code>InternalAccount</code> property.
     * @see Actor#getInternalAccount()
     */
    public final boolean canGetInternalAccount() {
        return canAccess(new RgtActor.InternalAccount(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get external account.
     * 
     * @return true if you can access the <code>ExternalAccount</code> property.
     */
    public final boolean canGetExternalAccount() {
        return canAccess(new RgtActor.ExternalAccount(), Action.READ);
    }

    /**
     * Tells if the authenticated user can get the bank transaction.
     * 
     * @return true if you can access the <code>BankTransaction</code> property
     *         (READ right).
     */
    public final boolean canGetBankTransactionAccount() {
        return canAccess(new RgtActor.BankTransaction(), Action.READ);
    }

    public final boolean canGetContributions() {
        return canAccess(new RgtActor.Contribution(), Action.READ);
    }

    public final boolean isTeam() {
        return this instanceof Team;
    }

    public boolean hasInvoicingContact() throws UnauthorizedPrivateAccessException {
        return hasInvoicingContact(false);
    }
    
    public boolean hasInvoicingContact(boolean all) throws UnauthorizedPrivateAccessException {
        final Contact contact = getContact();
        if (contact.getName() == null) {
            return false;
        }
        if (contact.getCountry() == null) {
            return false;
        }
        if (contact.getPostalCode() == null) {
            return false;
        }
        if (contact.getStreet() == null) {
            return false;
        }

        if (all) {
            if (contact.getInvoiceIdNumber() == null) {
                return false;
            }
            if (contact.getInvoiceIdTemplate() == null) {
                return false;
            }
            if (contact.getLegalId() == null) {
                return false;
            }
            if (contact.getTaxIdentification() == null) {
                return false;
            }
            if (contact.getTaxRate() == null) {
                return false;
            }
        }

        return true;
    }
}
