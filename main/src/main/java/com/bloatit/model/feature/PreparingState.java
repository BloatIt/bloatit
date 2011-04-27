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
import com.bloatit.model.Offer;

// TODO: Auto-generated Javadoc
/**
 * The Class PreparingState.
 */
public class PreparingState extends CanContributeMetaState {

    /**
     * Instantiates a new preparing state.
     * 
     * @param feature the feature on which this state apply.
     */
    protected PreparingState(final FeatureImplementation feature) {
        super(feature);
        feature.setFeatureStateUnprotected(getState());
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.feature.AbstractFeatureState#eventAddOffer(com.bloatit
     * .model.Offer)
     */
    @Override
    public AbstractFeatureState eventAddOffer() {
        return this;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.feature.AbstractFeatureState#eventRemoveOffer(com.bloatit
     * .model .Offer)
     */
    @Override
    public AbstractFeatureState eventRemoveOffer(final Offer offer) {
        if (feature.getDao().getOffers().size() > 0) {
            return this;
        }
        return new PendingState(feature);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.feature.AbstractFeatureState#eventSelectedOfferTimeOut
     * (java.math .BigDecimal)
     */
    @Override
    public AbstractFeatureState eventSelectedOfferTimeOut(final BigDecimal contribution) {
        return handleEvent();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.feature.CanContributeMetaState#notifyAddContribution()
     */
    @Override
    public AbstractFeatureState notifyAddContribution() {
        return handleEvent();
    }

    @Override
    public final FeatureState getState() {
        return FeatureState.PREPARING;
    }
}
