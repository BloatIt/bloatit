package com.bloatit.framework.demand;

import java.math.BigDecimal;

import com.bloatit.common.WrongDemandStateException;
import com.bloatit.framework.Offer;
import com.bloatit.model.data.DaoDemand.DemandState;

abstract class AbstractDemandState {

    protected final Demand demand;

    public AbstractDemandState(final Demand demand) {
        this.demand = demand;
    }

    public DemandState getState() {
        return demand.getDao().getDemandState();
    }

    public AbstractDemandState eventAddOffer(final Offer offer) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventRemoveOffer(final Offer offer) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventAddContribution() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState eventSelectedOfferTimeOut(final BigDecimal contribution) {
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

    public AbstractDemandState closeIncome(final boolean accepted) {
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
