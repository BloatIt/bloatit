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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * A class used to describe HttpParameters that can contain many values for one
 * parameter (eg: for checkboxes).
 * </p>
 * <p>
 * It may represent a single value or a multiple value parameter.
 * </p>
 */
public class HttpParameter implements Iterable<String> {
    private final List<String> values = new LinkedList<String>();

    public HttpParameter(final String value) {
        super();
        values.add(value);
    }

    /**
     * <p>
     * Indicates whether the parameter contains multiple values or not
     * </p>
     * 
     * @return <code>true</code> if the parameter contains multiple values,
     *         <code>false</code> otherwise
     */
    public boolean isMultiple() {
        return values.size() > 1;
    }

    public int size() {
        return values.size();
    }

    /**
     * <p>
     * Returns <b>only</b> the first value in the list of values.
     * </p>
     * 
     * @return the first value for this parameter
     */
    public String getSimpleValue() {
        return values.get(0);
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    protected void add(final String value) {
        values.add(value);
    }
}
