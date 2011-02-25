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

import java.util.HashMap;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.data.DaoIdentifiable;

/**
 * The cache manager is the class responsible of the model level cache. There is
 * a cache different on each thread.
 *
 * @author Thomas Guyard
 */
public final class CacheManager {

    /**
     * Used to have a cache object for each threads.
     *
     * @author Thomas Guyard
     */
    static class UniqueThreadCache {

        @SuppressWarnings("rawtypes")
        private static final ThreadLocal<Map<Integer, Identifiable>> uniqueCache = new ThreadLocal<Map<Integer, Identifiable>>() {
            @Override
            protected Map<Integer, Identifiable> initialValue() {
                return new HashMap<Integer, Identifiable>();
            }
        };

        @SuppressWarnings("rawtypes")
        public static Map<Integer, Identifiable> getCurrentCache() {
            return uniqueCache.get();
        }
    } // UniqueThreadCache

    private CacheManager() {
        // disactivate ctor.
    }

    /**
     * Erase all entries in the cache. This method is threadSafe.
     */
    public static void clear() {
        UniqueThreadCache.getCurrentCache().clear();
        Log.cache().trace("Model cache cleared.");
    }

    /**
     * Add a model class to the cache.
     *
     * @param id is the identifant of the object into the cache.
     * @param identifiable is the object to store into the cache.
     * @return identifiable.
     */
    @SuppressWarnings("rawtypes")
    public static Identifiable add(final Integer id, final Identifiable identifiable) {
        UniqueThreadCache.getCurrentCache().put(id, identifiable);
        Log.cache().trace("Add into model cache: " + id);
        return identifiable;
    }

    /**
     * Get a class from the cache using its identifant.
     *
     * @param identifiable is the id of the object you are looking for.
     * @return the object if it exist, null otherwise.
     */
    @SuppressWarnings("rawtypes")
    public static Identifiable get(final DaoIdentifiable identifiable) {
        Log.cache().trace("Get from model cache: " + identifiable.getId());
        return UniqueThreadCache.getCurrentCache().get(identifiable.getId());
    }

}
