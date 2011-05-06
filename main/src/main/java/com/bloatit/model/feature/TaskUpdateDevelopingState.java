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
import com.bloatit.model.FeatureImplementation;
import com.bloatit.model.PlannedTask;

/**
 * <p>
 * This is a planned task. It cannot store object from the Model layer (it would
 * introduce multithred bugs)
 * </p>
 * <p>
 * Tells that the selected offer of a specified feature is validated (and may
 * begin the Development)
 * </p>
 */
public class TaskUpdateDevelopingState extends PlannedTask {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5639581628713974313L;

    /** The id. */
    private final int id;

    /**
     * Instantiates a new task selected offer time out.
     * 
     * @param id the id
     * @param time the date when to run this task.
     */
    public TaskUpdateDevelopingState(final int id, final Date time) {
        super(time, id);
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.PlannedTask#doRun()
     */
    @Override
    public void doRun() {
        try {
            final FeatureImplementation feature = FeatureManager.getFeatureImplementationById(id);
            if (feature != null) {
                feature.updateDevelopmentState();
            } else {
                Log.model().fatal("Cannot perform the selectedOfferTimeOut. FeatureImplementation not found: " + id);
            }

        } catch (final WrongStateException e) {
            Log.model().fatal("Wrong state when trying to perform the selectedOfferTimeOut", e);
        }
    }

}
