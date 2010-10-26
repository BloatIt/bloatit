package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class DaoContribution extends DaoUserContent {

    public enum State {
        WAITING, ACCEPTED, CANCELED
    }

    @Basic(optional = false)
    private BigDecimal amount;

    @ManyToOne
    private DaoDemand demand;

    @Basic(optional = false)
    @Enumerated
    private State state;

    @OneToOne(optional = true)
    private DaoTransaction transaction;

    public DaoContribution() {}

    // the demand is associated into the DaoDemand class by hibernate.
    public DaoContribution(DaoActor Actor, BigDecimal amount) {
        // TODO make sure amount > 0
        super(Actor);
        this.amount = amount;
        this.setState(State.WAITING);
        getAuthor().getInternalAccount().block(amount);
    }

    public void accept(DaoOffer Offer) {
        // TODO verify that the state is WAITING
        getAuthor().getInternalAccount().unBlock(amount);
        transaction = DaoTransaction.createAndPersist(getAuthor().getInternalAccount(), Offer.getAuthor().getInternalAccount(), amount);
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

    public DaoTransaction getTransaction() {
        return transaction;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoDemand getDemand() {
        return demand;
    }

    protected void setDemand(DaoDemand Demand) {
        this.demand = Demand;
    }

    protected void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    protected void setState(State state) {
        this.state = state;
    }

    protected void setTransaction(DaoTransaction Transaction) {
        this.transaction = Transaction;
    }

}
