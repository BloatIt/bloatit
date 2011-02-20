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
package com.bloatit.model.managers;

import com.bloatit.data.DaoHighlightDemand;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.HighlightDemand;
import com.bloatit.model.lists.HighlightDemandList;

/**
 * The Class HighlightDemandManager is an utility class containing static methods for
 * {@link HighlightDemand} loading etc.
 */
public final class HighlightDemandManager {

    /**
     * Desactivated constructor on utility class.
     */
    private HighlightDemandManager() {
        // Desactivate default ctor
    }

    /**
     * Gets a {@link HighlightDemand} by its id.
     * 
     * @param id the id
     * @return the {@link HighlightDemand} or null if not found.
     */
    public static HighlightDemand getById(final Integer id) {
        return HighlightDemand.create(DBRequests.getById(DaoHighlightDemand.class, id));
    }

    /**
     * Gets the all th {@link HighlightDemand}s.
     * 
     * @return the {@link HighlightDemand} demands.
     */
    public static PageIterable<HighlightDemand> getHightlightDemand() {
        return new HighlightDemandList(DBRequests.getAll(DaoHighlightDemand.class));
    }
}
