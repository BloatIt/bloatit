package com.bloatit.model;

import java.util.HashMap;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.data.IdentifiableInterface;

/**
 * The cache manager is the class responsible of the model level cache. There is a cache
 * different on each thread.
 * 
 * @author Thomas Guyard
 * 
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
            @SuppressWarnings("synthetic-access")
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
        Log.model().trace("Model cache cleared.");
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
        Log.model().trace("Add into model cache: " + id);
        return identifiable;
    }

    /**
     * Get a class from the cache using its identifant.
     * 
     * @param identifiable is the id of the object you are looking for.
     * @return the object if it exist, null otherwise.
     */
    @SuppressWarnings("rawtypes")
    public static Identifiable get(final IdentifiableInterface identifiable) {
        Log.model().trace("Get from model cache: " + identifiable.getId());
        return UniqueThreadCache.getCurrentCache().get(identifiable.getId());
    }

}
