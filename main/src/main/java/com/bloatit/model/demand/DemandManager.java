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

import com.bloatit.data.DBRequests;
import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.AuthToken;
import com.bloatit.model.Demand;

/**
 * The Class DemandManager is a class with only static member. Use it to do some requests
 * on the DB returning demands.
 */
public final class DemandManager {

    /**
     * Desactivate default ctor
     */
    private DemandManager() {
        // Desactivate default ctor
    }

    /**
     * Gets the all demands stored in the DB.
     * 
     * @return the demands
     */
    public static PageIterable<Demand> getDemands() {
        return new DemandList(DBRequests.getAllUserContentOrderByDate(DaoDemand.class));
    }

    /**
     * Gets the demands with a specified state and with on offer selected or not.
     * 
     * @param state the state of the demands to return
     * @param hasSelectedOffer true if you want demands with a selected offer.
     * @return the demands
     */
    public static PageIterable<Demand> getDemands(DemandState state, boolean hasSelectedOffer) {
        // return new DemandList(DBRequests.getDemands(state, null, null, null, null,
        // null, null, null, hasSelectedOffer, null, null, null, null, null,//
        // null,
        // null,
        // null));
        // TODO !!
        return null;
    }

    /**
     * Gets the demand by id.
     * 
     * @param id the id of the demand we are looking for.
     * @return the demand or null if not found.
     */
    public static Demand getDemandById(final Integer id) {
        return getDemandImplementationById(id);
    }

    /**
     * Gets the demandImplementation by id.
     * 
     * @param id the id of the demand
     * @return the demand or null if not found
     */
    static DemandImplementation getDemandImplementationById(final Integer id) {
        return DemandImplementation.create(DBRequests.getById(DaoDemand.class, id));
    }

    /**
     * Gets the number of demand.
     * 
     * @return the number of demand.
     */
    public static int getDemandsCount() {
        return DBRequests.count(DaoDemand.class);
    }

    // Can create if authenticated.
    /**
     * Tells if a user can create a demand.
     * 
     * @param authToken the auth token representing the user wanting to create a demand.
     * @return true, if successful
     */
    public static boolean canCreate(final AuthToken authToken) {
        return authToken != null;
    }

}
