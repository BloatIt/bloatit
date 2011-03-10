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

import com.bloatit.data.DaoMilestone;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.Milestone;
import com.bloatit.model.lists.MilestoneList;

/**
 * The Class MilestoneManager. Utility class containing static methods to get
 * {@link Milestone}s from the DB.
 */
public final class MilestoneManager {

    /**
     * Desactivated constructor on utility class.
     */
    private MilestoneManager() {
        // Desactivate default ctor
    }

    /**
     * Gets the a milestone by Id.
     *
     * @param id the milestone id.
     * @return the milestone or null if not found.
     */
    public static Milestone getById(final Integer id) {
        return Milestone.create(DBRequests.getById(DaoMilestone.class, id));
    }

    /**
     * @return
     */
    public static MilestoneList getAll() {
        return new MilestoneList(DBRequests.getAll(DaoMilestone.class));
    }

}
