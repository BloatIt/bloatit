package com.bloatit.model.demand;

public class FinishedState extends AbstractDemandState {
    public FinishedState(final DemandImplementation demandImplementation) {
        super(demandImplementation);
        demandImplementation.inFinishedState();
    }
}
