package com.bloatit.model.demand;

import com.bloatit.model.Offer;

public class PendingState extends CanContributeMetaState {

    public PendingState(final DemandImplementation demandImplementation) {
        super(demandImplementation);
        demandImplementation.inPendingState();
    }

    @Override
    public AbstractDemandState eventAddOffer(final Offer offer) {
        demandImplementation.setSelectedOffer(offer);
        return new PreparingState(demandImplementation);
    }

    @Override
    protected AbstractDemandState notifyAddContribution() {
        return this;
    }
}