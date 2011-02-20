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

/**
 * The Class AbstractDemandState implement the Abstract base class of the State design
 * pattern. Each method in it is an event that can change the demandState. Each sub-class
 * is a state.
 */
abstract class AbstractDemandState {

    /** The demand. */
    protected final DemandImplementation demand;

    /**
     * Instantiates a new abstract demand state.
     * 
     * @param demand the demand on which this state apply.
     */
    public AbstractDemandState(final DemandImplementation demand) {
        this.demand = demand;
    }

    /**
     * Gets the demandState associated with this state.
     * 
     * @return the state
     */
    public DemandState getState() {
        return demand.getDao().getDemandState();
    }

    /**
     * Event add offer. Called when an offer is add to the demand.
     * 
     * @param offer the offer
     * @return the state object representing the demandState, after recieving this event
     */
    public AbstractDemandState eventAddOffer(final Offer offer) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event remove offer.
     * 
     * @param offer the offer
     * @return the state object representing the demandState, after recieving this event
     */
    public AbstractDemandState eventRemoveOffer(final Offer offer) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event add contribution.
     * 
     * @return the state object representing the demandState, after recieving this event
     */
    public AbstractDemandState eventAddContribution() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event selected offer time out.
     * 
     * @param contribution the contribution
     * @return the state object representing the demandState, after recieving this event
     */
    public AbstractDemandState eventSelectedOfferTimeOut(final BigDecimal contribution) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event development time out.
     * 
     * @return the state object representing the demandState, after receiving this event
     */
    public AbstractDemandState eventDevelopmentTimeOut() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event developer canceled.
     * 
     * @return the state object representing the demandState, after receiving this event
     * @throws WrongStateException if this event occurs whereas the demand was not in the
     *         right state (For example you cannot Finish a development if you are not in
     *         development state).
     */
    public AbstractDemandState eventDeveloperCanceled() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event demand rejected.
     * 
     * @return the state object representing the demandState, after receiving this event
     * @throws WrongStateException if this event occurs whereas the demand was not in the
     *         right state (For example you cannot Finish a development if you are not in
     *         development state).
     */
    public AbstractDemandState eventDemandRejected() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event popularity pending.
     * 
     * @return the state object representing the demandState, after recieving this event
     * @throws WrongStateException if this event occurs whereas the demand was not in the
     *         right state (For example you cannot Finish a development if you are not in
     *         development state).
     */
    public AbstractDemandState eventPopularityPending() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Popularity validated.
     * 
     * @return the state object representing the demandState, after recieving this event
     * @throws WrongStateException if this event occurs whereas the demand was not in the
     *         right state (For example you cannot Finish a development if you are not in
     *         development state).
     */
    public AbstractDemandState popularityValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event batch released.
     * 
     * @return the state object representing the demandState, after recieving this event
     * @throws WrongStateException if this event occurs whereas the demand was not in the
     *         right state (For example you cannot Finish a development if you are not in
     *         development state).
     */
    public AbstractDemandState eventBatchReleased() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event batch is rejected.
     * 
     * @return the state object representing the demandState, after recieving this event
     * @throws WrongStateException if this event occurs whereas the demand was not in the
     *         right state (For example you cannot Finish a development if you are not in
     *         development state).
     */
    public AbstractDemandState eventBatchIsRejected() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event batch is validated.
     * 
     * @return the state object representing the demandState, after recieving this event
     * @throws WrongStateException if this event occurs whereas the demand was not in the
     *         right state (For example you cannot Finish a development if you are not in
     *         development state).
     */
    public AbstractDemandState eventBatchIsValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event offer is validated is called when every batches are finished.
     * 
     * @return the state object representing the demandState, after recieving this event
     * @throws WrongStateException if this event occurs whereas the demand was not in the
     *         right state (For example you cannot Finish a development if you are not in
     *         development state).
     */
    public AbstractDemandState eventOfferIsValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

}
