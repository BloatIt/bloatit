package com.bloatit.framework.xcgiserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.Parameters;

public class Query {

    private static final String UTF_8 = "UTF-8";
    private static final String PARAMETERS = "param";
    private static final String PAGE_NAME = "page";
    private static final String LANGUAGE = "lang";

    private String language = "en";
    private String pageName = "404";
    private final Parameters parameters = new Parameters();
    private final Parameters getParameters = new Parameters();

    Query(final String queryString) {
        try {
            String parametersString = null;
            for (final String param : queryString.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length >= 2) {
                    final String key = URLDecoder.decode(pair[0], UTF_8);
                    if (key.equals(LANGUAGE)) {
                        language = URLDecoder.decode(pair[1], UTF_8);
                    } else if (key.equals(PAGE_NAME)) {
                        pageName = URLDecoder.decode(pair[1], UTF_8);
                    } else if (key.equals(PARAMETERS)) {
                        parametersString = URLDecoder.decode(pair[1], UTF_8);
                    } else {
                        getParameters.add(pair[0], pair[1]);
                    }
                }
            }

            if (parametersString != null) {
                final String[] namedValues = parametersString.split("/");
                for (final String namedValue : namedValues) {
                    final String[] pair = namedValue.split("-");
                    if (pair.length == 2) {
                        parameters.put(pair[0], pair[1]);
                    } else {
                        Log.framework().error("Malformed parameter " + namedValue);
                    }
                }
            }
        } catch (final UnsupportedEncodingException ex) {
            Log.framework().error("Cannot parse url", ex);
        }
    }

    public final String getLanguage() {
        return language;
    }

    public final String getPageName() {
        return pageName;
    }

    public final Parameters getParameters() {
        return parameters;
    }

    public final Parameters getGetParameters() {
        return getParameters;
    }

}
