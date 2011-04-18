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
    private Map<String, String> parameters = new HashMap<String, String>();

    public TemplateFile(final String filename) {
        super();
        this.filename = filename;
    }

    public void addNamedParameter(final String name, final String value) {
        parameters.put(name, Matcher.quoteReplacement(value));
    }

    public String getContent(final Locale language) throws IOException {
        File file = new File(FOLDER + language.getLanguage() + "/" + filename);
        if (!file.canRead()) {
            Log.framework().warn("File in default language not readable, using en instead");
            file = new File(FOLDER + "en/" + filename);
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
