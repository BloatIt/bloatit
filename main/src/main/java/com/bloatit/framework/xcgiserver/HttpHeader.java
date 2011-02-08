package com.bloatit.framework.xcgiserver;

import java.util.List;
import java.util.Map;

import com.bloatit.framework.xcgiserver.LazyLoaders.LazyInt;
import com.bloatit.framework.xcgiserver.LazyLoaders.LazyMap;
import com.bloatit.framework.xcgiserver.LazyLoaders.LazyString;
import com.bloatit.framework.xcgiserver.LazyLoaders.LazyStringList;

public class HttpHeader {

    /**
     * example : fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3
     */
    private final LazyStringList httpAcceptLanguage = new LazyStringList("HTTP_ACCEPT_LANGUAGE", ",");

    /**
     * example : Mozilla/5.0 (X11; U; Linux i686; fr; rv:1.9.2.14pre) Gecko/20110107
     * Ubuntu/10.04 (lucid) Namoroka/3.6.14pre
     */
    private final LazyString httpUserAgent = new LazyString("HTTP_USER_AGENT");

    /**
     * example : max-age=0
     */
    private final LazyString httpCacheControl = new LazyString("HTTP_CACHE_CONTROL");

    /**
     * example : text/html,application/xhtml+xml,application/xml;q=0.9,* /*;q=0.8
     */
    private final LazyStringList httpAccept = new LazyStringList("HTTP_ACCEPT", ",");

    /**
     * example : 115
     */
    private final LazyInt httpKeepAlive = new LazyInt("HTTP_KEEP_ALIVE");

    /**
     * example : 52610
     */
    private final LazyInt remotePort = new LazyInt("REMOTE_PORT");
    /**
     * example : gzip,deflate
     */
    private final LazyStringList httpAcceptEncoding = new LazyStringList("HTTP_ACCEPT_ENCODING", ",");
    /**
     * example : f2.b219.org
     */
    private final LazyString serverName = new LazyString("SERVER_NAME");
    /**
     * example : lighttpd/1.4.26
     */
    private final LazyString serverSoftware = new LazyString("SERVER_SOFTWARE");
    /**
     * example : 200
     */
    private final LazyInt redirectStatus = new LazyInt("REDIRECT_STATUS");
    /**
     * example : /home/tom/bloatit/www/bloatit
     */
    private final LazyString scriptFilename = new LazyString("SCRIPT_FILENAME");
    /**
     * example : SESS87038d7e49409b9d700737bd60ea56e8=b78427f651202b9466f2e01545458902;
     * session_key =b30e0cca4d46fe4194f891358ff5d8d48343de0013cd228e4daedae21415030d
     */
    private final LazyMap httpCookie = new LazyMap("HTTP_COOKIE");
    /**
     * example : 192.168.0.15
     */
    private final LazyString serverAddr = new LazyString("SERVER_ADDR");
    /**
     * example : HTTP/1.1
     */
    private final LazyString serverProtocol = new LazyString("SERVER_PROTOCOL");
    /**
     * example : GET
     */
    private final LazyString requestMethod = new LazyString("REQUEST_METHOD");
    /**
     * example : bloatit?lang=fr&page=payline&param=result-cancel/token-
     * EuuqQRn7AiPNrfqT7D0w1294355479323
     */
    private final LazyString redirectUri = new LazyString("REDIRECT_URI");
    /**
     * example : 80
     */
    private final LazyInt serverPort = new LazyInt("SERVER_PORT");
    /**
     * example : 1
     */
    private final LazyInt httpXBehavioralAdOptOut = new LazyInt("HTTP_X_BEHAVIORAL_AD_OPT_OUT");
    /**
     * example : /bloatit
     */
    private final LazyString scriptName = new LazyString("SCRIPT_NAME");
    /**
     * example : 192.168.0.254
     */
    private final LazyString remoteAddr = new LazyString("REMOTE_ADDR");
    /**
     * example : /home/tom/bloatit/www
     */
    private final LazyString documentRoot = new LazyString("DOCUMENT_ROOT");
    /**
     * example : ISO-8859-1,utf-8;q=0.7,*;q=0.7
     */
    private final LazyStringList httpAcceptCharset = new LazyStringList("HTTP_ACCEPT_CHARSET", ",|;");
    /**
     * example : keep-alive
     */
    private final LazyString httpConnection = new LazyString("HTTP_CONNECTION");
    /**
     * example : f2.b219.org:8081
     */
    private final LazyString httpHost = new LazyString("HTTP_HOST");
    private final LazyString pathInfo = new LazyString("PATH_INFO");
    /**
     * example : 1
     */
    private final LazyString scgi = new LazyString("SCGIUtils");
    /**
     * example : lang=fr&page=payline&param=result-cancel/token-
     * EuuqQRn7AiPNrfqT7D0w1294355479323
     */
    private Query queryString = null;
    /**
     * example : CGI/1.1
     */
    private final LazyString gatewayInterface = new LazyString("GATEWAY_INTERFACE");
    /**
     * example : 0
     */
    private final LazyInt contentLength = new LazyInt("CONTENT_LENGTH");
    /**
     * example : /fr/payline/result-cancel?token=EuuqQRn7AiPNrfqT7D0w1294355479323
     */
    private final LazyString requestUri = new LazyString("REQUEST_URI");
    /**
     * example : 1
     */
    private final LazyInt httpXDoNotTrack = new LazyInt("HTTP_X_DO_NOT_TRACK");

