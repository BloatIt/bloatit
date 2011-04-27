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

import com.bloatit.data.DaoFeature.FeatureState;

/**
 * The Class FinishedState. It is the final state. You cannot change of state
 * here (no event method implemented).
 */
public class FinishedState extends AbstractFeatureState {

    /**
     * Instantiates a new finished state.
     * 
     * @param feature the feature on which this state apply.
     */
    protected FinishedState(final FeatureImplementation feature) {
        super(feature);
        feature.setFeatureStateUnprotected(getState());
    }

    @Override
    public final FeatureState getState() {
        return FeatureState.FINISHED;
    }
}
