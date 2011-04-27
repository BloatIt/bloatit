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

import com.bloatit.data.DaoIdentifiable;

/**
 * Helper class to easily create identifiable object from their Dao
 * representation.
 * 
 * @param <T> is the Type of object we want to create.
 * @param <DAO> is the Dao version of <code>T</code>.
 */
public abstract class Creator<DAO extends DaoIdentifiable, T extends Identifiable<DAO>> {

    public Creator() {
        // nothing to do.
    }

    /**
     * Create a Identifiable using its dao representation. If a correspondig
     * object is found in the cache, then there is no creation and the cached
     * object is returned.
     * 
     * @param dao is the dao version of the created object from the model layer
     * @return the created object or the object form the cache or null if not
     *         found.
     */
    @SuppressWarnings("unchecked")
    public T create(final DAO dao) {
        if (dao != null) {
            final Identifiable<?> created = CacheManager.get(dao);
            if (created == null) {
                return doCreate(dao);
            }
            return (T) created;
        }
        return null;
    }

    /**
     * Abstract method to do the "new T(dao)", because generics are not
     * templates.
     * 
     * @param dao
     * @return the new T
     */
    public abstract T doCreate(DAO dao);
}
