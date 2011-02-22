package com.bloatit.framework.webserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.xcgiserver.HttpHeader;

public class WebHeader {

    private static final String UTF_8 = "UTF-8";
    private static final String PARAMETERS = "param";
    private static final String PAGE_NAME = "page";
    private static final String LANGUAGE = "lang";

    private String language = "en";
    private String pageName = "";
    private final Parameters parameters = new Parameters();
    private final Parameters getParameters = new Parameters();
    private final HttpHeader httpHeader;

    public WebHeader(HttpHeader httpHeader) {

        this.httpHeader = httpHeader;
        try {
            // Extract language
            String scriptName = httpHeader.getScriptName();
            if (scriptName.startsWith("/") && scriptName.length() > 1) {
                language = URLDecoder.decode(scriptName.substring(1), UTF_8);
            }

            // Extract params and page name
            String[] splitPath = httpHeader.getPathInfo().split("/");
            for (String part : splitPath) {
                // Page name is the list of part before a part with a "-"
                if (!part.contains("-")) {
                    // Part of the params
                    final String[] pair = part.split("-");
                    if (pair.length == 2) {
                        parameters.add(pair[0], pair[1]);
                    } else {
                        Log.framework().error("Malformed parameter " + part + " in '" + httpHeader.getPathInfo() + "'");
                    }
                } else {
                    // Part of the page name
                    if (!part.isEmpty()) {
                        if (!pageName.isEmpty()) {
                            pageName += "/";
                        }
                        pageName += URLDecoder.decode(part, UTF_8);
                    }
                }
            }

            //Extract get params
            for (final String param : httpHeader.getQueryString().split("&")) {
                final String[] pair = param.split("=");
                if (pair.length >= 2) {
                    getParameters.add(URLDecoder.decode(pair[0], UTF_8), URLDecoder.decode(pair[1], UTF_8));
                }
            }

        } catch (final UnsupportedEncodingException ex) {
            Log.framework().error("Cannot parse url", ex);
            pageName = "404";
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

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }



}
