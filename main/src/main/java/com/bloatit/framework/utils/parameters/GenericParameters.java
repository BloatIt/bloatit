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
package com.bloatit.framework.utils.parameters;

import java.util.HashMap;
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
