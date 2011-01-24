package com.bloatit.framework.demand;


public class DeveloppingState extends AbstractDemandState {
    public DeveloppingState(Demand demand) {
        super(demand);
        demand.inDevelopmentState();
    }

    @Override
    public AbstractDemandState eventDevelopmentFinish() {
        return new FinishedState(demand);
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demand);
    }
}
