package com.bloatit.framework.demand;

import com.bloatit.model.data.DaoDemand.DemandState;

public class IncomeState extends AbstractDemandState {
    public IncomeState(Demand demand) {
        super(demand);
        demand.getDao().setDemandState(DemandState.INCOME);
    }

    @Override
    public AbstractDemandState closeIncome(boolean accepted) {
        if (accepted) {
            return new FinishedState(demand);
        }
        return new DeveloppingState(demand);
    }

    @Override
    public AbstractDemandState developerCanceled() {
        return new DiscardedState(demand);
    }

}