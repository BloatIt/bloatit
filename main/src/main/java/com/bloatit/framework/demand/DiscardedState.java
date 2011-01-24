package com.bloatit.framework.demand;


public class DiscardedState extends AbstractDemandState {
    public DiscardedState(Demand demand) {
        super(demand);
        demand.inDiscardedState();
    }

    @Override
    public AbstractDemandState eventPopularityPending() {
        return new PendingState(demand);
    }

    @Override
    public AbstractDemandState popularityValidated() {
        return new PendingState(demand);
    }

}
