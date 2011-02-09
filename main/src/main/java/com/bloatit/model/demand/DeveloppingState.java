package com.bloatit.model.demand;

public class DeveloppingState extends AbstractDemandState {
    public DeveloppingState(final DemandImplementation demand) {
        super(demand);
        demand.inDevelopmentState();
    }

    @Override
    public AbstractDemandState eventBatchReleased() {
        return new IncomeState(demand);
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demand);
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
