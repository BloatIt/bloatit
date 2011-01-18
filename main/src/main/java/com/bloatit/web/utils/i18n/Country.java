/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.utils.i18n;

import java.io.IOException;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.bloatit.common.FatalErrorException;
import com.bloatit.web.html.components.standard.form.DropDownElement;
import com.bloatit.web.utils.PropertyLoader;

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
    private static Set<Country> availableCountries = null;
    private final String name;
    private final String code;

    /**
     * Creates a new country
     *
     * @param name the long name of the country
     * @param code the ISO code of the country
     */
    public Country(String name, String code) {
        super();
        this.name = name;
        this.code = code;
    }

    /**
     * @return the long name of the country (should be in the main language of the
     *         country)
     */
    public String getName() {
        return name;
    }

    /**
     * @return the ISO code of the country
     */
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
    public int compareTo(Country o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42; // any arbitrary constant will do
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Country other = (Country) obj;
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
        if (availableCountries == null) {
            availableCountries = new TreeSet<Country>();
            initAvailableCountries();
        }
        return availableCountries;
    }

    /**
     * Loads countries from the <code>countries list </code>
     */
    private static void initAvailableCountries() {
        try {
            Properties properties = PropertyLoader.loadProperties(COUNTRIES_PATH);
            for (Entry<?, ?> property : properties.entrySet()) {
                String key = (String) property.getKey();
                String value = (String) property.getValue();
                availableCountries.add(new Country(value, key));
            }
        } catch (IOException e) {
            throw new FatalErrorException("File describing available countries is not available at " + COUNTRIES_PATH, e);
        }
    }
}
