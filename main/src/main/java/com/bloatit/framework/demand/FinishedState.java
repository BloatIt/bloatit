package com.bloatit.framework.demand;

import com.bloatit.model.data.DaoDemand.DemandState;

public class FinishedState extends AbstractDemandState {
    public FinishedState(Demand demand) {
        super(demand);
        demand.getDao().setDemandState(DemandState.FINISHED);
    }
}
