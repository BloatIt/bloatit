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
import com.bloatit.common.ReloadableConfiguration;

/**
 * Class used to load properties related to 
 */
public class LocalesConfiguration extends ReloadableConfiguration {
    public static final LocalesConfiguration configuration = new LocalesConfiguration();
    private PropertiesRetriever languagesProperties;
    private PropertiesRetriever countryProperties;
    
    private LocalesConfiguration() {
        super();
        loadConfiguration();
    }
    
    public static Properties getCountries(){
        return configuration.countryProperties.getProperties();
    }
    
    public static Properties getLanguages(){
        return configuration.languagesProperties.getProperties();
    }
    
    protected void loadConfiguration() {
        languagesProperties = ConfigurationManager.loadProperties("framework.properties");
        countryProperties = ConfigurationManager.loadProperties("framework.properties");
    }
    
    @Override
    public String getName() {
        return "Locales";
    }

    @Override
    protected void doReload() {
        configuration.loadConfiguration();
    }
}
