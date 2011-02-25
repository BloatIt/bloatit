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

import java.math.BigDecimal;

import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.model.Offer;

// TODO: Auto-generated Javadoc
/**
 * The Class PreparingState.
 */
public class PreparingState extends CanContributeMetaState {

    /**
     * Instantiates a new preparing state.
     *
     * @param demand the demand on which this state apply.
     */
    public PreparingState(final DemandImplementation demand) {
        super(demand);
        demand.setDemandStateUnprotected(getState());
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.demand.AbstractDemandState#eventAddOffer(com.bloatit
     * .model.Offer)
     */
    @Override
    public AbstractDemandState eventAddOffer() {
        return this;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.demand.AbstractDemandState#eventRemoveOffer(com.bloatit
     * .model .Offer)
     */
    @Override
    public AbstractDemandState eventRemoveOffer(final Offer offer) {
        if (demand.getDao().getOffers().size() > 0) {
            return this;
        }
        return new PendingState(demand);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.demand.AbstractDemandState#eventSelectedOfferTimeOut
     * (java.math .BigDecimal)
     */
    @Override
    public AbstractDemandState eventSelectedOfferTimeOut(final BigDecimal contribution) {
        return handleEvent();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.demand.CanContributeMetaState#notifyAddContribution()
     */
    @Override
    public AbstractDemandState notifyAddContribution() {
        return handleEvent();
    }
    
    @Override
    public final DemandState getState() {
        return DemandState.PREPARING;
    }
}
