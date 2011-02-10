package com.bloatit.model.demand;

public class FinishedState extends AbstractDemandState {

    public FinishedState(final DemandImplementation demand) {
        super(demand);
        demand.inFinishedState();
    }
}
