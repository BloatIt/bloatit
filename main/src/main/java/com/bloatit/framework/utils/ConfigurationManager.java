package com.bloatit.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Properties;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.webserver.url.Loaders;
import com.bloatit.framework.webserver.url.Loaders.ConversionErrorException;

/**
 * A class to handle configuration files
 */
public class ConfigurationManager {
    public static String SHARE_DIR = System.getProperty("user.home") + "/.local/share/bloatit/";

    private final static String ETC_DIR = System.getProperty("user.home") + "/.config/bloatit/";
    private final static String FALLBACK_ETC_DIR = "/etc/bloatit/";
    private static final String SUFFIX = ".properties";

    /**
     * <p>
     * Loads the content of a properties file in the default configuration file directory
     * for bloatit. Returns it as a Properties list, aka a map containing the key->value
     * pairs
     * </p>
     * <p>
     * The <code>name</code> of the property file is the name of the file, or the path
     * from the root of the configuration directory.
     * </p>
     *
     * @param name the name of the property file
     * @return a map key -> value
     */
    public static PropertiesRetriever loadProperties(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("null input: name");
        }
        String newName = name;
        if (newName.endsWith(SUFFIX)) {
            newName = newName.substring(0, newName.length() - SUFFIX.length());
        }
        newName = newName.replace('.', '/');
        newName = newName + SUFFIX;

        File f = new File(ETC_DIR + newName);
        if (!f.exists()) {
            f = new File(FALLBACK_ETC_DIR + newName);
            if (!f.exists()) {
                throw new FatalErrorException("Cannot locate a configuration file. Please create either " + ETC_DIR + newName + " or "
                        + FALLBACK_ETC_DIR + newName);
            }
        }

        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            final InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            final Properties props = new Properties();
            props.load(isr);
            return new PropertiesRetriever(props);
        } catch (final FileNotFoundException e) {
            throw new FatalErrorException("Cannot load configuration file " + f.getAbsolutePath() + " might have been erroneously deleted");
        } catch (final IOException e) {
            throw new FatalErrorException("Cannot load configuration file " + f.getAbsolutePath() + ". I dunno why ...");
        }
    }

    public static class PropertiesRetriever {
        private final Properties prop;

        public PropertiesRetriever(Properties props) {
            prop = props;
        }

        public Properties getProperties() {
            return prop;
        }

        private <T> T getSome(String key, T defaultValue, Class<T> clazz) {
            String property = prop.getProperty(key);
            if (prop.getProperty(key) != null) {
                try {
                    return Loaders.fromStr(clazz, property);
                } catch (ConversionErrorException e) {
                    Log.framework().error("Conversion error in loading the property: " + key, e);
                }
            }
            Log.framework().warn("Using the default property for: " + key);
            return defaultValue;
        }

        public int getInt(String key, int defaultValue) {
            return getSome(key, defaultValue, Integer.class);
        }

        public Long getLong(String key, Long defaultValue) {
            return getSome(key, defaultValue, Long.class);
        }

        public Short getShort(String key, Short defaultValue) {
            return getSome(key, defaultValue, Short.class);
        }

        public Byte getByte(String key, Byte defaultValue) {
            return getSome(key, defaultValue, Byte.class);
        }

        public Boolean getBoolean(String key, Boolean defaultValue) {
            return getSome(key, defaultValue, Boolean.class);
        }

        public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
            return getSome(key, defaultValue, BigDecimal.class);
        }

        public Character getCharacter(String key, Character defaultValue) {
            return getSome(key, defaultValue, Character.class);
        }

        public Date getDate(String key, Date defaultValue) {
            return getSome(key, defaultValue, Date.class);
        }

        public Double getDouble(String key, Double defaultValue) {
            return getSome(key, defaultValue, Double.class);
        }

        public Float getFloat(String key, Float defaultValue) {
            return getSome(key, defaultValue, Float.class);
        }

        public String getString(String key, String defaultValue) {
            String property = prop.getProperty(key);
            if (property != null) {
                return property;
            }
            Log.framework().warn("Using the default property for: " + key);
            return defaultValue;
        }

        /**
         * @param key
         * @param defaultValue
         * @return
         * @see java.util.Properties#getProperty(java.lang.String, java.lang.String)
         */
        public String getProperty(String key, String defaultValue) {
            return prop.getProperty(key, defaultValue);
        }

        /**
         * @param key
         * @return
         * @see java.util.Properties#getProperty(java.lang.String)
         */
        public String getProperty(String key) {
            return prop.getProperty(key);
        }
    }
}
