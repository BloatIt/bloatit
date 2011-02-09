package com.bloatit.model.demand;

public class IncomeState extends AbstractDemandState {
    public IncomeState(final DemandImplementation demand) {
        super(demand);
        demand.inIncomeState();
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demand);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.demand.AbstractDemandState#eventBatchIsRejected()
     */
    @Override
    public AbstractDemandState eventBatchIsRejected() {
        return new DeveloppingState(demand);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.demand.AbstractDemandState#eventBatchIsValidated()
     */
    @Override
    public AbstractDemandState eventBatchIsValidated() {
        return new DeveloppingState(demand);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.demand.AbstractDemandState#eventOfferIsValidated()
     */
    @Override
    public AbstractDemandState eventOfferIsValidated() {
        return new FinishedState(demand);
    }

}