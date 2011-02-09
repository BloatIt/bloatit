package com.bloatit.model.demand;

public class IncomeState extends AbstractDemandState {
    public IncomeState(final DemandImplementation demandImplementation) {
        super(demandImplementation);
        demandImplementation.inIncomeState();
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demandImplementation);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.demand.AbstractDemandState#eventBatchIsRejected()
     */
    @Override
    public AbstractDemandState eventBatchIsRejected() {
        return new DeveloppingState(demandImplementation);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.demand.AbstractDemandState#eventBatchIsValidated()
     */
    @Override
    public AbstractDemandState eventBatchIsValidated() {
        return new DeveloppingState(demandImplementation);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.demand.AbstractDemandState#eventOfferIsValidated()
     */
    @Override
    public AbstractDemandState eventOfferIsValidated() {
        return new FinishedState(demandImplementation);
    }

}