package com.bloatit.framework.demand;

public class IncomeState extends AbstractDemandState {
    public IncomeState(final Demand demand) {
        super(demand);
        demand.inIncomeState();
    }

    @Override
    public AbstractDemandState closeIncome(final boolean accepted) {
        if (accepted) {
            return new FinishedState(demand);
        }
        return new DeveloppingState(demand);
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demand);
    }

    @Override
    public AbstractDemandState eventCloseBatch(final boolean accepted){
        return new DeveloppingState(demand);
    }

}