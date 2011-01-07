package com.bloatit.web.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.bloatit.common.Log;
import com.bloatit.web.utils.url.Parameters;

public class Query {

    private static final String PARAMETERS = "param";
    private static final String PAGE_NAME = "page";
    private static final String LANGUAGE = "lang";


    String language = "en";
    String pageName = "404";
    Parameters parameters = new Parameters();

    Query(String queryString) {
        try {
            String parametersString = null;
            for (final String param : queryString.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length >= 2) {
                    final String key = URLDecoder.decode(pair[0], "UTF-8");
                    if (key.equals(LANGUAGE)) {
                        language = URLDecoder.decode(pair[1], "UTF-8");
                    } else if (key.equals(PAGE_NAME)) {
                        pageName = URLDecoder.decode(pair[1], "UTF-8");
                    } else if (key.equals(PARAMETERS)) {
                        parametersString = URLDecoder.decode(pair[1], "UTF-8");
                    }
                }
            }

            if (parametersString != null){
                String[] namedValues = parametersString.split("/");
                for (String namedValue : namedValues) {
                    String[] pair = namedValue.split("-");
                    if (pair.length == 2){
                        parameters.put(pair[0], pair[1]);
                    }else{
                        Log.web().error("Malformed parameter " + namedValue);
                    }
                }
            }
        } catch (final UnsupportedEncodingException ex) {
            Log.web().error("Cannot parse url", ex);
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

}
