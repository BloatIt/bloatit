package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.Log;
import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

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
        PENDING, ACCEPTED, CANCELED
    }

    /**
     * The amount is the quantity of money put in this contribution.
     */
    @Basic(optional = false)
    @Column(updatable = false)
    private BigDecimal amount;

    @SuppressWarnings("unused")
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
    @OneToOne(optional = true)
    private DaoTransaction transaction;

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
    public DaoContribution(final DaoMember member, final DaoDemand demand, final BigDecimal amount, final String comment)
                                                                                                                         throws NotEnoughMoneyException {
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
        getAuthor().getInternalAccount().block(amount);
    }

    /**
     * Set the state to ACCEPTED, and create the transaction. If there is not enough money
     * then throw and set the state to canceled.
     * 
     * @param offer the offer that is accepted.
     * @throws NotEnoughMoneyException if there is not enough money to create the
     *         transaction.
     */
    public void accept(final DaoOffer offer) throws NotEnoughMoneyException {
        if (state != State.PENDING) {
            throw new FatalErrorException("Cannot accept a contribution if its state isn't PENDING");
        }
        try {
            // First we try to unblock. It can throw a notEnouthMoneyException.
            getAuthor().getInternalAccount().unBlock(amount);
        } catch (final NotEnoughMoneyException e) {
            // If it fails then there is a bug in our code. Set the state to
            // canceled and throw a fatalError.
            this.state = State.CANCELED;
            Log.data().fatal(e);
            throw new FatalErrorException("Not enough money exception on cancel !!", e);
        }
        try {
            // If we succeeded the unblock then we create a transaction.
            if (getAuthor() != offer.getAuthor()) {
                this.transaction = DaoTransaction.createAndPersist(getAuthor().getInternalAccount(), offer.getAuthor().getInternalAccount(), amount);
            }
            // if the transaction is ok then we set the state to ACCEPTED.
            this.state = State.ACCEPTED;
        } catch (final NotEnoughMoneyException e) {
            this.state = State.CANCELED;
            Log.data().error(e);
            throw e;
        }
    }

    /**
     * Set the state to CANCELED. (Unblock the blocked amount.)
     */
    public void cancel() {
        if (state != State.PENDING) {
            throw new FatalErrorException("Cannot cancel a contribution if its state isn't PENDING");
        }
        try {
            getAuthor().getInternalAccount().unBlock(amount);
        } catch (final NotEnoughMoneyException e) {
            Log.data().fatal(e);
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

    public DaoTransaction getTransaction() {
        return transaction;
    }

    public String getComment() {
        return comment;
    }
    
    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoContribution() {
        super();
    }
}
