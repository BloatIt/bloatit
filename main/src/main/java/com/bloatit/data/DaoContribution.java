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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.Query;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.common.Log;
import com.bloatit.data.DaoEvent.EventType;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.PageIterable;

/**
 * A contribution is a financial participation on a feature. Each contribution
 * can have a little comment/description text on it (144 char max like twitter)
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           cacheable=true,
                           name = "contribution.getMoneyRaised",
                           query = "SELECT sum(amount) " +
                           		   "FROM DaoContribution " +
                           		   "WHERE state != :state"),
                        @NamedQuery(
                                    name = "contribution.getInvoices",
                                    query = "FROM com.bloatit.data.DaoContributionInvoice invoice_ " +
                                    "WHERE invoice_.contribution = :this "),
                        @NamedQuery(
                                    name = "contribution.getInvoices.size",
                                    query = "SELECT count(*) " +
                                    "FROM com.bloatit.data.DaoContributionInvoice invoice_ " +
                                    "WHERE invoice_.contribution = :this "),
                        @NamedQuery(
                                    name = "contribution.getByFeatureMember",
                                    query = "FROM com.bloatit.data.DaoContribution contrib_ \n" +
                                            "WHERE contrib_.member = :member \n" +
                                            "AND contrib_.feature = :feature \n"),
                        @NamedQuery(
                                    name = "contribution.getByFeatureMember.size",
                                    query = "SELECT count(*)" +
                                            "FROM com.bloatit.data.DaoContribution contrib_ \n" +
                                            "WHERE contrib_.member = :member \n" +
                                            "AND contrib_.feature = :feature \n"),
                       }
             )
// @formatter:on
public class DaoContribution extends DaoUserContent {

    /** The Constant COMMENT_MAX_LENGTH. */
    protected static final int COMMENT_MAX_LENGTH = 140;

    /**
     * The state of a contribution should follow the state of the associated
     * feature.
     */
    public enum ContributionState {
        /** The PENDING. */
        PENDING,
        /** The VALIDATED. */
        VALIDATED,
        /** The CANCELED. */
        CANCELED
    }

    /**
     * The amount is the quantity of money put in this contribution.
     */
    @Basic(optional = false)
    @Column(updatable = false)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    private DaoFeature feature;

    @Basic(optional = false)
    @Enumerated
    private ContributionState state;

    @Column(length = COMMENT_MAX_LENGTH, updatable = false)
    private String comment;

    /**
     * If the feature is validated then the contribution is also validated and
     * then we create a transaction. So there should be a non null transaction
     * on each validated contribution and only on those. (Except when a user add
     * on offer on his own offer -> no transaction)
     */
    @OneToMany(orphanRemoval = false, cascade = CascadeType.PERSIST)
    private final List<DaoTransaction> transaction = new ArrayList<DaoTransaction>();

    @OneToMany(orphanRemoval = false, cascade = CascadeType.PERSIST, mappedBy = "contribution")
    private final List<DaoContributionInvoice> invoices = new ArrayList<DaoContributionInvoice>();

    @Basic(optional = false)
    private int percentDone;

    @Basic(optional = false)
    private BigDecimal alreadyGivenMoney;

    public static PageIterable<DaoContribution> getByFeatureMember(DaoFeature f, DaoMember m) {
        return new QueryCollection<DaoContribution>("contribution.getByFeatureMember").setEntity("member", m).setEntity("feature", f);
    }

    /**
     * Gets the money raised.
     * 
     * @return the money raised
     */
    public static BigDecimal getMoneyRaised() {
        final Query q = SessionManager
                .getNamedQuery("contribution.getMoneyRaised")
                .setParameter("state", ContributionState.CANCELED);

        return (BigDecimal) q.uniqueResult();
    }

    /**
     * Create a new contribution. Update the internal account of the author
     * (block the value that is reserved to this contribution)
     * 
     * @param member the person making the contribution. (Use
     *            DaoUserContent#setAsTeam() to make a contribution in the name
     *            of team)
     * @param team the team can be null.
     * @param feature the feature on which we add a contribution.
     * @param amount the amount of the contribution.
     * @param comment the comment can be null.
     * @throws NotEnoughMoneyException if the account of "member" has not enough
     *             money in it.
     */
    protected DaoContribution(final DaoMember member, final DaoTeam team, final DaoFeature feature, final BigDecimal amount, final String comment) throws NotEnoughMoneyException {
        super(member, team);
        checkOptionnal(feature);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            Log.data().error("The amount of a contribution cannot be <= 0.");
            throw new BadProgrammerException("The amount of a contribution cannot be <= 0.", null);
        }
        this.amount = amount;
        this.state = ContributionState.PENDING;
        this.feature = feature;
        this.comment = comment;
        this.percentDone = 0;
        this.alreadyGivenMoney = BigDecimal.ZERO;
        getAuthor().getInternalAccount().block(amount);
    }

    /**
     * Create a transaction from the contributor to the developer. If there is
     * not enough money then throw and set the state to canceled. After that if
     * all the money is transfered, the state of this contribution is become
     * VALIDATED.
     * 
     * @param offer the offer that is accepted.
     * @param percent integer ]0,100]. It is the percent of the total amount and
     *            not a percent of what is remaining. It is the percent of the
     *            total amount to transfer. There is a "round" done here, but we
     *            assure that when 100% is reached then everything is
     *            transfered. For example : 90% then 10% is ok and everything is
     *            transfered. 60% then 60% will throw an exception.
     * @throws NotEnoughMoneyException if there is not enough money to create
     *             the transaction.
     * @return the amount of the payment for the milestone
     */
    BigDecimal validate(final DaoMilestone milestone, final int percent) throws NotEnoughMoneyException {
        if (this.state != ContributionState.PENDING) {
            throw new BadProgrammerException("Cannot validate a contribution if its state isn't PENDING");
        }
        if (percent > 100 || percent <= 0 || (this.percentDone + percent) > 100) {
            throw new BadProgrammerException("Percent must be > 0 and <= 100.");
        }
        final BigDecimal moneyToGive = calculateHowMuchToTransfer(percent);
        final DaoInternalAccount fromAccount = getAuthor().getInternalAccount();
        final DaoInternalAccount toAccount = milestone.getOffer().getAuthor().getInternalAccount();
        try {
            // First we try to unblock. It can throw a notEnouthMoneyException.
            fromAccount.unBlock(moneyToGive);
        } catch (final NotEnoughMoneyException e) {
            // If it fails then there is a bug in our code. Set the state to
            // canceled and throw a fatalError.
            this.state = ContributionState.CANCELED;
            throw new BadProgrammerException("Not enough money exception on cancel !!", e);
        }
        try {
            // If we succeeded the unblock then we create a transaction.
            if (!fromAccount.equals(toAccount)) {
                this.transaction.add(DaoTransaction.createAndPersist(fromAccount, toAccount, moneyToGive));
            }
            // if the transaction is ok then we set the state to VALIDATED.
            this.percentDone += percent;
            this.alreadyGivenMoney = this.alreadyGivenMoney.add(moneyToGive);
            if (this.percentDone == 100) {
                this.state = ContributionState.VALIDATED;
            }
        } catch (final NotEnoughMoneyException e) {
            this.state = ContributionState.CANCELED;
            throw e;
        }
        return moneyToGive;
    }

    private BigDecimal calculateHowMuchToTransfer(final int percent) {
        BigDecimal moneyToGive;
        if ((percent + this.percentDone) == 100) {
            moneyToGive = this.amount.subtract(this.alreadyGivenMoney);
        } else {
            moneyToGive = new BigDecimal((this.amount.floatValue() * percent) / 100).setScale(2, RoundingMode.HALF_EVEN);
        }
        return moneyToGive;
    }

    /**
     * Set the state to CANCELED. (Unblock the blocked amount.)
     */
    public void cancel() {
        if (this.state != ContributionState.PENDING) {
            throw new BadProgrammerException("Cannot cancel a contribution if its state isn't PENDING");
        }
        try {
            final BigDecimal moneyToCancel = this.amount.subtract(this.alreadyGivenMoney);
            getAuthor().getInternalAccount().unBlock(moneyToCancel);
            this.feature.cancelContribution(moneyToCancel);
        } catch (final NotEnoughMoneyException e) {
            throw new BadProgrammerException("Not enough money exception on cancel !!", e);
        }
        this.state = ContributionState.CANCELED;
        DaoEvent.createContributionEvent(feature, EventType.REMOVE_CONTRIBUTION, this);
    }

    /**
     * Gets the amount is the quantity of money put in this contribution.
     * 
     * @return the amount is the quantity of money put in this contribution
     */
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * Gets the state.
     * 
     * @return the state
     */
    public ContributionState getState() {
        return this.state;
    }

    /**
     * Gets the comment.
     * 
     * @return the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Gets the feature.
     * 
     * @return the feature
     */
    public DaoFeature getFeature() {
        return this.feature;
    }

    /**
     * Gets the feature.
     * 
     * @return the feature
     */
    public PageIterable<DaoContributionInvoice> getInvoices() {
        return new MappedList<DaoContributionInvoice>(invoices);
    }

    @Override
    public void setIsDeleted(final Boolean isDeleted) {
        throw new IllegalStateException("You cannot delete a contribution. Use Cancel instead.");
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.data.DaoIdentifiable#accept(com.bloatit.data.DataClassVisitor
     * )
     */
    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao contribution.
     */
    protected DaoContribution() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
        result = prime * result + ((this.feature == null) ? 0 : this.feature.hashCode());
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
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoContribution other = (DaoContribution) obj;
        if (this.amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!this.amount.equals(other.amount)) {
            return false;
        }
        if (this.comment == null) {
            if (other.comment != null) {
                return false;
            }
        } else if (!this.comment.equals(other.comment)) {
            return false;
        }
        if (this.feature == null) {
            if (other.feature != null) {
                return false;
            }
        } else if (!this.feature.equals(other.feature)) {
            return false;
        }
        return true;
    }
}
