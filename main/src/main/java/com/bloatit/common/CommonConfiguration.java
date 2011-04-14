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

    public static final CommonConfiguration configuration = new CommonConfiguration();

    private String projectVersion;

    private CommonConfiguration() {
        super();
        loadConfiguration();
    }

    public static String getProjectVersion() {
        return configuration.projectVersion;
    }

    protected void loadConfiguration() {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("build.properties");
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
