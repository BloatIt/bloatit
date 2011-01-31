package com.bloatit.model.demand;

import java.math.BigDecimal;

import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.framework.exceptions.WrongStateException;
import com.bloatit.model.Offer;

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
        throw new WrongStateException();
    }

    public AbstractDemandState eventRemoveOffer(final Offer offer) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventAddContribution() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventSelectedOfferTimeOut(final BigDecimal contribution) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState developmentTimeOut() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventDeveloperCanceled() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventDemandRejected() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventPopularityPending() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState popularityValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventBatchReleased() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventBatchIsRejected() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventBatchIsValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    // When every batches are finished
    public AbstractDemandState eventOfferIsValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

}
