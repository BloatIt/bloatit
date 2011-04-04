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

import java.util.Date;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.lowlevel.WrongStateException;
import com.bloatit.model.PlannedTask;

/**
 * <p>
 * This is a planned task. It cannot store object from the Model layer (it would
 * introduce multithred bugs)
 * </p>
 * <p>
 * Tells that the current development of a specified feature should be finish
 * (the expiration date is reached).
 * </p>
 */
public class TaskDevelopmentTimeOut extends PlannedTask {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5639581628713974313L;

    /** The feature id. */
    private final int featureId;

    /**
     * Instantiates a new task development time out.
     *
     * @param featureId the feature id on which we will have to perform a
     *            "development time out".
     * @param time the date when this task will be run.
     */
    public TaskDevelopmentTimeOut(final int featureId, final Date time) {
        super(time, featureId);
        this.featureId = featureId;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.PlannedTask#doRun()
     */
    @Override
    public void doRun() {
        try {
            final FeatureImplementation feature = FeatureManager.getFeatureImplementationById(featureId);
            if (feature != null) {
                feature.developmentTimeOut();
            } else {
                Log.framework().fatal("Cannot perform the developmentTimeOut. FeatureImplementation not found: " + featureId);
            }
        } catch (final WrongStateException e) {
            Log.model().fatal("Wrong state when trying to perform the developmentTimeOut", e);
        }
    }

}
