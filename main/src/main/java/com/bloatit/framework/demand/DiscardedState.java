package com.bloatit.framework.demand;

import com.bloatit.model.data.DaoDemand.DemandState;

public class DiscardedState extends AbstractDemandState {
    public DiscardedState(Demand demand) {
        super(demand);
        demand.getDao().setDemandState(DemandState.DISCARDED);
    }

    @Override
    public AbstractDemandState popularityPending() {
        return new PendingState(demand);
    }

    @Override
    public AbstractDemandState popularityValidated() {
        return new PendingState(demand);
    }

}
