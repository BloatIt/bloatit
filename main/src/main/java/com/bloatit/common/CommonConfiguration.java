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
package com.bloatit.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

/**
 * Everything must be final and non mutable to make sure there is no pb wit the
 * multi-thread.
 *
 * @author thomas
 */
public class CommonConfiguration {
    private static final CommonConfiguration configuration = new CommonConfiguration();

    private String projectVersion;

    private CommonConfiguration() {
        super();
        loadConfiguration();
    }

    public static String getProjectVersion() {
        return configuration.projectVersion;
    }

    private void loadConfiguration() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("build.properties");
        if(resourceAsStream == null){
            throw new BadProgrammerException("Cannot locate the 'build.properties' configuration file.");
        }
        // ConfigurationManager
        try {
            Properties buildProperties = new Properties();
            buildProperties.load(resourceAsStream);
            projectVersion = buildProperties.getProperty("project.version");
        } catch (IOException e) {
            throw new BadProgrammerException("Cannot locate a configuration file.");
        }
    }

    public static void load() {
        configuration.loadConfiguration();
    }

}
