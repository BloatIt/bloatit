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

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.ConfigurationFactory;

import org.apache.commons.codec.digest.DigestUtils;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

public class MemoryCache implements Cache {
    private static final Ehcache mcache;
    private static final MemoryCache instance;

    static {
        mcache = CacheManager.create(ConfigurationFactory.class.getResource("/ehcache.xml")).addCacheIfAbsent("markdown_cache");
        instance = new MemoryCache();
    }


    private MemoryCache() {
        // Disable CTOR
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
        mcache.put(new Element(DigestUtils.md5(keyGenerator), value));
    }

    @Override
    public String get(final String keyGenerator) {
        Element element = mcache.get(DigestUtils.md5(keyGenerator));
        if (element == null) {
            return null;
        }
        return (String) element.getValue();
    }

    public static MemoryCache getInstance() {
        return instance;
    }

}
