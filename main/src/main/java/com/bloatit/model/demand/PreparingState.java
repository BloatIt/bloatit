package com.bloatit.model.demand;

import java.math.BigDecimal;

import com.bloatit.model.Offer;

public class PreparingState extends CanContributeMetaState {
    public PreparingState(final DemandImplementation demandImplementation) {
        super(demandImplementation);
        demandImplementation.inPreparingState();
    }

    @Override
    public AbstractDemandState eventAddOffer(final Offer offer) {
        return this;
    }

    @Override
    public AbstractDemandState eventRemoveOffer(final Offer offer) {
        if (demandImplementation.getDao().getOffers().size() > 0) {
            return this;
        }
        return new PendingState(demandImplementation);
    }

    @Override
    public AbstractDemandState eventSelectedOfferTimeOut(final BigDecimal contribution) {
        return handleEvent();
    }

    @Override
    public AbstractDemandState notifyAddContribution() {
        return handleEvent();
    }
}