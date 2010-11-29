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
import com.bloatit.model.exceptions.NotEnoughMoneyException;

/**
 * A contribution is a financial participation on a demand. Each contribution can have a
 * little comment/description text on it (144 char max like twitter)
 */
@Entity
public class DaoContribution extends DaoUserContent {

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
    private BigDecimal amount;

    @ManyToOne(optional = false)
    private DaoDemand demand;

    @Basic(optional = false)
    @Enumerated
    private State state;

    @Column(length = 144, nullable = false, updatable = false)
    private String comment;

    /**
     * If the demand is validated then the contribution is also validated and then we
     * create a transaction. So there should be a non null transaction on each validated
     * contribution and only on those.
     */
    @OneToOne(optional = true)
    private DaoTransaction transaction;

    /**
     * Create a new contribution. Update the internal account of the member (block the
     * value that is reserved to this contribution)
     * 
     * @param member the person making the contribution
     * @param demand the demand on which we add a contribution
     * @param amount the amount of the contribution
     * @param comment the comment
     * @throws NullPointerException if any of the parameter is null
     * @throws NotEnoughMoneyException if the account of "member" has not enough money in
     * it.
     */
    public DaoContribution(DaoMember member, DaoDemand demand, BigDecimal amount, String comment) throws NotEnoughMoneyException {
        super(member);
        if (comment == null || demand == null) {
            throw new NullPointerException();
        }
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            Log.data().error("The amount of a contribution cannot be <= 0.");
            throw new FatalErrorException("The amount of a contribution cannot be <= 0.", null);
        }
        this.amount = amount;
        state = State.PENDING;
        this.demand = demand;
        this.comment = comment;
        getAuthor().getInternalAccount().block(amount);
    }

    /**
     * Set the state to ACCEPTED, and create the transaction. If there is not enough money
     * then throw and call cancel.
     * 
     * @param Offer the offer that is accepted.
     * @throws NotEnoughMoneyException if there is not enough money to create the
     * transaction.
     * @see DaoContribution#cancel()
     */
    public void accept(DaoOffer Offer) throws NotEnoughMoneyException {
        // TODO verify that the state is PENDING
        try {
            transaction = DaoTransaction.createAndPersist(getAuthor().getInternalAccount(), Offer.getAuthor().getInternalAccount(), amount);
            getAuthor().getInternalAccount().unBlock(amount);
            setState(State.ACCEPTED);
        } catch (final NotEnoughMoneyException e) {
            cancel();
            transaction = null;
            Log.data().error(e);
            throw e;
        }
    }

    /**
     * Set the state to CANCELED. (Unblock the blocked amount.)
     */
    public void cancel() {
        // TODO verify that the state is PENDING
        try {
            getAuthor().getInternalAccount().unBlock(amount);
        } catch (NotEnoughMoneyException e) {
            throw new FatalErrorException("Not enough money exception on cancel !!", e);
        }
        setState(State.CANCELED);
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

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoContribution() {}

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoDemand getDemand() {
        return demand;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setDemand(DaoDemand Demand) {
        demand = Demand;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setState(State state) {
        this.state = state;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setTransaction(DaoTransaction Transaction) {
        transaction = Transaction;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setComment(String comment) {
        this.comment = comment;
    }
}
