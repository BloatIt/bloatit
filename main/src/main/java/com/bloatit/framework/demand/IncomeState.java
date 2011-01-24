package com.bloatit.framework.demand;


public class IncomeState extends AbstractDemandState {
    public IncomeState(Demand demand) {
        super(demand);
        demand.inIncomeState();
    }

    @Override
    public AbstractDemandState closeIncome(boolean accepted) {
        if (accepted) {
            return new FinishedState(demand);
        }
        return new DeveloppingState(demand);
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demand);
    }

}