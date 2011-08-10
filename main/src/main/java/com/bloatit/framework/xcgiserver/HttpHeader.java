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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.utils.i18n.Localizator.LanguageDescriptor;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.xcgiserver.LazyLoaders.LazyInt;
import com.bloatit.framework.xcgiserver.LazyLoaders.LazyMap;
import com.bloatit.framework.xcgiserver.LazyLoaders.LazyString;
import com.bloatit.framework.xcgiserver.LazyLoaders.LazyStringList;

public class HttpHeader {

    public static final String AUTHORIZATION_UNKNOWN = "unknown";

    private static final String DEFAULT_LANG = "default";

    private static final String UTF_8 = "UTF-8";

    private final Parameters getParameters = new Parameters();
    private String language = "en";
    private String pageName = "";
    private final Map<String, String> env;

    protected HttpHeader(final Map<String, String> env) {
        super();
        this.env = env;

        System.out.println(env);
        
        parseLanguageAndPageName();

        try {
            parseGetParameters();
        } catch (final UnsupportedEncodingException e) {
            Log.framework().warn("Cannot parse Get Parameters", e);
        }
    }

    private void parseGetParameters() throws UnsupportedEncodingException {
        final String[] params = getQueryString().split("&");
        for (final String param : params) {
            final String[] pair = param.split("=", 2);
            if (pair.length == 2) {
                getParameters.add(URLDecoder.decode(pair[0], UTF_8), URLDecoder.decode(pair[1], UTF_8));
            }
        }
    }

