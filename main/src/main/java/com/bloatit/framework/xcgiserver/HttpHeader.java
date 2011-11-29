/*
 * Copyright (C) 2011 Linkeos.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.xcgiserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.parameters.Parameters;

public class HttpHeader {

    public static final String AUTHORIZATION_UNKNOWN = "unknown";
    public static final String DEFAULT_LANG = "default";
    private static final String UTF_8 = "UTF-8";

    private final Parameters getParameters = new Parameters();
    private String language = "en";
    private String pageName = "";
    private final Map<String, String> env;

    protected HttpHeader(final Map<String, String> env) {
        super();
        this.env = env;

        this.serverProtocol = notNull(env.get("SERVER_PROTOCOL"));
        this.serverSoftware = notNull(env.get("SERVER_SOFTWARE"));
        this.serverPort = toInt(env.get("SERVER_PORT"));
        this.serverName = notNull(env.get("SERVER_NAME"));
        this.serverAddr = notNull(env.get("SERVER_ADDR"));
        this.scriptName = notNull(env.get("SCRIPT_NAME"));
        this.scriptFilename = notNull(env.get("SCRIPT_FILENAME"));
        this.scgi = notNull(env.get("SCGIUtils"));
        this.requestUri = notNull(env.get("REQUEST_URI"));
        this.requestMethod = notNull(env.get("REQUEST_METHOD"));
        this.remotePort = toInt(env.get("REMOTE_PORT"));
        this.remoteAddr = notNull(env.get("REMOTE_ADDR"));
        this.redirectUri = notNull(env.get("REDIRECT_URI"));
        this.redirectStatus = toInt(env.get("REDIRECT_STATUS"));
        this.queryString = notNull(env.get("QUERY_STRING"));
        this.pathTranslated = notNull(env.get("PATH_TRANSLATED"));
        this.pathInfo = notNull(env.get("PATH_INFO"));
        this.httpXDoNotTrack = toInt(env.get("HTTP_X_DO_NOT_TRACK"));
        this.httpXBehavioralAdOptOut = toInt(env.get("HTTP_X_BEHAVIORAL_AD_OPT_OUT"));
        this.httpUserAgent = notNull(env.get("HTTP_USER_AGENT"));
        this.httpReferer = notNull(env.get("HTTP_REFERER"));
        this.httpKeepAlive = toInt(env.get("HTTP_KEEP_ALIVE"));
        this.httpHost = notNull(env.get("HTTP_HOST"));
        this.httpCookie = toMap(env.get("HTTP_COOKIE"));
        this.httpConnection = notNull(env.get("HTTP_CONNECTION"));
        this.httpCacheControl = notNull(env.get("HTTP_CACHE_CONTROL"));
        this.httpAuthorization = notNull(env.get("HTTP_AUTHORIZATION"));
        this.httpAcceptEncoding = toList(env.get("HTTP_ACCEPT_ENCODING"), ",");
        this.httpAcceptCharset = toList(env.get("HTTP_ACCEPT_CHARSET"), ",|;");
        this.httpAccept = toList(env.get("HTTP_ACCEPT"), ",");
        this.gatewayInterface = notNull(env.get("GATEWAY_INTERFACE"));
        this.documentRoot = notNull(env.get("DOCUMENT_ROOT"));
        this.contentType = notNull(env.get("CONTENT_TYPE"));
        this.contentLength = toInt(env.get("CONTENT_LENGTH"));

        parseLanguageAndPageName(pathInfo);

        try {
            parseGetParameters(queryString);
        } catch (final UnsupportedEncodingException e) {
            Log.framework().warn("Cannot parse Get Parameters", e);
        }

        this.httpAcceptLanguage = new AcceptedLanguages(notNull(env.get("HTTP_ACCEPT_LANGUAGE")));
    }

    private void parseLanguageAndPageName(String pathInfo) {
        // the script name is the first word
        final String[] splitedUri = removeFirstSlashes(pathInfo).split("/", 2);
        final String firstWord = splitedUri[0];

        if (firstWord.isEmpty()) {
            pageName = "";
            language = DEFAULT_LANG;
        } else if (LangExists(firstWord)) {
            language = firstWord;
            if (splitedUri.length > 1) {
                pageName = removeFirstSlashes(splitedUri[1]);
            } else {
                pageName = "";
            }
        } else {
            language = DEFAULT_LANG;
            pageName = removeFirstSlashes(pathInfo);
        }
    }

    private void parseGetParameters(String queryString) throws UnsupportedEncodingException {
        final String[] params = queryString.split("&");
        for (final String param : params) {
            final String[] pair = param.split("=", 2);
            if (pair.length == 2) {
                getParameters.add(URLDecoder.decode(pair[0], UTF_8), URLDecoder.decode(pair[1], UTF_8));
            }
        }
    }

    private String removeFirstSlashes(String pi) {
        while (pi.startsWith("/")) {
            pi = pi.substring(1);
        }
        return pi;
    }

    private boolean LangExists(final String langCode) {
        if (DEFAULT_LANG.equals(langCode)) {
            return true;
        }
        return AvailableLocales.getAvailableLangs().containsKey(langCode);
    }

    private int toInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String notNull(String v) {
        if (v == null) {
            return "";
        }
        return v;
    }

    @SuppressWarnings("unchecked")
    private List<String> toList(String stringValue, String separator) {
        if (stringValue == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(stringValue.split(separator));
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> toMap(String stringValue) {
        if (stringValue == null) {
            return Collections.EMPTY_MAP;
        }
        final HashMap<String, String> map = new HashMap<String, String>();
        final String[] namedValues = stringValue.split(";");
        for (final String namedValue : namedValues) {
            final String[] aValue = namedValue.split("=");
            if (aValue.length >= 2) {
                map.put(aValue[0].trim(), aValue[1].trim());
            } else {
                Log.framework().warn("Malformed cookie value: " + namedValue);
            }
        }
        return map;
    }

    public final String getHttpAuthorizationType() {
        final String[] splited = httpAuthorization.split(" ", 2);
        if (splited.length <= 1) {
            return AUTHORIZATION_UNKNOWN;
        }
        return splited[0];
    }

    public final String getHttpAuthorizationData() {
        final String[] splited = httpAuthorization.split(" ", 2);
        if (splited.length != 2) {
            return "";
        }
        return splited[1];
    }

    /**
     * @return the language code found in the page ('default', 'en' or 'fr')
     */
    public final String getPageLanguage() {
        return language;
    }

    public final String getPageName() {
        return pageName;
    }

    /** example : true */
    public final boolean isHttps() {
        String ishttps = env.get("HTTPS");
        return ishttps != null && ishttps.equals("on");
    }

    /**
     * Get get parameters formated with syntax as: ?plop=1&plip=2
     */
    public final Parameters getGetParameters() {
        return getParameters;
    }

    /** example : 0 */
    private final int contentLength;
    /**
     * example : multipart/form-data;
     * boundary=---------------------------1317007049440113364772076208
     */
    private final String contentType;
    /** example : /home/tom/bloatit/www */
    private final String documentRoot;
    /** example : CGI/1.1 */
    private final String gatewayInterface;
    /** example : Basic dGhvbWFzOnBsb3A= */
    private final String httpAuthorization;
    /** text/html,application/xhtml+xml,application/xml;q=0.9,* /*;q=0.8 */
    private final List<String> httpAccept;
    /** example : ISO-8859-1,utf-8;q=0.7,*;q=0.7 */
    private final List<String> httpAcceptCharset;
    /** example : gzip,deflate */
    private final List<String> httpAcceptEncoding;
    /** example : fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3 */
    private final AcceptedLanguages httpAcceptLanguage;
    /** example : max-age=0 */
    private final String httpCacheControl;
    /** example : keep-alive */
    private final String httpConnection;
    /** return value key map */
    private final Map<String, String> httpCookie;
    /** example : f2.b219.org:8081 */
    private final String httpHost;
    /** example : 115 */
    private final int httpKeepAlive;
    private final String httpReferer;
    private final String httpUserAgent;
    /** example : 1 */
    private final int httpXBehavioralAdOptOut;
    /** example : 1 */
    private final int httpXDoNotTrack;
    private final String pathInfo;
    private final String pathTranslated;
    /** example : lang=fr&page=payline&param=result-cancel/token-Eu */
    private final String queryString;
    /** example : 200 */
    private final int redirectStatus;
    /** example : bloatit?lang=fr&page=payline&param=result-cancel/token-Eu */
    private final String redirectUri;
    /** example : 192.168.0.254 */
    private final String remoteAddr;
    /** example : 52610 */
    private final int remotePort;
    /** example : GET */
    private final String requestMethod;
    /** example :/fr/payline/result-cancel?token=Eu */
    private final String requestUri;
    /** example : 1 */
    private final String scgi;
    /** example : /home/tom/bloatit/www/bloatit */
    private final String scriptFilename;
    /** example : /bloatit */
    private final String scriptName;
    /** example : 192.168.0.15 */
    private final String serverAddr;
    /** example : f2.b219.org */
    private final String serverName;
    /** example : 80 */
    private final int serverPort;
    /** example : HTTP/1.1 */
    private final String serverProtocol;
    /** example : lighttpd/1.4.26 */
    private final String serverSoftware;

    public final int getContentLength() {
        return contentLength;
    }

    public final String getContentType() {
        return contentType;
    }

    public final String getDocumentRoot() {
        return documentRoot;
    }

    public final String getGatewayInterface() {
        return gatewayInterface;
    }

    public final String getHttpAuthorization() {
        return httpAuthorization;
    }

    public final List<String> getHttpAccept() {
        return httpAccept;
    }

    public final List<String> getHttpAcceptCharset() {
        return httpAcceptCharset;
    }

    public final List<String> getHttpAcceptEncoding() {
        return httpAcceptEncoding;
    }

    public final AcceptedLanguages getHttpAcceptLanguage() {
        return httpAcceptLanguage;
    }

    public final String getHttpCacheControl() {
        return httpCacheControl;
    }

    public final String getHttpConnection() {
        return httpConnection;
    }

    public final Map<String, String> getHttpCookie() {
        return httpCookie;
    }

    public final String getHttpHost() {
        return httpHost;
    }

    public final int getHttpKeepAlive() {
        return httpKeepAlive;
    }

    public final String getHttpReferer() {
        return httpReferer;
    }

    public final String getHttpUserAgent() {
        return httpUserAgent;
    }

    public final int getHttpXBehavioralAdOptOut() {
        return httpXBehavioralAdOptOut;
    }

    public final int getHttpXDoNotTrack() {
        return httpXDoNotTrack;
    }

    public final String getPathInfo() {
        return pathInfo;
    }

    public final String getPathTranslated() {
        return pathTranslated;
    }

    public final String getQueryString() {
        return queryString;
    }

    public final int getRedirectStatus() {
        return redirectStatus;
    }

    public final String getRedirectUri() {
        return redirectUri;
    }

    public final String getRemoteAddr() {
        return remoteAddr;
    }

    public final int getRemotePort() {
        return remotePort;
    }

    public final String getRequestMethod() {
        return requestMethod;
    }

    public final String getRequestUri() {
        return requestUri;
    }

    public final String getScgi() {
        return scgi;
    }

    public final String getScriptFilename() {
        return scriptFilename;
    }

    public final String getScriptName() {
        return scriptName;
    }

    public final String getServerAddr() {
        return serverAddr;
    }

    public final String getServerName() {
        return serverName;
    }

    public final int getServerPort() {
        return serverPort;
    }

    public final String getServerProtocol() {
        return serverProtocol;
    }

    public final String getServerSoftware() {
        return serverSoftware;
    }
}
