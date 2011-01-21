package com.bloatit.framework.demand;

import com.bloatit.model.data.DaoDemand.DemandState;

public class DeveloppingState extends AbstractDemandState {
    public DeveloppingState(Demand demand) {
        super(demand);
        demand.getDao().setDemandState(DemandState.DEVELOPPING);
    }

    @Override
    public AbstractDemandState developmentFinish() {
        return new FinishedState(demand);
    }

    @Override
    public AbstractDemandState developerCanceled() {
        return new DiscardedState(demand);
    }
}