    /**
     * example : multipart/form-data;
     * boundary=---------------------------1317007049440113364772076208
     */
    private final LazyString contentType = new LazyString("CONTENT_TYPE");

    private final Map<String, String> env;

    public HttpHeader(final Map<String, String> env) {
        super();
        this.env = env;
        queryString = new Query(env.get("QUERY_STRING"));
    }

    public final String getPathInfo() {
        return pathInfo.getValue(env);
    }

    public final String getContentType() {
        return contentType.getValue(env);
    }

    public final List<String> getHttpAcceptLanguage() {
        return httpAcceptLanguage.getValue(env);
    }

    public final String getHttpUserAgent() {
        return httpUserAgent.getValue(env);
    }

    public final String getHttpCacheControl() {
        return httpCacheControl.getValue(env);
    }

    public final List<String> getHttpAccept() {
        return httpAccept.getValue(env);
    }

    public final int getHttpKeepAlive() {
        return httpKeepAlive.getValue(env);
    }

    public final int getRemotePort() {
        return remotePort.getValue(env);
    }

    public final String getServerName() {
        return serverName.getValue(env);
    }

    public final String getServerSoftware() {
        return serverSoftware.getValue(env);
    }

    public final int getRedirectStatus() {
        return redirectStatus.getValue(env);
    }

    public final String getScriptFilename() {
        return scriptFilename.getValue(env);
    }

    public final String getServerAddr() {
        return serverAddr.getValue(env);
    }

    public final String getServerProtocol() {
        return serverProtocol.getValue(env);
    }

    public final String getRequestMethod() {
        return requestMethod.getValue(env);
    }

    public final String getRedirectUri() {
        return redirectUri.getValue(env);
    }

    public final int getServerPort() {
        return serverPort.getValue(env);
    }

    public final int getHttpXBehavioralAdOptOut() {
        return httpXBehavioralAdOptOut.getValue(env);
    }

    public final String getScriptName() {
        return scriptName.getValue(env);
    }

    public final String getRemoteAddr() {
        return remoteAddr.getValue(env);
    }

    public final String getDocumentRoot() {
        return documentRoot.getValue(env);
    }

    public final List<String> getHttpAcceptCharset() {
        return httpAcceptCharset.getValue(env);
    }

    public final String getHttpConnection() {
        return httpConnection.getValue(env);
    }

    public final String getHttpHost() {
        return httpHost.getValue(env);
    }

    public final String getScgi() {
        return scgi.getValue(env);
    }

    public final Query getQueryString() {
        return queryString;
    }

    public final String getGatewayInterface() {
        return gatewayInterface.getValue(env);
    }

    public final int getContentLength() {
        return contentLength.getValue(env);
    }

    public final String getRequestUri() {
        return requestUri.getValue(env);
    }

    public final int getHttpXDoNotTrack() {
        return httpXDoNotTrack.getValue(env);
    }

    public final List<String> getHttpAcceptEncoding() {
        return httpAcceptEncoding.getValue(env);
    }

    public final Map<String, String> getHttpCookie() {
        return httpCookie.getValue(env);
    }

}
