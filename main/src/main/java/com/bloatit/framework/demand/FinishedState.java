package com.bloatit.framework.demand;

public class FinishedState extends AbstractDemandState {
    public FinishedState(final Demand demand) {
        super(demand);
        demand.inFinishedState();
    }
}
