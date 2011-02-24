package com.bloatit.framework.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class GenericParameters<T, U> {

    private final Map<String, T> elements = new HashMap<String, T>();

    /**
     * Create an empty list of parameter
     */
    public GenericParameters() {
        super();
    }

    /**
     * Add a parameter to the list
     *
     * @param name the name of the parameter to add
     * @param value the value of the parameter to add
     */
    public abstract void add(final String name, final U value);

    protected Map<String, T> getElements() {
        return elements;
    }

    // {
    // T element = elements.get(name);
    // if (element != null) {
    // element.addValue(value);
    // } else {
    // elements.put(name, createElement(value));
    // }
    // return this;
    // }

    /**
     * <p>
     * Finds a parameter into the list and removes it.
     * </p>
     *
     * @param name the name of the parameter to find
     * @return the string value of the parameter
     */
    public final T pick(final String name) {
        final T element = elements.get(name);
        if (element == null) {
            return null;
        }
        elements.remove(name);
        return element;
    }

    /**
     * <p>
     * Finds a parameter into the list without removing it.
     * </p>
     *
     * @param name the name of the parameter to find
     * @return the string value of the parameter
     */
    public final T look(final String name) {
        final T element = elements.get(name);
        if (element == null) {
            return null;
        }
        return element;
    }

    public final boolean containsKey(final String key) {
        if (key == null) {
            return false;
        }
        return elements.containsKey(key);
    }

    public final int size() {
        return elements.size();
    }

    public void putAll(final GenericParameters<T, U> parameters) {
        elements.putAll(parameters.elements);
    }

    public Set<Entry<String, T>> entrySet() {
        return elements.entrySet();
    }
}
