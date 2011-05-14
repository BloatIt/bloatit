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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

public final class TemplateFile {
    private static final String ENDL = System.getProperty("line.separator");

    private static String FOLDER = ConfigurationManager.SHARE_DIR + "/templates/";

    private final String filename;
    private final Map<String, String> parameters = new HashMap<String, String>();

    public TemplateFile(final String filename) {
        super();
        this.filename = filename;
    }

    public void addNamedParameter(final String name, final String value) {
        parameters.put(name, Matcher.quoteReplacement(value));
    }

    public String getContent(final Locale language) throws IOException {
        File file;
        if(language == null) {
            file = new File(FOLDER + "commons/" + filename);
        } else {
            file = new File(FOLDER + language.getLanguage() + "/" + filename);
            if (!file.canRead()) {
                Log.framework().warn("File in default language not readable, using en instead");
                file = new File(FOLDER + "en/" + filename);
            }
        }
        final StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            // repeat until all lines are read
            while ((text = reader.readLine()) != null) {
                for (final Entry<String, String> param : parameters.entrySet()) {
                    text = text.replace("${" + param.getKey() + "}", param.getValue());
                }
                contents.append(text).append(ENDL);
            }
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (final IOException e) {
                Log.framework().fatal("Cannot close the file reader", e);
            }
        }
        return contents.toString();
    }
}
