//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model.demand;

/**
 * The Class IncomeState.
 */
public class IncomeState extends AbstractDemandState {

    /**
     * Instantiates a new income state.
     * 
     * @param demand the demand on which this state apply.
     */
    public IncomeState(final DemandImplementation demand) {
        super(demand);
        demand.inIncomeState();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.demand.AbstractDemandState#eventDeveloperCanceled()
     */
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
        return new DevelopingState(demand);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.demand.AbstractDemandState#eventBatchIsValidated()
     */
    @Override
    public AbstractDemandState eventBatchIsValidated() {
        return new DevelopingState(demand);
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