    private void parseLanguageAndPageName() {
        // the script name is the first word
        final String[] splitedUri = removeFirstSlashes(getPathInfo()).split("/", 2);
        final String firstWord = splitedUri[0];

        if (firstWord.isEmpty()) {
            pageName = "index";
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
            pageName = removeFirstSlashes(getPathInfo());
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
        Localizator.getAvailableLanguages();
        for (final Entry<String, LanguageDescriptor> lang : Localizator.getAvailableLanguages().entrySet()) {
            if (lang.getValue().getCode().equals(langCode)) {
                return true;
            }
        }
        return false;
    }

    public final String getLanguage() {
        return language;
    }

    public final String getPageName() {
        return pageName;
    }

    /**
     * Get get parameters formated with syntax as: ?plop=1&plip=2
     */
    public final Parameters getGetParameters() {
        return getParameters;
    }

    private final LazyInt contentLength = new LazyInt("CONTENT_LENGTH");

    /**
     * example : 0
     */
    public final int getContentLength() {
        return contentLength.getValue(env);
    }

    private final LazyString contentType = new LazyString("CONTENT_TYPE");

    /**
     * example : multipart/form-data;
     * boundary=---------------------------1317007049440113364772076208
     */
    public final String getContentType() {
        return contentType.getValue(env);
    }

    private final LazyString documentRoot = new LazyString("DOCUMENT_ROOT");

    /**
     * example : /home/tom/bloatit/www
     */
    public final String getDocumentRoot() {
        return documentRoot.getValue(env);
    }

    private final LazyString gatewayInterface = new LazyString("GATEWAY_INTERFACE");

    /**
     * example : CGI/1.1
     */
    public final String getGatewayInterface() {
        return gatewayInterface.getValue(env);
    }

    private final LazyString httpAuthorization = new LazyString("HTTP_AUTHORIZATION");

    /**
     * example : Basic dGhvbWFzOnBsb3A=
     */
    public final String getHttpAuthorization() {
        return httpAuthorization.getValue(env);
    }

    public final String getHttpAuthorizationType() {
        final String[] splited = httpAuthorization.getValue(env).split(" ", 2);
        if (splited.length <= 1) {
            return AUTHORIZATION_UNKNOWN;
        }
        return splited[0];
    }

    public final String getHttpAuthorizationData() {
        final String[] splited = httpAuthorization.getValue(env).split(" ", 2);
        if (splited.length != 2) {
            return "";
        }
        return splited[1];
    }

    private final LazyStringList httpAccept = new LazyStringList("HTTP_ACCEPT", ",");

    /**
     * example : text/html,application/xhtml+xml,application/xml;q=0.9,*
     * /*;q=0.8
     */
    public final List<String> getHttpAccept() {
        return httpAccept.getValue(env);
    }

    private final LazyStringList httpAcceptCharset = new LazyStringList("HTTP_ACCEPT_CHARSET", ",|;");

    /**
     * example : ISO-8859-1,utf-8;q=0.7,*;q=0.7
     */
    public final List<String> getHttpAcceptCharset() {
        return httpAcceptCharset.getValue(env);
    }

    private final LazyStringList httpAcceptEncoding = new LazyStringList("HTTP_ACCEPT_ENCODING", ",");

    /**
     * example : gzip,deflate
     */
    public final List<String> getHttpAcceptEncoding() {
        return httpAcceptEncoding.getValue(env);
    }

    private final LazyStringList httpAcceptLanguage = new LazyStringList("HTTP_ACCEPT_LANGUAGE", ",");

    /**
     * example : fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3
     */
    public final List<String> getHttpAcceptLanguage() {
        return httpAcceptLanguage.getValue(env);
    }

    private final LazyString httpCacheControl = new LazyString("HTTP_CACHE_CONTROL");

    /**
     * example : max-age=0
     */
    public final String getHttpCacheControl() {
        return httpCacheControl.getValue(env);
    }

    private final LazyString httpConnection = new LazyString("HTTP_CONNECTION");

    /**
     * example : keep-alive
     */
    public final String getHttpConnection() {
        return httpConnection.getValue(env);
    }

    /**
     * example : true
     */
    public final boolean isHttps() {
        String ishttps = env.get("HTTPS");
        return ishttps != null && ishttps.equals("on");
    }

    private final LazyMap httpCookie = new LazyMap("HTTP_COOKIE");

    /**
     * example :
     * SESS87038d7e49409b9d700737bd60ea56e8=b78427f651202b9466f2e01545458902;
     * session_key
     * =b30e0cca4d46fe4194f891358ff5d8d48343de0013cd228e4daedae21415030d
     */
    public final Map<String, String> getHttpCookie() {
        return httpCookie.getValue(env);
    }

    private final LazyString httpHost = new LazyString("HTTP_HOST");

    /**
     * example : f2.b219.org:8081
     */
    public final String getHttpHost() {
        return httpHost.getValue(env);
    }

    private final LazyInt httpKeepAlive = new LazyInt("HTTP_KEEP_ALIVE");

    /**
     * example : 115
     */
    public final int getHttpKeepAlive() {
        return httpKeepAlive.getValue(env);
    }

    private final LazyString httpReferer = new LazyString("HTTP_REFERER");

    public String getHttpReferer() {
        return httpReferer.getValue(env);
    }

    private final LazyString httpUserAgent = new LazyString("HTTP_USER_AGENT");

    /**
     * example : Mozilla/5.0 (X11; U; Linux i686; fr; rv:1.9.2.14pre)
     * Gecko/20110107 Ubuntu/10.04 (lucid) Namoroka/3.6.14pre
     */
    public final String getHttpUserAgent() {
        return httpUserAgent.getValue(env);
    }

    private final LazyInt httpXBehavioralAdOptOut = new LazyInt("HTTP_X_BEHAVIORAL_AD_OPT_OUT");

    /**
     * example : 1
     */
    public final int getHttpXBehavioralAdOptOut() {
        return httpXBehavioralAdOptOut.getValue(env);
    }

    private final LazyInt httpXDoNotTrack = new LazyInt("HTTP_X_DO_NOT_TRACK");

    /**
     * example : 1
     */
    public final int getHttpXDoNotTrack() {
        return httpXDoNotTrack.getValue(env);
    }

    private final LazyString pathInfo = new LazyString("PATH_INFO");

    public final String getPathInfo() {
        return pathInfo.getValue(env);
    }

    private final LazyString pathTranslated = new LazyString("PATH_TRANSLATED");

    public String getPathTranslated() {
        return pathTranslated.getValue(env);
    }

    private final LazyString queryString = new LazyString("QUERY_STRING");

    /**
     * example : lang=fr&page=payline&param=result-cancel/token-
     * EuuqQRn7AiPNrfqT7D0w1294355479323
     */
    public final String getQueryString() {
        return queryString.getValue(env);
    }

    private final LazyInt redirectStatus = new LazyInt("REDIRECT_STATUS");

    /**
     * example : 200
     */
    public final int getRedirectStatus() {
        return redirectStatus.getValue(env);
    }

    private final LazyString redirectUri = new LazyString("REDIRECT_URI");

    /**
     * example : bloatit?lang=fr&page=payline&param=result-cancel/token-
     * EuuqQRn7AiPNrfqT7D0w1294355479323
     */
    public final String getRedirectUri() {
        return redirectUri.getValue(env);
    }

    private final LazyString remoteAddr = new LazyString("REMOTE_ADDR");

    /**
     * example : 192.168.0.254
     */
    public final String getRemoteAddr() {
        return remoteAddr.getValue(env);
    }

    private final LazyInt remotePort = new LazyInt("REMOTE_PORT");

    /**
     * example : 52610
     */
    public final int getRemotePort() {
        return remotePort.getValue(env);
    }

    private final LazyString requestMethod = new LazyString("REQUEST_METHOD");

    /**
     * example : GET
     */
    public final String getRequestMethod() {
        return requestMethod.getValue(env);
    }

    private final LazyString requestUri = new LazyString("REQUEST_URI");

    /**
     * example :
     * /fr/payline/result-cancel?token=EuuqQRn7AiPNrfqT7D0w1294355479323
     */
    public final String getRequestUri() {
        return requestUri.getValue(env);
    }

    private final LazyString scgi = new LazyString("SCGIUtils");

    /**
     * example : 1
     */
    public final String getScgi() {
        return scgi.getValue(env);
    }

    private final LazyString scriptFilename = new LazyString("SCRIPT_FILENAME");

    /**
     * example : /home/tom/bloatit/www/bloatit
     */
    public final String getScriptFilename() {
        return scriptFilename.getValue(env);
    }

    private final LazyString scriptName = new LazyString("SCRIPT_NAME");

    /**
     * example : /bloatit
     */
    public final String getScriptName() {
        return scriptName.getValue(env);
    }

    private final LazyString serverAddr = new LazyString("SERVER_ADDR");

    /**
     * example : 192.168.0.15
     */
    public final String getServerAddr() {
        return serverAddr.getValue(env);
    }

    private final LazyString serverName = new LazyString("SERVER_NAME");

    /**
     * example : f2.b219.org
     */
    public final String getServerName() {
        return serverName.getValue(env);
    }

    private final LazyInt serverPort = new LazyInt("SERVER_PORT");

    /**
     * example : 80
     */
    public final int getServerPort() {
        return serverPort.getValue(env);
    }

    private final LazyString serverProtocol = new LazyString("SERVER_PROTOCOL");

    /**
     * example : HTTP/1.1
     */
    public final String getServerProtocol() {
        return serverProtocol.getValue(env);
    }

    private final LazyString serverSoftware = new LazyString("SERVER_SOFTWARE");

    /**
     * example : lighttpd/1.4.26
     */
    public final String getServerSoftware() {
        return serverSoftware.getValue(env);
    }
}
