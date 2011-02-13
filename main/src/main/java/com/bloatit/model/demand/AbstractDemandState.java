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
import com.bloatit.framework.exceptions.WrongStateException;
import com.bloatit.model.Offer;

abstract class AbstractDemandState {

    protected final DemandImplementation demand;

    public AbstractDemandState(final DemandImplementation demand) {
        this.demand = demand;
    }

    public DemandState getState() {
        return demand.getDao().getDemandState();
    }

    public AbstractDemandState eventAddOffer(final Offer offer) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventRemoveOffer(final Offer offer) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventAddContribution() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventSelectedOfferTimeOut(final BigDecimal contribution) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventDevelopmentTimeOut() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventDeveloperCanceled() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventDemandRejected() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventPopularityPending() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState popularityValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventBatchReleased() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventBatchIsRejected() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    public AbstractDemandState eventBatchIsValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    // When every batches are finished
    public AbstractDemandState eventOfferIsValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

}
