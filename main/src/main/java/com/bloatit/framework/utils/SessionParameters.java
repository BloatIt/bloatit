package com.bloatit.framework.utils;

import java.util.HashMap;

import com.bloatit.framework.webserver.url.UrlParameter;

public class SessionParameters extends HashMap<String, UrlParameter<?>> {

    private static final long serialVersionUID = -6721136936202045617L;

    /**
     * Create an empty list of parameter
     */
    public SessionParameters() {
        super();
    }

    /**
     * Add a parameter to the list
     *
     * @param name
     *            the name of the parameter to add
     * @param value
     *            the value of the paramter to add
     * @return itself
     */
    public final SessionParameters add(final String name, final UrlParameter<?> value) {
        put(name, value);
        return this;
    }

    /**
     * <p>
     * Finds a parameter into the list and DO NOT REMOVE IT.
     * </p>
     *
     * @param name the name of the parameter to find
     * @return the string value of the parameter
     */
    public final UrlParameter<?> look(final String name) {
        final UrlParameter<?> value = get(name);
        return value;
    }
}
