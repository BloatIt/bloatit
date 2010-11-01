package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.bloatit.common.FatalErrorException;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

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

    // TODO add the possibility to add some text (144 c for auto tweet ?)

    public DaoContribution() {}

    // the demand is associated into the DaoDemand class by hibernate.
    public DaoContribution(DaoMember member, BigDecimal amount) {
        super(member);
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            throw new FatalErrorException("The amount of a contribution cannot be <= 0.", null);
        }
        this.amount = amount;
        setState(State.WAITING);
        getAuthor().getInternalAccount().block(amount);
    }

    public void accept(DaoOffer Offer) {
        // TODO verify that the state is WAITING
        try {
            transaction = DaoTransaction.createAndPersist(getAuthor().getInternalAccount(), Offer.getAuthor().getInternalAccount(), amount);
            getAuthor().getInternalAccount().unBlock(amount);
            setState(State.ACCEPTED);
        } catch (final NotEnoughMoneyException e) {
            cancel();
            e.printStackTrace(); // TODO do something more constructive ...
        }
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
