package com.bloatit.framework.utils;

import java.util.HashMap;

/**
 * <p>
 * A class used to describe a set of parameters
 * </p>
 * <p>
 * Parameters are described as pairs Key->value
 * </p>
 * <p>
 * Whenever a parameter is read, it is deleted. A convenience method is provided
 * to read without deletion, but should be used unless for checking
 * </p>
 */
public class Parameters extends HashMap<String, String> {
    private static final long serialVersionUID = 2992292463988772864L;

    /**
     * Create an empty list of parameter
     */
    public Parameters() {
        super();
    }

    /**
     * Create a list of parameter with initialized containing a single element
     * <code>name</code>-><code>value</code>
     *
     * @param name
     *            the name of the first parameter to inser
     * @param value
     *            the value of the parameter
     */
    public Parameters(final String name, final String value) {
        super();
        put(name, value);
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
    public final Parameters add(final String name, final String value) {
        put(name, value);
        return this;
    }

    /**
     * <p>
     * Finds a parameter into the list and removes it.
     * </p>
     *
     * @param name the name of the parameter to find
     * @return the string value of the parameter
     */
    public final String pick(final String name) {
        final String value = get(name);
        if (value != null) {
            remove(name);
        }
        return value;
    }

    /**
     * <p>
     * Finds a parameter into the list without removing it.
     * </p>
     *
     * @param name the name of the parameter to find
     * @return the string value of the parameter
     */
    public final String look(final String name) {
        return get(name);
    }
}
