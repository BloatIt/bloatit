package com.bloatit.model.demand;

public class DeveloppingState extends AbstractDemandState {
    public DeveloppingState(final DemandImplementation demandImplementation) {
        super(demandImplementation);
        demandImplementation.inDevelopmentState();
    }

    @Override
    public AbstractDemandState eventBatchReleased() {
        return new IncomeState(demandImplementation);
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demandImplementation);
    }

    /* (non-Javadoc)
     * @see com.bloatit.model.demand.AbstractDemandState#eventDevelopmentTimeOut()
     */
    @Override
    public AbstractDemandState eventDevelopmentTimeOut() {
        // TODO: make Penality.
        return this;
    }


}
