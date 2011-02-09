package com.bloatit.model.demand;

public class DiscardedState extends AbstractDemandState {
    public DiscardedState(final DemandImplementation demandImplementation) {
        super(demandImplementation);
        demandImplementation.inDiscardedState();
    }

    @Override
    public AbstractDemandState eventPopularityPending() {
        return new PendingState(demandImplementation);
    }

    @Override
    public AbstractDemandState popularityValidated() {
        return new PendingState(demandImplementation);
    }

}
