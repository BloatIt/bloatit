package com.bloatit.web;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;

public class WebConfiguration {

    public static final WebConfiguration configuration = new WebConfiguration();

    private final PropertiesRetriever properties;

    private final String bloatitDocumentationDir;

    private final String bloatitWwwDir;

    private WebConfiguration() {
        properties = ConfigurationManager.loadProperties("web.properties");

        bloatitDocumentationDir = properties.getString("bloatit.documentation.dir");
        bloatitWwwDir = properties.getString("bloatit.www.dir");
    }

    /**
     * Make sure the configuration file is loaded.
     */
    public static void loadConfiguration() {
        configuration.getClass();
    }

    public static WebConfiguration getConfiguration() {
        return configuration;
    }

    public static String getBloatitDocumentationDir() {
        return configuration.bloatitDocumentationDir;
    }

    public static String getBloatitWwwDir() {
        return configuration.bloatitWwwDir;
    }

}
