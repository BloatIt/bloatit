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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.bloatit.common.Log;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;

/**
 * A contribution is a financial participation on a demand. Each contribution can have a
 * little comment/description text on it (144 char max like twitter)
 */
@Entity
public final class DaoContribution extends DaoUserContent {
    protected static final int COMMENT_MAX_LENGTH = 144;

    /**
     * The state of a contribution should follow the state of the associated demand.
     */
    public enum State {
        PENDING, VALIDATED, CANCELED
    }

    /**
     * The amount is the quantity of money put in this contribution.
     */
    @Basic(optional = false)
    @Column(updatable = false)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    private DaoDemand demand;

    @Basic(optional = false)
    @Enumerated
    private State state;

    @Column(length = COMMENT_MAX_LENGTH, updatable = false)
    private String comment;

    /**
     * If the demand is validated then the contribution is also validated and then we
     * create a transaction. So there should be a non null transaction on each validated
     * contribution and only on those. (Except when a user add on offer on his own offer
     * -> no transaction)
     */
    @OneToMany(orphanRemoval = false, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private final Set<DaoTransaction> transaction = new HashSet<DaoTransaction>();

    @Basic(optional = false)
    private int percentDone;

    @Basic(optional = false)
    private BigDecimal alreadyGivenMoney;

    /**
     * Create a new contribution. Update the internal account of the member (block the
     * value that is reserved to this contribution)
     * 
     * @param member the person making the contribution.
     * @param demand the demand on which we add a contribution.
     * @param amount the amount of the contribution.
     * @param comment the comment can be null.
     * @throws NonOptionalParameterException if any of the parameter is null except
     *         comment.
     * @throws NotEnoughMoneyException if the account of "member" has not enough money in
     *         it.
     */
    public DaoContribution(final DaoMember member, final DaoDemand demand, final BigDecimal amount, final String comment) throws NotEnoughMoneyException {
        super(member);
        if (demand == null) {
            throw new NonOptionalParameterException();
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            Log.data().error("The amount of a contribution cannot be <= 0.");
            throw new FatalErrorException("The amount of a contribution cannot be <= 0.", null);
        }
        this.amount = amount;
        this.state = State.PENDING;
        this.demand = demand;
        this.comment = comment;
        this.percentDone = 0;
        this.alreadyGivenMoney = BigDecimal.ZERO;
        getAuthor().getInternalAccount().block(amount);
    }

    /**
     * Create a transaction from the contributor to the developer. If there is not enough
     * money then throw and set the state to canceled. After that if all the money is
     * transfered, the state of this contribution is become VALIDATED.
     * 
     * @param offer the offer that is accepted.
     * @param percent integer ]0,100]. It is the percent of the total amount and not a
     *        percent of what is remaining. It is the percent of the total amount to
     *        transfer. There is a "round" done here, but we assure that when 100% is
     *        reached then everything is transfered. For example : 90% then 10% is ok and
     *        everything is transfered. 60% then 60% will throw an exception.
     * @throws NotEnoughMoneyException if there is not enough money to create the
     *         transaction.
     */
    public void validate(final DaoOffer offer, final int percent) throws NotEnoughMoneyException {
        if (state != State.PENDING) {
            throw new FatalErrorException("Cannot validate a contribution if its state isn't PENDING");
        }
        if (percent > 100 || percent <= 0 || (percentDone + percent) > 100) {
            throw new FatalErrorException("Percent must be > 0 and <= 100.");
        }
        final BigDecimal moneyToGive = calculateHowMuchToTransfer(percent);
        try {
            // First we try to unblock. It can throw a notEnouthMoneyException.
            getAuthor().getInternalAccount().unBlock(moneyToGive);
        } catch (final NotEnoughMoneyException e) {
            // If it fails then there is a bug in our code. Set the state to
            // canceled and throw a fatalError.
            this.state = State.CANCELED;
            throw new FatalErrorException("Not enough money exception on cancel !!", e);
        }
        try {
            // If we succeeded the unblock then we create a transaction.
            if (getAuthor() != offer.getAuthor()) {
                this.transaction.add(DaoTransaction.createAndPersist(getAuthor().getInternalAccount(), offer.getAuthor().getInternalAccount(), moneyToGive));
            }
            // if the transaction is ok then we set the state to VALIDATED.
            this.percentDone += percent;
            this.alreadyGivenMoney = alreadyGivenMoney.add(moneyToGive);
            if (percentDone == 100) {
                this.state = State.VALIDATED;
            }
        } catch (final NotEnoughMoneyException e) {
            this.state = State.CANCELED;
            throw e;
        }
    }

    private BigDecimal calculateHowMuchToTransfer(final int percent) {
        BigDecimal moneyToGive;
        if ((percent + percentDone) == 100) {
            moneyToGive = amount.subtract(alreadyGivenMoney);
        } else {
            moneyToGive = amount.multiply(new BigDecimal((amount.floatValue() * percent) / 100));
        }
        return moneyToGive;
    }

    /**
     * Set the state to CANCELED. (Unblock the blocked amount.)
     */
    public void cancel() {
        if (state != State.PENDING) {
            throw new FatalErrorException("Cannot cancel a contribution if its state isn't PENDING");
        }
        try {
            final BigDecimal moneyToCancel = amount.subtract(alreadyGivenMoney);
            getAuthor().getInternalAccount().unBlock(moneyToCancel);
            demand.cancelContribution(moneyToCancel);
        } catch (final NotEnoughMoneyException e) {
            throw new FatalErrorException("Not enough money exception on cancel !!", e);
        }
        this.state = State.CANCELED;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public State getState() {
        return state;
    }

    public String getComment() {
        return comment;
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

    protected DaoContribution() {
        super();
    }

    protected DaoDemand getDemand() {
        return demand;
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((demand == null) ? 0 : demand.hashCode());
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
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoContribution other = (DaoContribution) obj;
        if (amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!amount.equals(other.amount)) {
            return false;
        }
        if (comment == null) {
            if (other.comment != null) {
                return false;
            }
        } else if (!comment.equals(other.comment)) {
            return false;
        }
        if (demand == null) {
            if (other.demand != null) {
                return false;
            }
        } else if (!demand.equals(other.demand)) {
            return false;
        }
        return true;
    }
}
