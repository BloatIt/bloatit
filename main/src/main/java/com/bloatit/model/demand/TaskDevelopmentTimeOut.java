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

import java.util.Date;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.WrongStateException;
import com.bloatit.model.PlannedTask;

/**
 * This is a planned task. It cannot store object from the Model layer (it would introduce
 * multithred bugs)
 */
public class TaskDevelopmentTimeOut extends PlannedTask {
    private static final long serialVersionUID = 5639581628713974313L;
    private final int demandId;

    public TaskDevelopmentTimeOut(final int demandId, final Date time) {
        super(time, demandId);
        this.demandId = demandId;
    }

    @Override
    public void doRun() {
        try {
            DemandImplementation demand = DemandManager.getDemandImplementationById(demandId);
            if (demand != null) {
                demand.developmentTimeOut();
            } else {
                Log.framework().fatal("Cannot perform the developmentTimeOut. DemandImplementation not found: " + demandId);
            }
        } catch (final WrongStateException e) {
            Log.model().fatal("Wrong state when trying to perform the developmentTimeOut", e);
        }
    }

}
