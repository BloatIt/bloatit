package com.bloatit.model.demand;

public class FinishedState extends AbstractDemandState {
    public FinishedState(final Demand demand) {
        super(demand);
        demand.inFinishedState();
    }
}
