package com.bloatit.model.demand;

public class DiscardedState extends AbstractDemandState {
    public DiscardedState(final DemandImplementation demand) {
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
