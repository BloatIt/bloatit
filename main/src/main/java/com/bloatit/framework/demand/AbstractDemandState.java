package com.bloatit.framework.demand;

import java.math.BigDecimal;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.common.WrongDemandStateException;
import com.bloatit.framework.Member;
import com.bloatit.framework.Offer;
import com.bloatit.model.data.DaoDemand.DemandState;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

abstract class AbstractDemandState {

    protected final Demand demand;

    public AbstractDemandState(Demand demand) {
        this.demand = demand;
    }

    public DemandState getState() {
        return demand.getDao().getDemandState();
    }

    public AbstractDemandState eventAddOffer(Offer offer) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventRemoveOffer(Offer offer) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventAddContribution(final Member member, final BigDecimal amount, final String comment) throws NotEnoughMoneyException, UnauthorizedOperationException {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventSelectedOfferTimeOut(BigDecimal contribution) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState developmentTimeOut() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventDevelopmentFinish() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventDeveloperCanceled() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventRejected() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState closeIncome(boolean accepted) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventPopularityPending() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState popularityValidated() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

}
