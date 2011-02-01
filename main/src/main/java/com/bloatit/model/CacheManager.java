package com.bloatit.model;

import java.util.HashMap;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.data.IdentifiableInterface;

public final class CacheManager {

    private CacheManager() {
        // disactivate ctor.
    }

    @SuppressWarnings("rawtypes")
    private static final Map<Integer, Identifiable> cache = new HashMap<Integer, Identifiable>();

    public static void clear() {
        cache.clear();
        Log.model().trace("Model cache cleared.");
    }

    @SuppressWarnings("rawtypes")
    public static Identifiable add(final Integer id, final Identifiable identifiable) {
        cache.put(id, identifiable);
        Log.model().trace("Add into model cache: " + id);
        return identifiable;
    }

    @SuppressWarnings("rawtypes")
    public static Identifiable get(final IdentifiableInterface identifiable) {
        Log.model().trace("Get from model cache: " + identifiable.getId());
        return cache.get(identifiable.getId());
    }

}
