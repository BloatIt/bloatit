package com.bloatit.framework.demand;

public class IncomeState extends AbstractDemandState {
    public IncomeState(final Demand demand) {
        super(demand);
        demand.inIncomeState();
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demand);
    }

}