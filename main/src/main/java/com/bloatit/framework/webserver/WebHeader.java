package com.bloatit.framework.webserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.xcgiserver.HttpHeader;

public class WebHeader {

    private static final String UTF_8 = "UTF-8";
    
    private String language = "en";
    private String pageName = "";
    private final Parameters parameters = new Parameters();
    private final Parameters getParameters = new Parameters();
    private final HttpHeader httpHeader;

    public WebHeader(final HttpHeader httpHeader) {

        this.httpHeader = httpHeader;
        try {

            boolean languageFound = false;
            // Extract params and page name
            final String[] splitPath = httpHeader.getPathInfo().split("/");
            for (final String part : splitPath) {

                if (!part.isEmpty()) {
                    if (!languageFound) {
                        language = part;
                        languageFound = true;
                    } else {

                        // Page name is the list of part before a part with a
                        // "-"
                        if (part.contains("-")) {
                            // Part of the params
                            final String[] pair = part.split("-",2);
                            if (pair.length == 2) {
                                parameters.add(pair[0], pair[1]);
                            } else {
                                Log.framework().error("Malformed parameter " + part + " in '" + httpHeader.getPathInfo() + "'");
                            }
                        } else {
                            // Part of the page name

                            if (!pageName.isEmpty()) {
                                pageName += "/";
                            }
                            pageName += URLDecoder.decode(part, UTF_8);

                        }
                    }
                }
            }

            // Extract get params
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

    /**
     * Get parameters formated with syntax as: /plop-1/plip-2
     */
    public final Parameters getParameters() {
        return parameters;
    }

    /**
     * Get get parameters formated with syntax as: ?plop=1&plip=2
     */
    public final Parameters getGetParameters() {
        return getParameters;
    }

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }

}
