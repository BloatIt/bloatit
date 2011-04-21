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
import com.bloatit.framework.webprocessor.annotations.ConversionErrorException;
import com.bloatit.framework.webprocessor.url.Loaders;

/**
 * A class to handle configuration files
 */
public class ConfigurationManager {
    public final static String SHARE_DIR = System.getProperty("user.home") + "/.local/share/bloatit/";
    public final static String ETC_DIR = System.getProperty("user.home") + "/.config/bloatit/";
    private final static String FALLBACK_ETC_DIR = "/etc/bloatit/";
    private static final String SUFFIX = ".properties";

    private static final StandardPBEStringEncryptor encryptor = createEncryptor();

    public enum PropertiesType {
        ETC, SHARE
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
        return loadProperties(name, PropertiesType.ETC);
    }

    public static PropertiesRetriever loadProperties(final String name, final PropertiesType type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("null input: name");
        }
        String newName = name;
        if (newName.endsWith(SUFFIX)) {
            newName = newName.substring(0, newName.length() - SUFFIX.length());
        }
        newName = newName.replace('.', '/');
        newName = newName + SUFFIX;

        File f;
        if (type == PropertiesType.ETC) {
            f = new File(ETC_DIR + newName);
        } else {
            f = new File(SHARE_DIR + newName);
        }
        if (!f.exists()) {
            if (type != PropertiesType.ETC) {
                throw new BadProgrammerException("Cannot locate a configuration file. Please create " + SHARE_DIR + newName);
            }

            f = new File(FALLBACK_ETC_DIR + newName);
            if (!f.exists()) {
                throw new BadProgrammerException("Cannot locate a configuration file. Please create either " + ETC_DIR + newName + " or "
                        + FALLBACK_ETC_DIR + newName);
            }
        }

        FileInputStream fis = null;
        InputStreamReader isr = null;
        try {
            fis = new FileInputStream(f);
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            final Properties props = new EncryptableProperties(encryptor);
            props.load(isr);
            return new PropertiesRetriever(props, new Date(f.lastModified()));
        } catch (final FileNotFoundException e) {
            throw new BadProgrammerException("Cannot load configuration file " + f.getAbsolutePath() + " might have been erroneously deleted");
        } catch (final IOException e) {
            throw new BadProgrammerException("Cannot load configuration file " + f.getAbsolutePath() + ". I dunno why ...");
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (final IOException e) {
                    throw new BadProgrammerException("Stream crashed when closing ... This is bad");
                }
            }
        }
    }

    public static PBEStringEncryptor getEncryptor() {
        return encryptor;
    }

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

    public static class PropertiesRetriever {
        private final Properties properties;
        private final Date lastModified;

        public PropertiesRetriever(final Properties properties, final Date lastModified) {
            this.properties = properties;
            this.lastModified = lastModified;
        }

        public Properties getProperties() {
            return properties;
        }

        private <T> T getSome(final String key, final T defaultValue, final Class<T> clazz) {
            try {
                return getSome(key, clazz);
            } catch (final NoSuchElementException e) {
                return defaultValue;
            }
        }

        private <T> T getSome(final String key, final Class<T> clazz) {
            String property = properties.getProperty(key);
            if (property != null) {
                property = property.trim();
                try {
                    return Loaders.fromStr(clazz, property);
                } catch (final ConversionErrorException e) {
                    Log.framework().error("Conversion error in loading the property: " + key, e);
                }
            }
            throw new NoSuchElementException("Cannot find property: " + key);
        }

        public int getInt(final String key, final Integer defaultValue) {
            return getSome(key, defaultValue, Integer.class).intValue();
        }

        public int getInt(final String key) {
            return getSome(key, Integer.class).intValue();
        }

        public long getLong(final String key, final Long defaultValue) {
            return getSome(key, defaultValue, Long.class).longValue();
        }

        public long getLong(final String key) {
            return getSome(key, Long.class).longValue();
        }

        public short getShort(final String key, final Short defaultValue) {
            return getSome(key, defaultValue, Short.class).shortValue();
        }

        public short getShort(final String key) {
            return getSome(key, Short.class).shortValue();
        }

        public byte getByte(final String key, final Byte defaultValue) {
            return getSome(key, defaultValue, Byte.class).byteValue();
        }

        public byte getByte(final String key) {
            return getSome(key, Byte.class).byteValue();
        }

        public boolean getBoolean(final String key, final Boolean defaultValue) {
            return getSome(key, defaultValue, Boolean.class).booleanValue();
        }

        public boolean getBoolean(final String key) {
            return getSome(key, Boolean.class).booleanValue();
        }

        public BigDecimal getBigDecimal(final String key, final BigDecimal defaultValue) {
            return getSome(key, defaultValue, BigDecimal.class);
        }

        public BigDecimal getBigDecimal(final String key) {
            return getSome(key, BigDecimal.class);
        }

        public char getCharacter(final String key, final Character defaultValue) {
            return getSome(key, defaultValue, Character.class).charValue();
        }

        public char getCharacter(final String key) {
            return getSome(key, Character.class).charValue();
        }

        public Date getDate(final String key, final Date defaultValue) {
            return getSome(key, defaultValue, Date.class);
        }

        public Date getDate(final String key) {
            return getSome(key, Date.class);
        }

        public double getDouble(final String key, final Double defaultValue) {
            return getSome(key, defaultValue, Double.class).doubleValue();
        }

        public double getDouble(final String key) {
            return getSome(key, Double.class).doubleValue();
        }

        public float getFloat(final String key, final Float defaultValue) {
            return getSome(key, defaultValue, Float.class).floatValue();
        }

        public float getFloat(final String key) {
            return getSome(key, Float.class).floatValue();
        }

        public String getString(final String key, final String defaultValue) {
            return getSome(key, defaultValue, String.class);
        }

        public String getString(final String key) {
            return getSome(key, String.class);
        }

        public Date getModificationDate() {
            return lastModified;
        }
    }
}
