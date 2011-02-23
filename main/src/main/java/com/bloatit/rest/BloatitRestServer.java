package com.bloatit.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bloatit.framework.rest.RestServer;
import com.bloatit.rest.resources.RestMember;

public class BloatitRestServer extends RestServer {
    private final Map<String, Class<?>> locations = new HashMap<String, Class<?>>() {
        private static final long serialVersionUID = -5012179845511358309L;

        {
//            put("members", Member.class);
            put("members", RestMember.class);
        }
    };

    private final Class<?>[] classes = new Class<?>[] { RestMember.class, Member.class, Members.class, MarshableList.class };

    @Override
    protected Set<String> getResourcesDirectories() {
        final HashSet<String> directories = new HashSet<String>();
        directories.add("rest");
        return directories;
    }

    @Override
    protected Class<?> getClass(final String forResource) {
        return locations.get(forResource);
    }

    @Override
    protected boolean isValidResource(final String forResource) {
        return locations.containsKey(forResource);
    }

    @Override
    protected Class<?>[] getJAXClasses() {
        return classes;
    }

    @Override
    public boolean initialize() {
        return true;
    }
}
