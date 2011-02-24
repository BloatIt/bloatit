/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.utils.i18n;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.bloatit.common.PropertyLoader;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.webserver.components.form.DropDownElement;

/**
 * <p>
 * Represents countries with a couple of full
 * <code>name<code> and short ISO <code>code</code>.
 * </p>
 * <p>
 * Examples :
 * <li>France -> fr</li>
 * <li>England -> en</li>
 * <li>Deutschland -> de</li>
 * </p>
 */
public final class Country implements Comparable<Country>, DropDownElement {
    private static final String COUNTRIES_PATH = "i18n/countries";
    private static final Set<Country> availableCountries = Collections.unmodifiableSet(createAvailableCountries());
    private final String name;
    private final String code;

    /**
     * Creates a new country
     *
     * @param name the long name of the country
     * @param code the ISO code of the country
     */
    public Country(final String name, final String code) {
        super();
        this.name = name;
        this.code = code;
    }

    /**
     * @return the long name of the country (should be in the main language of
     *         the country)
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the ISO code of the country
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @return the Locale matching the country
     */
    public Locale getLocale() {
        return new Locale(code);
    }

    @Override
    public int compareTo(final Country o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42; // any arbitrary constant will do
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Country other = (Country) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Lists all available countries ordered on their fullname
     * </p>
     *
     * @return a list of the available countries
     */
    public static Set<Country> getAvailableCountries() {
        return availableCountries;
    }

    /**
     * Used to initialize the {@link Country#availableCountries} static field.
     *
     * @return the list of country loaded from a country ressources file.
     */
    private static Set<Country> createAvailableCountries() {
        final TreeSet<Country> countries = new TreeSet<Country>();
        try {
            final Properties properties = PropertyLoader.loadProperties(COUNTRIES_PATH);
            for (final Entry<?, ?> property : properties.entrySet()) {
                final String key = (String) property.getKey();
                final String value = (String) property.getValue();
                countries.add(new Country(value, key));
            }
        } catch (final IOException e) {
            throw new FatalErrorException("File describing available countries is not available at " + COUNTRIES_PATH, e);
        }
        return countries;
    }
}
