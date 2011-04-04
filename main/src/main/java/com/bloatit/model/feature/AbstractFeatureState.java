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
package com.bloatit.model.feature;

import java.math.BigDecimal;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.exceptions.lowlevel.WrongStateException;
import com.bloatit.model.Offer;

/**
 * The Class AbstractFeatureState implement the Abstract base class of the State
 * design pattern. Each method in it is an event that can change the
 * featureState. Each sub-class is a state.
 */
abstract class AbstractFeatureState {

    /** The feature. */
    protected final FeatureImplementation feature;

    /**
     * Instantiates a new abstract feature state.
     *
     * @param feature the feature on which this state apply.
     */
    public AbstractFeatureState(final FeatureImplementation feature) {
        this.feature = feature;
    }

    /**
     * Gets the featureState associated with this state.
     *
     * @return the state
     */
    public abstract FeatureState getState();

    /**
     * Event add offer. Called when an offer is add to the feature.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     */
    public AbstractFeatureState eventAddOffer() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event remove offer.
     *
     * @param offer the offer
     * @return the state object representing the featureState, after receiving
     *         this event
     */
    public AbstractFeatureState eventRemoveOffer(final Offer offer) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event add contribution.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     */
    public AbstractFeatureState eventAddContribution() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event selected offer time out.
     *
     * @param contribution the contribution
     * @return the state object representing the featureState, after receiving
     *         this event
     */
    public AbstractFeatureState eventSelectedOfferTimeOut(final BigDecimal contribution) {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event development time out.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     */
    public AbstractFeatureState eventDevelopmentTimeOut() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event developer canceled.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     * @throws WrongStateException if this event occurs whereas the feature was
     *             not in the right state (For example you cannot Finish a
     *             development if you are not in development state).
     */
    public AbstractFeatureState eventDeveloperCanceled() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event feature rejected.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     * @throws WrongStateException if this event occurs whereas the feature was
     *             not in the right state (For example you cannot Finish a
     *             development if you are not in development state).
     */
    public AbstractFeatureState eventFeatureRejected() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event popularity pending.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     * @throws WrongStateException if this event occurs whereas the feature was
     *             not in the right state (For example you cannot Finish a
     *             development if you are not in development state).
     */
    public AbstractFeatureState eventPopularityPending() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Popularity validated.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     * @throws WrongStateException if this event occurs whereas the feature was
     *             not in the right state (For example you cannot Finish a
     *             development if you are not in development state).
     */
    public AbstractFeatureState popularityValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event milestone released.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     * @throws WrongStateException if this event occurs whereas the feature was
     *             not in the right state (For example you cannot Finish a
     *             development if you are not in development state).
     */
    public AbstractFeatureState eventMilestoneReleased() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event milestone is rejected.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     * @throws WrongStateException if this event occurs whereas the feature was
     *             not in the right state (For example you cannot Finish a
     *             development if you are not in development state).
     */
    public AbstractFeatureState eventMilestoneIsRejected() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event milestone is validated.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     * @throws WrongStateException if this event occurs whereas the feature was
     *             not in the right state (For example you cannot Finish a
     *             development if you are not in development state).
     */
    public AbstractFeatureState eventMilestoneIsValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

    /**
     * Event offer is validated is called when every milestonees are finished.
     *
     * @return the state object representing the featureState, after receiving
     *         this event
     * @throws WrongStateException if this event occurs whereas the feature was
     *             not in the right state (For example you cannot Finish a
     *             development if you are not in development state).
     */
    public AbstractFeatureState eventOfferIsValidated() {
        // Implement me if you wish.
        throw new WrongStateException();
    }

}
