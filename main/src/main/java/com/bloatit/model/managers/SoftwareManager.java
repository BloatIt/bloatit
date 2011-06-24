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

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Software;
import com.bloatit.model.lists.SoftwareList;

/**
 * The Class SoftwareManager is an utility class containing static methods.
 */
public final class SoftwareManager {



    /**
     * Desactivated constructor on utility class.
     */
    private SoftwareManager() {
        // Desactivate default ctor
    }

    /**
     * Gets the software by id.
     * 
     * @param id the id
     * @return the software or null if not found.
     */
    public static Software getById(final Integer id) {
        return Software.create(DBRequests.getById(DaoSoftware.class, id));
    }

    /**
     * Gets all the softwares.
     * 
     * @return all the softwares
     */
    public static PageIterable<Software> getAll() {
        return new SoftwareList(DBRequests.getAll(DaoSoftware.class));
    }
    
    /**
     * Tells if a name exists.
     *
     * @param name the name
     * @return <code>true</code>, if it exists, <code>false</code> otherwise.
     */
    public static boolean nameExists(final String name) {
        return DaoSoftware.nameExists(name);
    }

    public static Software getByName(String name) {
        return Software.create(DaoSoftware.getByName(name));
    }
}
