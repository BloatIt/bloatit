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
 * The Class PendingState.
 */
public class PendingState extends CanContributeMetaState {

    /**
     * Instantiates a new pending state.
     *
     * @param demand the demand on which this state apply.
     */
    public PendingState(final DemandImplementation demand) {
        super(demand);
        demand.inPendingState();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.demand.AbstractDemandState#eventAddOffer(com.bloatit
     * .model.Offer)
     */
    @Override
    public AbstractDemandState eventAddOffer() {
        return new PreparingState(demand);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.demand.CanContributeMetaState#notifyAddContribution()
     */
    @Override
    protected AbstractDemandState notifyAddContribution() {
        return this;
    }
}
