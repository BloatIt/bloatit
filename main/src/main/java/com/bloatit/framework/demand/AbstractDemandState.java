package com.bloatit.framework.demand;

import java.math.BigDecimal;

import com.bloatit.common.WrongDemandStateException;
import com.bloatit.framework.Offer;
import com.bloatit.model.data.DaoDemand.DemandState;

abstract class AbstractDemandState {

    protected final Demand demand;

    public AbstractDemandState(Demand demand) {
        this.demand = demand;
    }

    public DemandState getState() {
        return demand.getDao().getDemandState();
    }

    public AbstractDemandState addOffer(Offer offer) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState removeOffer(Offer offer) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState addContribution(BigDecimal contribution, Offer offer) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState selectedOfferTimeOut(BigDecimal contribution, Offer offer) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState developmentTimeOut() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState developmentFinish() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState developerCanceled() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState popularityTooLow() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState closeIncome(boolean accepted) {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState popularityPending() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

    public AbstractDemandState popularityValidated() {
        // Implement me if you wish.
        throw new WrongDemandStateException();
    }

}
