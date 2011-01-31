package com.bloatit.model.demand;

import com.bloatit.model.Offer;

public class PendingState extends CanContributeMetaState {

    public PendingState(final Demand demand) {
        super(demand);
        demand.inPendingState();
    }

    @Override
    public AbstractDemandState eventAddOffer(final Offer offer) {
        demand.setSelectedOffer(offer);
        return new PreparingState(demand);
    }

    @Override
    protected AbstractDemandState notifyAddContribution() {
        return this;
    }
}