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

import com.bloatit.data.DaoBug;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Bug;
import com.bloatit.model.lists.BugList;

/**
 * The Class BugManager. Utility class containing static methods to get
 * {@link Bug}s from the DB.
 */
public final class BugManager {

    /**
     * Desactivated constructor on utility class.
     */
    private BugManager() {
        // Desactivate default ctor
    }

    /**
     * Gets a Bug by id.
     *
     * @param id the {@link Bug} id
     * @return the bug or null if not found.
     */
    public static Bug getById(final Integer id) {
        return Bug.create(DBRequests.getById(DaoBug.class, id));
    }

    public static PageIterable<Bug> getAll() {
        return new BugList(DBRequests.getAll(DaoBug.class));
    }

}
