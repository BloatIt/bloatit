/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework;

import java.util.Properties;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.ConfigurationManager.PropertiesType;
import com.bloatit.common.ReloadableConfiguration;

/**
 * Class used to load properties related to locales management
 */
public class LocalesConfiguration extends ReloadableConfiguration {
    public static final LocalesConfiguration configuration = new LocalesConfiguration();
    private PropertiesRetriever languagesProperties;
    private PropertiesRetriever countryProperties;

    private LocalesConfiguration() {
        super();
        loadConfiguration();
    }

    /**
     * @return the list of available countries on the system
     */
    public static Properties getCountries() {
        return configuration.countryProperties.getProperties();
    }

    /**
     * @return the list of available languages on the system
     */
    public static Properties getLanguages() {
        return configuration.languagesProperties.getProperties();
    }

    private void loadConfiguration() {
        languagesProperties = ConfigurationManager.loadProperties("locales/languages.properties", PropertiesType.SHARE);
        countryProperties = ConfigurationManager.loadProperties("locales/countries.properties", PropertiesType.SHARE);
    }

    @Override
    public String getName() {
        return "Locales";
    }

    @Override
    protected void doReload() {
        configuration.loadConfiguration();
    }

    protected static void load() {
        configuration.loadConfiguration();
    }
}
