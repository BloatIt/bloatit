package com.bloatit.framework.demand;

import java.math.BigDecimal;

import com.bloatit.common.WrongStateException;
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

    public AbstractDemandState eventRejected() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState closeIncome(final boolean accepted) {
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

    public AbstractDemandState eventCloseBatch(boolean accepted) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventBatchDevelopmentFinished() {
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

    public AbstractDemandState eventOfferIsValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

}
