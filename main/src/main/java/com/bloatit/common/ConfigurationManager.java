package com.bloatit.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.webserver.annotations.ConversionErrorException;
import com.bloatit.framework.webserver.url.Loaders;

/**
 * A class to handle configuration files
 */
public class ConfigurationManager {
    public final static String SHARE_DIR = System.getProperty("user.home") + "/.local/share/bloatit/";
    public final static String ETC_DIR = System.getProperty("user.home") + "/.config/bloatit/";
    private final static String FALLBACK_ETC_DIR = "/etc/bloatit/";
    private static final String SUFFIX = ".properties";

    private static final StandardPBEStringEncryptor encryptor = createEncryptor();

    private static StandardPBEStringEncryptor createEncryptor() {
        final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        // Get the root password
        String password = System.getProperty("masterPassword");
        if (password == null || password.isEmpty()) {
            try {
                System.out.println("I need the master password!");
                final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                password = in.readLine();
            } catch (final IOException e) {
                throw new ExternalErrorException("Failed to read password from input", e);
            }
        }
        encryptor.setPassword(password);
        return encryptor;
    }

    public static PBEStringEncryptor getEncryptor() {
        return encryptor;
    }

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
     * @param name the name of the property file
     * @return a map key -> value
     */
    public static PropertiesRetriever loadProperties(final String name) {
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
                throw new BadProgrammerException("Cannot locate a configuration file. Please create either " + ETC_DIR + newName + " or "
                        + FALLBACK_ETC_DIR + newName);
            }
        }

        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            final InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            final Properties props = new EncryptableProperties(encryptor);
            props.load(isr);
            return new PropertiesRetriever(props);
        } catch (final FileNotFoundException e) {
            throw new BadProgrammerException("Cannot load configuration file " + f.getAbsolutePath() + " might have been erroneously deleted");
        } catch (final IOException e) {
            throw new BadProgrammerException("Cannot load configuration file " + f.getAbsolutePath() + ". I dunno why ...");
        }
    }

    public static class PropertiesRetriever {
        private final Properties prop;

        public PropertiesRetriever(final Properties props) {
            prop = props;
        }

        public Properties getProperties() {
            return prop;
        }

        private <T> T getSome(final String key, final T defaultValue, final Class<T> clazz) {
            try {
                return getSome(key, clazz);
            } catch (final NoSuchElementException e) {
                return defaultValue;
            }
        }

        private <T> T getSome(final String key, final Class<T> clazz) {
            final String property = prop.getProperty(key);
            Log.framework().info("Loading property: " + key + ", value: " + property);
            if (prop.getProperty(key) != null) {
                try {
                    return Loaders.fromStr(clazz, property);
                } catch (final ConversionErrorException e) {
                    Log.framework().error("Conversion error in loading the property: " + key, e);
                }
            }
            throw new NoSuchElementException("Cannot find property: " + key);
        }

        public int getInt(final String key, final int defaultValue) {
            return getSome(key, defaultValue, Integer.class);
        }

        public int getInt(final String key) {
            return getSome(key, Integer.class);
        }

        public Long getLong(final String key, final Long defaultValue) {
            return getSome(key, defaultValue, Long.class);
        }

        public Long getLong(final String key) {
            return getSome(key, Long.class);
        }

        public Short getShort(final String key, final Short defaultValue) {
            return getSome(key, defaultValue, Short.class);
        }

        public Short getShort(final String key) {
            return getSome(key, Short.class);
        }

        public Byte getByte(final String key, final Byte defaultValue) {
            return getSome(key, defaultValue, Byte.class);
        }

        public Byte getByte(final String key) {
            return getSome(key, Byte.class);
        }

        public Boolean getBoolean(final String key, final Boolean defaultValue) {
            return getSome(key, defaultValue, Boolean.class);
        }

        public Boolean getBoolean(final String key) {
            return getSome(key, Boolean.class);
        }

        public BigDecimal getBigDecimal(final String key, final BigDecimal defaultValue) {
            return getSome(key, defaultValue, BigDecimal.class);
        }

        public BigDecimal getBigDecimal(final String key) {
            return getSome(key, BigDecimal.class);
        }

        public Character getCharacter(final String key, final Character defaultValue) {
            return getSome(key, defaultValue, Character.class);
        }

        public Character getCharacter(final String key) {
            return getSome(key, Character.class);
        }

        public Date getDate(final String key, final Date defaultValue) {
            return getSome(key, defaultValue, Date.class);
        }

        public Date getDate(final String key) {
            return getSome(key, Date.class);
        }

        public Double getDouble(final String key, final Double defaultValue) {
            return getSome(key, defaultValue, Double.class);
        }

        public Double getDouble(final String key) {
            return getSome(key, Double.class);
        }

        public Float getFloat(final String key, final Float defaultValue) {
            return getSome(key, defaultValue, Float.class);
        }

        public Float getFloat(final String key) {
            return getSome(key, Float.class);
        }

        public String getString(final String key, final String defaultValue) {
            return getSome(key, defaultValue, String.class);
        }

        public String getString(final String key) {
            return getSome(key, String.class);
        }

        /**
         * @param key
         * @param defaultValue
         * @see java.util.Properties#getProperty(java.lang.String,
         *      java.lang.String)
         */
        public String getProperty(final String key, final String defaultValue) {
            return prop.getProperty(key, defaultValue);
        }

        /**
         * @param key
         * @see java.util.Properties#getProperty(java.lang.String)
         */
        public String getProperty(final String key) {
            return prop.getProperty(key);
        }
    }
}
