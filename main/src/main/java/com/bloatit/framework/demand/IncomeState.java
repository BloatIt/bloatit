package com.bloatit.framework.demand;

public class IncomeState extends AbstractDemandState {
    public IncomeState(final Demand demand) {
        super(demand);
        demand.inIncomeState();
    }

    @Override
    public AbstractDemandState eventDeveloperCanceled() {
        return new DiscardedState(demand);
    }

    /* (non-Javadoc)
     * @see com.bloatit.framework.demand.AbstractDemandState#eventBatchIsRejected()
     */
    @Override
    public AbstractDemandState eventBatchIsRejected() {
        return new DeveloppingState(demand);
    }

    /* (non-Javadoc)
     * @see com.bloatit.framework.demand.AbstractDemandState#eventBatchIsValidated()
     */
    @Override
    public AbstractDemandState eventBatchIsValidated() {
        return new DeveloppingState(demand);
    }

    /* (non-Javadoc)
     * @see com.bloatit.framework.demand.AbstractDemandState#eventOfferIsValidated()
     */
    @Override
    public AbstractDemandState eventOfferIsValidated() {
        return new FinishedState(demand);
    }



}