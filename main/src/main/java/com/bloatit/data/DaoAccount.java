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
package com.bloatit.data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.InternalAccount;

/**
 * A DaoAccount generalize the idea of bank account for our system. This class
 * is mapped as a joined table. So there is a table for DaoAccount, and a table
 * for each of its children. Each time you want to access a DaoAccount, there is
 * a SQL join done, between the daoAccount and its child.
 */
// @formatter:off
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = { @NamedQuery(
                           name = "account.getTransactions",
                           query = "from DaoTransaction as t where t.from = :this or t.to = :this"),
                       @NamedQuery(
                           name = "account.getTransactions.size",
                           query = "select count(*) from DaoTransaction as t where t.from = :this or t.to = :this") })
// @formatter:on
public abstract class DaoAccount extends DaoIdentifiable {

    /**
     * The DaoActor is the person that own this account.
     */
    @OneToOne(optional = true, fetch = FetchType.LAZY)
    private DaoActor actor;

    @Basic(optional = false)
    private Date creationDate;

    @Basic(optional = false)
    private Date lastModificationDate;

    /**
     * The amount is the quantity of money you has in your account.
     */
    @Basic(optional = false)
    private BigDecimal amount;

    /**
     * Initialize the creation and modification dates. The amount is set to 0.
     * 
     * @param actor is the owner of this account
     * @throws NonOptionalParameterException if the actor == null
     */
    protected DaoAccount(final DaoActor actor) {
        if (actor == null) {
            throw new NonOptionalParameterException();
        }
        this.actor = actor;
        this.creationDate = new Date();
        this.lastModificationDate = this.creationDate;
        this.amount = BigDecimal.ZERO;
    }

    /**
     * Tells if you can take <code>amount</code> money in the account. On
     * {@link InternalAccount} the money has to exist. The
     * {@link ExternalAccount} can have negative amount of money.
     * 
     * @param amount The quantity of money you want to get. Should be > 0.
     * @return true if this operation is allowed.
     */
    protected abstract boolean hasEnoughMoney(BigDecimal amount);

    /**
     * Used internally or by subclasses to every time the Amount is changed. It
     * reset the modification date to now.
     */
    protected void resetModificationDate() {
        this.lastModificationDate = new Date();
    }

    /**
     * <p>
     * Add <code>value</code> into the account.
     * </p>
     * <p>
     * To modify the value of the amount, you have to create a transaction. This
     * method is protected to be used by transaction only !
     * </p>
     * 
     * @param value the quantity of money to add to the amount of this account.
     *            (May be a negative value)
     */
    void addToAmountValue(final BigDecimal value) {
        resetModificationDate();
        this.lastModificationDate = new Date();
        this.amount = this.amount.add(value);
    }

    /**
     * <p>
     * Substract <code>value</code> from the account.
     * </p>
     * To modify the value of the amount, you have to create a transaction. This
     * method is protected to be used by transaction only ! </p>
     * 
     * @param value the quantity of money to subtract to the amount of this
     *            account. (May be a negative value)
     */
    protected void substractToAmountValue(final BigDecimal value) {
        resetModificationDate();
        this.lastModificationDate = new Date();
        this.amount = this.amount.subtract(value);
    }

    /**
     * This is for hibernate only. The amount must be modified by some higher
     * level methods. For test purpose it is protected, but it should be
     * private.
     * 
     * @see DaoTransaction
     * @param amount the new amount to set.
     */
    protected void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    // ======================================================================
    // Getters
    // ======================================================================

    /**
     * <p>
     * Find all the transaction made from or to this account.
     * </p>
     * <p>
     * WARNING: the order is not specified yet. Maybe it will be ordered by date
     * (if needed)
     * </p>
     * 
     * @return all the transactions that are from/to this account.
     */
    public PageIterable<DaoTransaction> getTransactions() {
        return new QueryCollection<DaoTransaction>("account.getTransactions").setEntity("this", this);
    }

    /**
     * @return the modification date of this account
     */
    public Date getLastModificationDate() {
        return (Date) this.lastModificationDate.clone();
    }

    /**
     * @return the quantity of money on this account
     */
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * @return the actor that possess this account
     */
    public DaoActor getActor() {
        return this.actor;
    }

    /**
     * @return this account creation date.
     */
    public Date getCreationDate() {
        return (Date) this.creationDate.clone();
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * For hibernate mapping.
     */
    protected DaoAccount() {
        super();
    }

    // ======================================================================
    // equals and hashCode.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.actor == null) ? 0 : this.actor.hashCode());
        result = prime * result + ((this.creationDate == null) ? 0 : this.creationDate.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj.getClass().equals(getClass()))) {
            return false;
        }
        final DaoAccount other = (DaoAccount) obj;
        if (this.actor == null) {
            if (other.actor != null) {
                return false;
            }
        } else if (!this.actor.equals(other.actor)) {
            return false;
        }
        if (this.creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!this.creationDate.equals(other.creationDate)) {
            return false;
        }
        return true;
    }

}
