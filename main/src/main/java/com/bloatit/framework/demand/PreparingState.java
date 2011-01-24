package com.bloatit.framework.demand;

import java.math.BigDecimal;

import com.bloatit.framework.Offer;

public class PreparingState extends CanContributeMetaState {
    public PreparingState(Demand demand) {
        super(demand);
        demand.inPreparingState();
    }

    @Override
    public AbstractDemandState eventAddOffer(Offer offer) {
        demand.setSelectedOffer(offer);
        return this;
    }

    @Override
    public AbstractDemandState eventRemoveOffer(Offer offer) {
        if (demand.getDao().getOffers().size() > 0) {
            return this;
        }
        return new PendingState(demand);
    }

    @Override
    public AbstractDemandState eventSelectedOfferTimeOut(BigDecimal contribution) {
        return handleEvent();
    }

    @Override
    public AbstractDemandState notifyAddContribution() {
        return handleEvent();
    }
}