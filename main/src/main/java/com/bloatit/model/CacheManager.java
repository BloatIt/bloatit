package com.bloatit.model;

import java.util.HashMap;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.data.IdentifiableInterface;

public final class CacheManager {

    static class UniqueThreadCache {

        @SuppressWarnings("rawtypes")
        private static final Map<Integer, Identifiable> cache = new HashMap<Integer, Identifiable>();

        @SuppressWarnings("rawtypes")
        private static final ThreadLocal<Map<Integer, Identifiable>> uniqueCache = new ThreadLocal<Map<Integer, Identifiable>>() {
            @SuppressWarnings("synthetic-access")
            @Override
            protected Map<Integer, Identifiable> initialValue() {
                return cache;
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

    public static void clear() {
        UniqueThreadCache.getCurrentCache().clear();
        Log.model().trace("Model cache cleared.");
    }

    @SuppressWarnings("rawtypes")
    public static Identifiable add(final Integer id, final Identifiable identifiable) {
        UniqueThreadCache.getCurrentCache().put(id, identifiable);
        Log.model().trace("Add into model cache: " + id);
        return identifiable;
    }

    @SuppressWarnings("rawtypes")
    public static Identifiable get(final IdentifiableInterface identifiable) {
        Log.model().trace("Get from model cache: " + identifiable.getId());
        return UniqueThreadCache.getCurrentCache().get(identifiable.getId());
    }

}
