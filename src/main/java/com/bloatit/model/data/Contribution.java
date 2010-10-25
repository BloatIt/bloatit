package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Contribution extends UserContent {

    public enum State {
        WAITING, ACCEPTED, CANCELED
    }

    @Basic(optional = false)
    private BigDecimal amount;

    @ManyToOne
    private Demand demand;

    @Basic(optional = false)
    @Enumerated
    private State state;

    @OneToOne(optional = true)
    private Transaction transaction;

    public Contribution() {}

    // the demand is associated into the Demand class by hibernate.
    public Contribution(Member member, BigDecimal amount) {
        // TODO make sure amount > 0
        super(member);
        this.amount = amount;
        this.setState(State.WAITING);
        getAuthor().getInternalAccount().block(amount);
    }

    public void accept(Offer offer) {
        // TODO verify that the state is WAITING
        getAuthor().getInternalAccount().unBlock(amount);
        transaction = Transaction.createAndPersist(getAuthor().getInternalAccount(), offer.getAuthor().getInternalAccount(), amount);
        setState(State.ACCEPTED);
    }

    public void cancel() {
        // TODO verify that the state is WAITING
        getAuthor().getInternalAccount().unBlock(amount);
        setState(State.CANCELED);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public State getState() {
        return state;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected Demand getDemand() {
        return demand;
    }

    protected void setDemand(Demand demand) {
        this.demand = demand;
    }

    protected void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    protected void setState(State state) {
        this.state = state;
    }

    protected void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

}
