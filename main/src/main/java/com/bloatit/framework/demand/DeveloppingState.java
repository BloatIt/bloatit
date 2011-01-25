package com.bloatit.framework.demand;

public class DeveloppingState extends AbstractDemandState {
    public DeveloppingState(final Demand demand) {
        super(demand);
        demand.inDevelopmentState();
    }

    @Override
    public AbstractDemandState eventDevelopmentFinish() {
        return new IncomeState(demand);
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demand);
    }
}
