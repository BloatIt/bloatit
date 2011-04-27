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

import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>
 * A class used to describe a set of parameters
 * </p>
 * <p>
 * Parameters are described as pairs Key->value
 * </p>
 * <p>
 * Whenever a parameter is read, it is deleted. A convenience method is provided
 * to read without deletion, but shouldn't be used unless for checking
 * </p>
 */
public class Parameters extends GenericParameters<HttpParameter, String> {

    /**
     * Create an empty list of parameter
     */
    public Parameters() {
        super();
    }

    /**
     * Add a parameter to the list
     * 
     * @param name the name of the parameter to add
     * @param value the value of the parameter to add
     */
    @Override
    public final void add(final String name, final String value) {
        final HttpParameter httpParameter = getElements().get(name);
        if (httpParameter != null) {
            httpParameter.add(value);
        } else {
            final HttpParameter newHttpParameter = new HttpParameter(value);
            getElements().put(name, newHttpParameter);
        }
    }

    @Override
    public Set<Entry<String, HttpParameter>> entrySet() {
        return super.entrySet();
    }

}
