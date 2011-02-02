package com.bloatit.model.demand;

public class DeveloppingState extends AbstractDemandState {
    public DeveloppingState(final Demand demand) {
        super(demand);
        demand.inDevelopmentState();
    }

    @Override
    public AbstractDemandState eventBatchReleased() {
        return new IncomeState(demand);
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demand);
    }
}