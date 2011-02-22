package com.bloatit.framework.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.xcgiserver.HttpHeader;

public class RestHeader {

    private static final String UTF_8 = "UTF-8";

    private String resourceName = "";
    private final Parameters parameters = new Parameters();
    private final HttpHeader httpHeader;

    public RestHeader(HttpHeader httpHeader) {

        this.httpHeader = httpHeader;
        try {
            // Extract language
            String pathInfo = httpHeader.getPathInfo();
            if (pathInfo.startsWith("/") && pathInfo.length() > 1) {
                resourceName = URLDecoder.decode(pathInfo.substring(1), UTF_8);
            }

            // Extract get params
            for (final String param : httpHeader.getQueryString().split("&")) {
                final String[] pair = param.split("=");
                if (pair.length >= 2) {
                    parameters.add(URLDecoder.decode(pair[0], UTF_8), URLDecoder.decode(pair[1], UTF_8));
                }
            }

        } catch (final UnsupportedEncodingException ex) {
            Log.framework().error("Cannot parse url", ex);
        }

    }

    public final String getResourceName() {
        return resourceName;
    }

    public final Parameters getParameters() {
        return parameters;
    }

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }

}
