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
package com.bloatit.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Class used to load configuration files stored as properties
 */
public class PropertyLoader {

    private static final String SUFFIX = ".properties";

    /**
     * <p>
     * Loads a properties file from the classpath
     * </p>
     * <p>
     * Will parse a properties file found in the classpath. The file must respect standard
     * properties conventions :
     * <li>Name must end with .properties</li>
     * <li>File content should be lines containing pair of key = value e.g : foo.bar = 42</li>
     * </p>
     * <p>
     * For ease of use, the loader is flexible with the <i>name</i> of the properties
     * file, and therefore :
     * <li><i>name</i> can be described using <b>.</b> or <b>/</b> as separators</li>
     * <li><i>name</i> can optionally end with <b>.properties</b></ki>
     * <li>leading <b>/</b> in <i>name</i> will be ignored Therefore all the following
     * <i>names</i> are the same :
     * 
     * <pre>
     * foo.bar
     * foo.bar.properties
     * foo/bar
     * foo/bar.properties
     * /foo/bar
     * /foo/bar.properties
     * </pre>
     * 
     * </p>
     * 
     * @param name classpath resource name
     * @return resource converted to java.util.Properties
     * @throws IOException when <i>name</i> doesn't describe a valid properties file
     * @throws IllegalArgumentException when <i>name</i> is null
     */
    public static Properties loadProperties(final String inname) throws IOException {
        String name = inname;

        if (name == null) {
            throw new IllegalArgumentException("null input: name");
        }
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        if (name.endsWith(SUFFIX)) {
            name = name.substring(0, name.length() - SUFFIX.length());
        }
        name = name.replace('.', '/');

        Properties result = null;
        name = "/" + name + SUFFIX;
        final File f = new File(PropertyLoader.class.getResource(name).getFile());
        final FileInputStream fis = new FileInputStream(f);
        final InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        try {
            result = new Properties();
            result.load(isr);
        } catch (final FileNotFoundException e) {
            throw e;
        } finally {
            isr.close();
        }

        fis.close();

        return result;
    }
}