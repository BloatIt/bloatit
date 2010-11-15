/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.managers;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.AuthToken;
import com.bloatit.framework.Demand;
import com.bloatit.framework.lists.DemandList;
import com.bloatit.model.data.DBRequests;
import com.bloatit.model.data.DaoDemand;

public class DemandManager {

    public static PageIterable<Demand> getDemands() {
        return new DemandList(DBRequests.getAll(DaoDemand.class));
    }

    public static Demand getDemandById(Integer id) {
        return Demand.create(DBRequests.getById(DaoDemand.class, id));
    }

    public static int getDemandsCount() {
        return DBRequests.count(DaoDemand.class);
    }

    public static PageIterable<Demand> search(String searchString) {
        return new DemandList(DBRequests.searchDemands(searchString));
    }

    public static boolean canCreate(AuthToken authToken) {
        //TODO: set right right
        return true;
    }

}
