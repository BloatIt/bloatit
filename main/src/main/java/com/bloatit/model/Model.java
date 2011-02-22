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
package com.bloatit.model;

import com.bloatit.common.Log;
import com.bloatit.data.DataManager;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.DemandList;
import com.bloatit.model.demand.TaskUpdateDevelopingState;

public class Model implements AbstractModel {
    public Model() {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#launch()
     */
    @Override
    public void init() {
        Log.model().trace("Launching the Model.");
        
        // Find the demand with selected offer that should pass into validated.
        PageIterable<Demand> demandsToValidate = new DemandList(DBRequests.demandsThatShouldBeValidated());
        for (Demand demand : demandsToValidate) {
            demand.updateDevelopmentState();
        }
        
        // Find the demand with selected offer that should pass into validated.
        PageIterable<Demand> demandsToValidateInTheFuture = new DemandList(DBRequests.demandsThatShouldBeValidatedInTheFuture());
        for (Demand demand : demandsToValidateInTheFuture) {
            new TaskUpdateDevelopingState(demand.getId(), demand.getValidationDate());
        }
        
        
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#shutdown()
     */
    @Override
    public void shutdown() {
        Log.model().trace("Shutdowning the Model.");
        DataManager.shutdown();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#openReadOnly()
     */
    @Override
    public void setReadOnly() {
        Log.model().trace("This transaction is Read Only.");
        DataManager.setReadOnly();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#open()
     */
    @Override
    public void open() {
        Log.model().trace("Open a new transaction.");
        DataManager.open();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#close()
     */
    @Override
    public void close() {
        Log.model().trace("Close the current transaction.");
        CacheManager.clear();
        DataManager.close();
    }
}
