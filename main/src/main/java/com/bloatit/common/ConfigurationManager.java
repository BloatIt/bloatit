package com.bloatit.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * A class to handle configuration files
 */
public class ConfigurationManager {
    private final static String DEFAULT_FILE = System.getProperty("user.home") + "/.config/bloatit";
    private final static String FALLBACK_FILE = "/etc/bloatit";
    private static final String SUFFIX = ".properties";

    /**
     * <p>
     * Loads the content of a properties file in the default configuration file
     * directory for bloatit. Returns it as a Properties list, aka a map
     * containing the key->value pairs
     * </p>
     * <p>
     * The <code>name</code> of the property file is the name of the file, or
     * the path from the root of the configuration directory.
     * </p>
     * 
     * @param name
     *            the name of the property file
     * @return a map key -> value
     */
    public static Properties loadProperties(String name) {
        if (name == null) {
            throw new IllegalArgumentException("null input: name");
        }
        if (name.charAt(0) == '/') {
            name = name.substring(1);
        }
        if (name.endsWith(SUFFIX)) {
            name = name.substring(0, name.length() - SUFFIX.length());
        }
        name = name.replace('.', '/');
        name = "/" + name + SUFFIX;

        File f = new File(DEFAULT_FILE + name);
        if (!f.exists()) {
            f = new File(FALLBACK_FILE + name);
            if (!f.exists()) {
                throw new FatalErrorException("Cannot locate a configuration file. Please create either " + DEFAULT_FILE + name + " or "
                        + FALLBACK_FILE + name);
            }
        }

        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            final InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            Properties props = new Properties();
            props.load(isr);
            return props;
        } catch (FileNotFoundException e) {
            throw new FatalErrorException("Cannot load configuration file " + f.getAbsolutePath() + " might have been erroneously deleted");
        } catch (IOException e) {
            throw new FatalErrorException("Cannot load configuration file " + f.getAbsolutePath() + ". I dunno why ...");
        }
    }
}
