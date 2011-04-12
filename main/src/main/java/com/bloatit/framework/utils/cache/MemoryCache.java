package com.bloatit.framework.utils.cache;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

import edu.emory.mathcs.backport.java.util.Collections;

public class MemoryCache implements Cache {
    private static MemoryCache instance;

    @SuppressWarnings("unchecked")
    private LinkedHashMap<String, String> cache = (LinkedHashMap<String, String>) Collections.synchronizedMap(new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 8626612206780654805L;

        @Override
        protected boolean removeEldestEntry(Entry<String, String> eldest) {
            return size() > FrameworkConfiguration.getMemoryCacheMaxSize();
        }
    });

    private MemoryCache() {
        // Disable CTOR
    }

    /**
     * Finds the instance of the Memory cache for the server
     */
    public static MemoryCache getInstance() {
        if (instance == null) {
            instance = new MemoryCache();
        }
        return instance;
    }

    @Override
    public void cache(String key, String value) {
        if (key == null || key.isEmpty()) {
            throw new NonOptionalParameterException("Trying to cache with a null or empty key");
        }
        if (value == null) {
            throw new NonOptionalParameterException("Trying to cache a null value");
        }
        cache.put(key, value);
    }

    @Override
    public String get(String key) {
        return cache.get(key);
    }
}
