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
package com.bloatit.framework.utils.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

import edu.emory.mathcs.backport.java.util.Collections;

public class MemoryCache implements Cache {
    private static MemoryCache instance;

    @SuppressWarnings("unchecked")
    private final Map<String, String> cache = Collections.synchronizedMap(new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 8626612206780654805L;

        @Override
        protected boolean removeEldestEntry(final Entry<String, String> eldest) {
            final boolean remove = size() > FrameworkConfiguration.getMemoryCacheMaxSize();
            if (remove) {
                Log.framework().info("Removing eldest entry from Memory cache. (size " + size() + ")");
            }
            return remove;
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

    /**
     * {@inheritDoc}
     * <p>
     * This method generates a hash of the <code>keyGenerator</code> and uses it
     * as a key for the element
     * </p>
     * 
     * @param keyGenerator a String that will ba hashed to serve as a key
     */
    @Override
    public void cache(final String keyGenerator, final String value) {
        if (keyGenerator == null || keyGenerator.isEmpty()) {
            throw new NonOptionalParameterException("Trying to cache with a null or empty key");
        }
        if (value == null) {
            throw new NonOptionalParameterException("Trying to cache a null value");
        }
        cache.put(sha1(keyGenerator), value);
    }

    @Override
    public String get(final String keyGenerator) {
        return cache.get(sha1(keyGenerator));
    }

    /**
     * Finds the sha1 hash of an <code>input</code> string
     * 
     * @param input the string to hash
     * @return the hashed string
     */
    private static String sha1(final String input) {
        Log.framework().trace("Hashing to cache");
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (final NoSuchAlgorithmException ex) {
            throw new BadProgrammerException("Algorithm Sha1 not available", ex);
        }
        md.update(input.getBytes());
        final byte byteData[] = md.digest();

        final StringBuilder sb = new StringBuilder();
        for (final byte element : byteData) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
