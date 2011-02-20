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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.FatalErrorException;

/**
 * A transaction is a transaction between an internal account and an other account.
 */
@Entity
public final class DaoTransaction extends DaoIdentifiable {

    @Column(updatable = false, nullable = false)
    private Date creationDate;
    @ManyToOne(optional = false)
    private DaoInternalAccount from;
    @ManyToOne(optional = false)
    private DaoAccount to;
    @Column(updatable = false, nullable = false)
    private BigDecimal amount;

    public static DaoTransaction createAndPersist(final DaoInternalAccount from, final DaoAccount to, final BigDecimal amount) throws NotEnoughMoneyException {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoTransaction transaction = new DaoTransaction(from, to, amount);
        try {
            session.save(transaction);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return transaction;
    }

    /**
     * Create a new transaction and update the two accounts.
     * 
     * @param from is the account from which we will take money.
     * @param to is the account where the money goes
     * @param amount is the quantity of money transfered.
     * @throws NotEnoughMoneyException if there is not enough money to make the
     *         transaction
     * @throws FatalErrorException if to == from
     * @throws NullPointerException if any of the parameters = null
     */
    private DaoTransaction(final DaoInternalAccount from, final DaoAccount to, final BigDecimal amount) throws NotEnoughMoneyException {
        super();
        if (from.equals(to)) {
            throw new FatalErrorException("Cannot create a transaction on the same account.", null);
        }
        if (!from.hasEnoughMoney(amount) || !to.hasEnoughMoney(amount.negate())) {
            throw new NotEnoughMoneyException();
        }
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.creationDate = new Date();
        from.substractToAmountValue(amount);
        to.addToAmountValue(amount);
    }

    public DaoInternalAccount getFrom() {
        return from;
    }

    public DaoAccount getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getCreationDate() {
        return (Date) creationDate.clone();
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoTransaction() {
        super();
    }

    // ======================================================================
    // hashcode and equals.
    // ======================================================================

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
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
        if (!(obj instanceof DaoTransaction)) {
            return false;
        }
        final DaoTransaction other = (DaoTransaction) obj;
        if (amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!amount.equals(other.amount)) {
            return false;
        }
        if (creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!creationDate.equals(other.creationDate)) {
            return false;
        }
        if (from == null) {
            if (other.from != null) {
                return false;
            }
        } else if (!from.equals(other.from)) {
            return false;
        }
        if (to == null) {
            if (other.to != null) {
                return false;
            }
        } else if (!to.equals(other.to)) {
            return false;
        }
        return true;
    }

}
