package com.bloatit.framework.demand;

import com.bloatit.framework.Offer;
import com.bloatit.model.data.DaoDemand.DemandState;

public class PendingState extends AbstractDemandState {

    public PendingState(Demand demand) {
        super(demand);
        demand.getDao().setDemandState(DemandState.PENDING);
    }

    @Override
    public AbstractDemandState addOffer(Offer offer) {
        demand.setSelectedOffer(offer);
        return new PreparingState(demand);
    }
}