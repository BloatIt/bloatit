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
import java.util.Date;

import com.bloatit.data.DaoOffer;

/**
 * The Class CanContributeMetaState is not a real state. Every state that allows
 * to contribute should inherit from it.
 */
public abstract class CanContributeMetaState extends AbstractFeatureState {

    /**
     * Instantiates a new can contribute meta state.
     * 
     * @param feature the feature on which this state apply.
     */
    protected CanContributeMetaState(final FeatureImplementation feature) {
        super(feature);
    }

    /**
     * Notify that a new contribution arrived. This method is called each time a
     * new contribution is done on the feature.
     * 
     * @return the abstract feature state
     */
    protected abstract AbstractFeatureState notifyAddContribution();

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.feature.AbstractFeatureState#eventAddContribution()
     */
    @Override
    public final AbstractFeatureState eventAddContribution() {
        return notifyAddContribution();
    }

    /**
     * Test if the current feature should pass in DevelopingState. To pass in
     * {@link DevelopingState} state we have to have a selected offer, enough
     * contribution and the validation period spent.
     * 
     * @return the abstract feature state (Developing or this.)
     */
    protected final AbstractFeatureState handleEvent() {
        final BigDecimal contribution = feature.getDao().getContribution();
        final DaoOffer selectedOffer = feature.getDao().getSelectedOffer();
        final Date validationDate = feature.getDao().getValidationDate();
        if (selectedOffer != null && validationDate != null && contribution.compareTo(selectedOffer.getAmount()) >= 0
                && new Date().after(validationDate)) {
            return new DevelopingState(feature);
        }
        return this;
    }

}
