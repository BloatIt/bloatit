package com.bloatit.framework.xcgiserver;

import java.io.IOException;
import java.io.OutputStream;

public class HttpReponseField {
    private static final byte[] SEPARATOR = ": ".getBytes();

    /**
     * Describes the error level
     */
    public enum StatusCode {
        /**
         * This means that the server has received the request headers, and that
         * the client should proceed to send the request body (in the case of a
         * request for which a body needs to be sent; for example, a POST
         * request). If the request body is large, sending it to a server when a
         * request has already been rejected based upon inappropriate headers is
         * inefficient. To have a server check if the request could be accepted
         * based on the request's headers alone, a client must send Expect:
         * 100-continue as a header in its initial request and check if a 100
         * Continue status code is received in response before continuing (or
         * receive 417 Expectation Failed and not continue).
         */
        INFORMATIONAL_100_CONTINUE("100", "Continue"),
        /**
         * This means the requester has asked the server to switch protocols and
         * the server is acknowledging that it will do so.
         */
        INFORMATIONAL_101_SWITCHING_Protocols("101", "Switching Protocols"),
        /**
         * As a WebDAV request may contain many sub-requests involving file
         * operations, it may take a long time to complete the request. This
         * code indicates that the server has received and is processing the
         * request, but no response is available yet. This prevents the client
         * from timing out and assuming the request was lost.
         */
        INFORMATIONAL_102_PROCESSING("102", "Processing"),
        /**
         * This is a non-standard IE7-only code which means the URI is longer
         * than a maximum of 2083 characters. (See code 414.)
         */
        INFORMATIONAL_122_REQUEST_URI_TOO_LONG("122", "Request-URI too long"),

        /**
         * Standard response for successful HTTP requests. The actual response
         * will depend on the request method used. In a GET request, the
         * response will contain an entity corresponding to the requested
         * resource. In a POST request the response will contain an entity
         * describing or containing the result of the action.
         */
        SUCCESS_200_OK("200", "OK"),
        /**
         * The request has been fulfilled and resulted in a new resource being
         * created.
         */
        SUCCESS_201_CREATED("201", "Created"),
        /**
         * The request has been accepted for processing, but the processing has
         * not been completed. The request might or might not eventually be
         * acted upon, as it might be disallowed when processing actually takes
         * place.
         */
        SUCCESS_202_ACCEPTED("202", "Accepted"),
        /**
         * The server successfully processed the request, but is returning
         * information that may be from another source.
         */
        SUCCESS_203_NON_AUTHORITATIVE_INFORMATION("203", "Non-Authoritative Information"),
        /**
         * The server successfully processed the request, but is not returning
         * any content.
         */
        SUCCESS_204_NO_CONTENT("204", "No Content"),
        /**
         * The server successfully processed the request, but is not returning
         * any content. Unlike a 204 response, this response requires that the
         * requester reset the document view.
         */
        SUCCESS_205_RESET_CONTENT("205", "Reset Content"),
        /**
         * The server is delivering only part of the resource due to a range
         * header sent by the client. The range header is used by tools like
         * wget to enable resuming of interrupted downloads, or split a download
         * into multiple simultaneous streams.
         */
        SUCCESS_206_PARTIAL_CONTENT("206", "Partial Content"),
        /**
         * The message body that follows is an XML message and can contain a
         * number of separate response codes, depending on how many sub-requests
         * were made.
         */
        SUCCESS_207_MULTI_STATUS("207", "Multi-Status"),
        /**
         * The server has fulfilled a GET request for the resource, and the
         * response is a representation of the result of one or more
         * instance-manipulations applied to the current instance.
         */
        SUCCESS_226_IM_USED("226", "IM Used"),

        /**
         * Indicates multiple options for the resource that the client may
         * follow. It, for instance, could be used to present different format
         * options for video, list files with different extensions, or word
         * sense disambiguation.
         */
        REDIRECTION_300_MULTIPLE_CHOICES("300", "Multiple Choices"),
        /** This and all future requests should be directed to the given URI. */
        REDIRECTION_301_MOVED_PERMANENTLY("301", "Moved Permanently"),
        /**
         * This is an example of industrial practice contradicting the standard.
         * HTTP/1.0 specification required the client to perform a temporary
         * redirect (the original describing phrase was "Moved Temporarily"),
         * but popular browsers implemented 302 with the functionality of a 303
         * See Other. Therefore, HTTP/1.1 added status codes 303 and 307 to
         * distinguish between the two behaviours. However, the majority of Web
         * applications and frameworks still[as of?] use the 302 status code as
         * if it were the 303.[citation needed]
         */
        REDIRECTION_302_FOUND("302", "Found"),
        /**
         * The response to the request can be found under another URI using a
         * GET method. When received in response to a POST (or PUT/DELETE), it
         * should be assumed that the server has received the data and the
         * redirect should be issued with a separate GET message.
         */
        REDIRECTION_303_SEE_OTHER("303", "See Other"),
        /**
         * Indicates the resource has not been modified since last requested.
         * Typically, the HTTP client provides a header like the
         * If-Modified-Since header to provide a time against which to compare.
         * Using this saves bandwidth and reprocessing on both the server and
         * client, as only the header data must be sent and received in
         * comparison to the entirety of the page being re-processed by the
         * server, then sent again using more bandwidth of the server and
         * client.
         */
        REDIRECTION_304_NOT_MODIFIED("304", "Not Modified"),
        /**
         * Many HTTP clients (such as Mozilla and Internet Explorer) do not
         * correctly handle responses with this status code, primarily for
         * security reasons.
         */
        REDIRECTION_305_USE_PROXY("305", "Use Proxy"),
        /** No longer used. */
        REDIRECTION_306_SWITCH_PROXY("306", "Switch Proxy"),
        /**
         * In this occasion, the request should be repeated with another URI,
         * but future requests can still use the original URI. In contrast to
         * 303, the request method should not be changed when reissuing the
         * original request. For instance, a POST request must be repeated using
         * another POST request.
         */
        REDIRECTION_307_TEMPORARY_REDIRECT("307", "Temporary Redirect"),

        /** The request cannot be fulfilled due to bad syntax. */
        ERROR_CLI_400_BAD_REQUEST("400", "Bad Request"),
        /**
         * Similar to 403 Forbidden, but specifically for use when
         * authentication is possible but has failed or not yet been provided.
         * The response must include a WWW-Authenticate header field containing
         * a challenge applicable to the requested resource. See Basic access
         * authentication and Digest access authentication.
         */
        ERROR_CLI_401_UNAUTHORIZED("401", "Unauthorized"),
        /**
         * Reserved for future use. The original intention was that this code
         * might be used as part of some form of digital cash or micropayment
         * scheme, but that has not happened, and this code is not usually used.
         * As an example of its use, however, Apple's MobileMe service generates
         * a 402 error ("httpStatusCode:402" in the Mac OS X Console log) if the
         * MobileMe account is delinquent.
         */
        ERROR_CLI_402_PAYMENT_REQUIRED("402", "Payment Required"),
        /**
         * The request was a legal request, but the server is refusing to
         * respond to it. Unlike a 401 Unauthorized response, authenticating
         * will make no difference.
         */
        ERROR_CLI_403_FORBIDDEN("403", "Forbidden"),
        /**
         * The requested resource could not be found but may be available again
         * in the future. Subsequent requests by the client are permissible.
         */
        ERROR_CLI_404_NOT_FOUND("404", "Not Found"),
        /**
         * A request was made of a resource using a request method not supported
         * by that resource; for example, using GET on a form which requires
         * data to be presented via POST, or using PUT on a read-only resource.
         */
        ERROR_CLI_405_METHOD_NOT_ALLOWED("405", "Method Not Allowed"),
        /**
         * The requested resource is only capable of generating content not
         * acceptable according to the Accept headers sent in the request.
         */
        ERROR_CLI_406_NOT_ACCEPTABLE("406", "Not Acceptable"),
        /**
         * 408 Request Timeout The server timed out waiting for the request.
         * According to W3 HTTP specifications:
         * "The client did not produce a request within the time that the server was prepared to wait. The client MAY repeat the request without modifications at any later time."
         */
        ERROR_CLI_407_PROXY_AUTHENTICATION_REQUIRED("407", "Proxy Authentication Required"),

        /**
         * Indicates that the request could not be processed because of conflict
         * in the request, such as an edit conflict.
         */
        ERROR_CLI_409_CONFLICT("409", "Conflict"),
        /**
         * Indicates that the resource requested is no longer available and will
         * not be available again. This should be used when a resource has been
         * intentionally removed and the resource should be purged. Upon
         * receiving a 410 status code, the client should not request the
         * resource again in the future. Clients such as search engines should
         * remove the resource from their indices. Most use cases do not require
         * clients and search engines to purge the resource, and a
         * "404 Not Found" may be used instead.
         */
        ERROR_CLI_410_GONE("410", "Gone"),
        /**
         * The request did not specify the length of its content, which is
         * required by the requested resource.
         */
        ERROR_CLI_411_LENGTH_REQUIRED("411", "Length Required"),
        /**
         * The server does not meet one of the preconditions that the requester
         * put on the request.
         */
        ERROR_CLI_412_PRECONDITION_FAILED("412", "Precondition Failed"),
        /** The request is larger than the server is willing or able to process. */
        ERROR_CLI_413_REQUEST_ENTITY_TOO_LARGE("413", "Request Entity Too Large"),
        /** The URI provided was too long for the server to process. */
        ERROR_CLI_414_REQUEST_URI_TOO_LONG("414", "Request-URI Too Long"),
        /**
         * The request entity has a media type which the server or resource does
         * not support. For example, the client uploads an image as
         * image/svg+xml, but the server requires that images use a different
         * format.
         */
        ERROR_CLI_415_UNSUPPORTED_MEDIA_TYPE("415", "Unsupported Media Type"),
        /**
         * The client has asked for a portion of the file, but the server cannot
         * supply that portion. For example, if the client asked for a part of
         * the file that lies beyond the end of the file.
         */
        ERROR_CLI_416_REQUESTED_RANGE_NOT_SATISFIABLE("416", "Requested Range Not Satisfiable"),
        /**
         * The server cannot meet the requirements of the Expect request-header
         * field.
         */
        ERROR_CLI_417_EXPECTATION_FAILED("417", "Expectation Failed"),
        /**
         * This code was defined in 1998 as one of the traditional IETF April
         * Fools' jokes, in RFC 2324, Hyper Text Coffee Pot Control Protocol,
         * and is not expected to be implemented by actual HTTP servers.
         */
        ERROR_CLI_418_I("418", "I"),
        /**
         * The request was well-formed but was unable to be followed due to
         * semantic errors.
         */
        ERROR_CLI_422_UNPROCESSABLE_ENTITY("422", "Unprocessable Entity"),
        /** The resource that is being accessed is locked. */
        ERROR_CLI_423_LOCKED("423", "Locked "),
        /**
         * The request failed due to failure of a previous request (e.g. a
         * PROPPATCH).
         */
        ERROR_CLI_424_FAILED_DEPENDENCY("424", "Failed Dependency"),
        /**
         * Defined in drafts of "WebDAV Advanced Collections Protocol", but not
         * present in
         * "Web Distributed Authoring and Versioning (WebDAV) Ordered Collections Protocol"
         * .
         */
        ERROR_CLI_425_UNORDERED_COLLECTION("425", "Unordered Collection"),
        /** The client should switch to a different protocol such as TLS/1.0. */
        ERROR_CLI_426_UPGRADE_REQUIRED("426", "Upgrade Required"),
        /**
         * A Nginx HTTP server extension. The server returns no information to
         * the client and closes the connection (useful as a deterrent for
         * malware).
         */
        ERROR_CLI_444_NO_RESPONSE("444", "No Response"),
        /**
         * A Microsoft extension. The request should be retried after performing
         * the appropriate action.
         */
        ERROR_CLI_449_RETRY_WITH("449", "Retry With"),
        /**
         * A Microsoft extension. This error is given when Windows Parental
         * Controls are turned on and are blocking access to the given webpage.
         */
        ERROR_CLI_450_BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS("450", "Blocked by Windows Parental Controls"),
        /**
         * An Nginx HTTP server extension. This code is introduced to log the
         * case when the connection is closed by client while HTTP server is
         * processing its request, making server unable to send the HTTP header
         * back.
         */
        ERROR_CLI_499_CLIENT_CLOSED_REQUEST("499", "Client Closed Request"),

        /**
         * A generic error message, given when no more specific message is
         * suitable.
         */
        ERROR_SERV_500_INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
        /**
         * The server either does not recognise the request method, or it lacks
         * the ability to fulfill the request.
         */
        ERROR_SERV_501_NOT_IMPLEMENTED("501", "Not Implemented"),
        /**
         * The server was acting as a gateway or proxy and received an invalid
         * response from the upstream server.
         */
        ERROR_SERV_502_BAD_GATEWAY("502", "Bad Gateway"),
        /**
         * The server is currently unavailable (because it is overloaded or down
         * for maintenance). Generally, this is a temporary state.
         */
        ERROR_SERV_503_SERVICE_UNAVAILABLE("503", "Service Unavailable"),
        /**
         * The server was acting as a gateway or proxy and did not receive a
         * timely response from the upstream server.
         */
        ERROR_SERV_504_GATEWAY_TIMEOUT("504", "Gateway Timeout"),
        /**
         * The server does not support the HTTP protocol version used in the
         * request.
         */
        ERROR_SERV_505_HTTP_VERSION_NOT_SUPPORTED("505", "HTTP Version Not Supported"),
        /**
         * Transparent content negotiation for the request results in a circular
         * reference.
         */
        ERROR_SERV_506_VARIANT_ALSO_NEGOTIATES("506", "Variant Also Negotiates"),
        /** 509 Bandwidth Limit Exceeded (Apache bw/limited extension) */
        ERROR_SERV_507_INSUFFICIENT_STORAGE("507", "Insufficient Storage"),
        /** Further extensions to the requ */
        ERROR_SERV_510_NOT_EXTENDED("510", "Not Extended"), ;//

        private final String code;
        private final String message;

        StatusCode(final String code, final String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return code + " " + message;
        }
    }

    private final String name;
    private final String value;

    public HttpReponseField(final String name, final String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public void write(final OutputStream stream) throws IOException {
        stream.write(name.getBytes());
        stream.write(SEPARATOR);
        stream.write(value.getBytes());
        stream.write(HttpResponse.EOL);
    }

    /**
     * Create a HttpResponseHeader with name "Accept-Ranges". This httpHeader is
     * : What partial content range types this server supports.
     * 
     * @param value can be for example "Accept-Ranges: bytes"
     * @return "Accept-Ranges" HttpResponseHeader
     */
    public static HttpReponseField acceptRanges(final String value) {
        return new HttpReponseField("Accept-Ranges", value);
    }

    /**
     * Create a HttpResponseHeader with name "Age". This httpHeader is : The age
     * the object has been in a proxy cache in seconds .
     * 
     * @param value can be for example "Age: 12"
     * @return "Age  " HttpResponseHeader
     */
    public static HttpReponseField age(final String value) {
        return new HttpReponseField("Age", value);
    }

    /**
     * Create a HttpResponseHeader with name "Allow". This httpHeader is : Valid
     * actions for a specified resource. To be used for a 405 Method not allowed
     * .
     * 
     * @param value can be for example "Allow: GET, HEAD"
     * @return "Allow" HttpResponseHeader
     */
    public static HttpReponseField allow(final String value) {
        return new HttpReponseField("Allow", value);
    }

    /**
     * Create a HttpResponseHeader with name "Cache-Control". This httpHeader is
     * : Tells all caching mechanisms from server to client whether they may
     * cache this object.
     * 
     * @param value can be for example "Cache-Control: max-age=3600"
     * @return "Cache-Control" HttpResponseHeader
     */
    public static HttpReponseField cacheControl(final String value) {
        return new HttpReponseField("Cache-Control", value);
    }

    /**
     * Create a HttpResponseHeader with name "Connection". This httpHeader is :
     * Options that are desired for the connection[4].
     * 
     * @param value can be for example "Connection: close"
     * @return "Connection" HttpResponseHeader
     */
    public static HttpReponseField connection(final String value) {
        return new HttpReponseField("Connection", value);
    }

    /**
     * Create a HttpResponseHeader with name "Content-Encoding". This httpHeader
     * is : The type of encoding used on the data.
     * 
     * @param value can be for example "Content-Encoding: gzip"
     * @return "Content-Encoding " HttpResponseHeader
     */
    public static HttpReponseField contentEncoding(final String value) {
        return new HttpReponseField("Content-Encoding", value);
    }

    /**
     * Create a HttpResponseHeader with name "Content-Language". This httpHeader
     * is : The language the content is in.
     * 
     * @param value can be for example "Content-Language: da"
     * @return "Content-Language  " HttpResponseHeader
     */
    public static HttpReponseField contentLanguage(final String value) {
        return new HttpReponseField("Content-Language", value);
    }

    /**
     * Create a HttpResponseHeader with name "Content-Length". This httpHeader
     * is : The length of the response body in octets (8-bit bytes).
     * 
     * @param value can be for example "Content-Length: 348"
     * @return 
     *         "Content-Length   The length of the response body in octets (8-bit bytes)"
     *         HttpResponseHeader
     */
    public static HttpReponseField contentLength(final String value) {
        return new HttpReponseField("Content-Length", value);
    }

    /**
     * Create a HttpResponseHeader with name "Content-Location". This httpHeader
     * is : An alternate location for the returned data .
     * 
     * @param value can be for example "Content-Location: /index.htm"
     * @return "Content-Location " HttpResponseHeader
     */
    public static HttpReponseField contentLocation(final String value) {
        return new HttpReponseField("Content-Location", value);
    }

    /**
     * Create a HttpResponseHeader with name "Content-MD5". This httpHeader is :
     * A Base64-encoded binary MD5 sum of the content of the response.
     * 
     * @param value can be for example "Content-MD5: Q2hlY2sgSW50ZWdyaXR5IQ=="
     * @return "Content-MD5" HttpResponseHeader
     */
    public static HttpReponseField contentMD5(final String value) {
        return new HttpReponseField("Content-MD5", value);
    }

    /**
     * Create a HttpResponseHeader with name "Content-Disposition". This
     * httpHeader is : An opportunity to raise a "File Download" dialogue box
     * for a known MIME type .
     * 
     * @param value can be for example
     *            "Content-Disposition: attachment; filename=fname.ext"
     * @return "Content-Disposition" HttpResponseHeader
     */
    public static HttpReponseField contentDisposition(final String value) {
        return new HttpReponseField("Content-Disposition", value);
    }

    /**
     * Create a HttpResponseHeader with name "Content-Range". This httpHeader is
     * : Where in a full body message this partial message belongs.
     * 
     * @param value can be for example "Content-Range: bytes 21010-47021/47022"
     * @return "Content-Range" HttpResponseHeader
     */
    public static HttpReponseField contentRange(final String value) {
        return new HttpReponseField("Content-Range", value);
    }

    /**
     * Create a HttpResponseHeader with name "Content-Type". This httpHeader is
     * : The mime type of this content.
     * 
     * @param value can be for example "Content-Type: text/html; charset=utf-8"
     * @return "Content-Type" HttpResponseHeader
     */
    public static HttpReponseField contentType(final String value) {
        return new HttpReponseField("Content-Type", value);
    }

    /**
     * Create a HttpResponseHeader with name "Date". This httpHeader is : The
     * date and time that the message was sent .
     * 
     * @param value can be for example "Date: Tue, 15 Nov 1994 08:12:31 GMT"
     * @return "Date" HttpResponseHeader
     */
    public static HttpReponseField date(final String value) {
        return new HttpReponseField("Date", value);
    }

    /**
     * Create a HttpResponseHeader with name "ETag". This httpHeader is : An
     * identifier for a specific version of a resource, often a Message Digest,
     * see ETag .
     * 
     * @param value can be for example
     *            "ETag: "737060cd8c284d8af7ad3082f209582d""
     * @return "ETag" HttpResponseHeader
     */
    public static HttpReponseField eTag(final String value) {
        return new HttpReponseField("ETag", value);
    }

    /**
     * Create a HttpResponseHeader with name "Expires". This httpHeader is :
     * Gives the date/time after which the response is considered stale .
     * 
     * @param value can be for example "Expires: Thu, 01 Dec 1994 16:00:00 GMT"
     * @return "Expires" HttpResponseHeader
     */
    public static HttpReponseField expires(final String value) {
        return new HttpReponseField("Expires", value);
    }

    /**
     * Create a HttpResponseHeader with name "Last-Modified". This httpHeader is
     * : The last modified date for the requested object, in RFC 2822 format .
     * 
     * @param value can be for example
     *            "Last-Modified: Tue, 15 Nov 1994 12:45:26 GMT"
     * @return "Last-Modified" HttpResponseHeader
     */
    public static HttpReponseField lastModified(final String value) {
        return new HttpReponseField("Last-Modified", value);
    }

    /**
     * Create a HttpResponseHeader with name "Link". This httpHeader is : Used
     * to express a typed relationship with another resource, where the relation
     * type is defined by RFC 5988 .
     * 
     * @param value can be for example "Link: </feed>; rel="alternate""
     * @return "Link" HttpResponseHeader
     */
    public static HttpReponseField link(final String value) {
        return new HttpReponseField("Link", value);
    }

    /**
     * Create a HttpResponseHeader with name "Location". This httpHeader is :
     * Used in redirection, or when a new resource has been created..
     * 
     * @param value can be for example
     *            "Location: http://www.w3.org/pub/WWW/People.html"
     * @return "Location" HttpResponseHeader
     */
    public static HttpReponseField location(final String value) {
        return new HttpReponseField("Location", value);
    }

    /**
     * Create a HttpResponseHeader with name "P3P". This httpHeader is : This
     * header is supposed to set P3P policy, in the form of
     * P3P:CP="your_compact_policy". However, P3P did not take off,[5] most
     * browsers have never fully implemented it, a lot of websites set this
     * header with fake policy text, that was enough to fool browsers the
     * existence of P3P policy and grant permissions for third party cookies..
     * 
     * @param value can be for example "P3P: CP="This is not a P3P policy! See
     *            http://www.google.com/support/accounts/bin/answer.py?hl
     *            =en&answer=151657 for more info.""
     * @return "P3P" HttpResponseHeader
     */
    public static HttpReponseField P3P(final String value) {
        return new HttpReponseField("P3P", value);
    }

    /**
     * Create a HttpResponseHeader with name "Pragma". This httpHeader is :
     * Implementation-specific headers that may have various effects anywhere
     * along the request-response chain. .
     * 
     * @param value can be for example "Pragma: no-cache"
     * @return "Pragma" HttpResponseHeader
     */
    public static HttpReponseField pragma(final String value) {
        return new HttpReponseField("Pragma", value);
    }

    /**
     * Create a HttpResponseHeader with name "Proxy-Authenticate". This
     * httpHeader is : Request authentication to access the proxy. .
     * 
     * @param value can be for example "Proxy-Authenticate: Basic"
     * @return "Proxy-Authenticate" HttpResponseHeader
     */
    public static HttpReponseField proxyAuthenticate(final String value) {
        return new HttpReponseField("Proxy-Authenticate", value);
    }

    /**
     * Create a HttpResponseHeader with name "Refresh". This httpHeader is :
     * Used in redirection, or when a new resource has been created. This
     * refresh redirects after 5 seconds.(This is a proprietary/non-standard
     * header extension introduced by Netscape and supported by most web
     * browsers.) .
     * 
     * @param value can be for example
     *            "Refresh: 5; url=http://www.w3.org/pub/WWW/People.html"
     * @return "Refresh" HttpResponseHeader
     */
    public static HttpReponseField refresh(final String value) {
        return new HttpReponseField("Refresh", value);
    }

    /**
     * Create a HttpResponseHeader with name "Retry-After". This httpHeader is :
     * If an entity is temporarily unavailable, this instructs the client to try
     * again after a specified period of time..
     * 
     * @param value can be for example "Retry-After: 120"
     * @return "Retry-After" HttpResponseHeader
     */
    public static HttpReponseField retryAfter(final String value) {
        return new HttpReponseField("Retry-After", value);
    }

    /**
     * Create a HttpResponseHeader with name "Server". This httpHeader is : A
     * name for the server .
     * 
     * @param value can be for example
     *            "Server: Apache/1.3.27 (Unix) (Red-Hat/Linux)"
     * @return "Server" HttpResponseHeader
     */
    public static HttpReponseField server(final String value) {
        return new HttpReponseField("Server", value);
    }

    /**
     * Create a HttpResponseHeader with name "Set-Cookie". This httpHeader is :
     * an HTTP cookie.
     * 
     * @param value can be for example
     *            "Set-Cookie: UserID=JohnDoe; Max-Age=3600; Version=1"
     * @return "Set-Cookie" HttpResponseHeader
     */
    public static HttpReponseField setCookie(final String value) {
        return new HttpReponseField("Set-Cookie", value);
    }

    /**
     * Create a HttpResponseHeader with name "Strict-Transport-Security". This
     * httpHeader is : A HSTS Policy informing the HTTP client how long to cache
     * the HTTPS only policy and whether this applies to subdomains. .
     * 
     * @param value can be for example
     *            "Strict-Transport-Security: max-age=16070400; includeSubDomains"
     * @return "Strict-Transport-Security" HttpResponseHeader
     */
    public static HttpReponseField strictTransportSecurity(final String value) {
        return new HttpReponseField("Strict-Transport-Security", value);
    }

    /**
     * Create a HttpResponseHeader with name "Trailer". This httpHeader is : The
     * Trailer general field value indicates that the given set of header fields
     * is present in the trailer of a message encoded with chunked
     * transfer-coding..
     * 
     * @param value can be for example "Trailer: Max-Forwards"
     * @return "Trailer" HttpResponseHeader
     */
    public static HttpReponseField trailer(final String value) {
        return new HttpReponseField("Trailer", value);
    }

    /**
     * Create a HttpResponseHeader with name "Transfer-Encoding". This
     * httpHeader is : The form of encoding used to safely transfer the entity
     * to the user. Currently defined methods are: chunked, compress, deflate,
     * gzip, identity. .
     * 
     * @param value can be for example "Transfer-Encoding: chunked"
     * @return "Transfer-Encoding" HttpResponseHeader
     */
    public static HttpReponseField transferEncoding(final String value) {
        return new HttpReponseField("Transfer-Encoding", value);
    }

    /**
     * Create a HttpResponseHeader with name "Vary". This httpHeader is : Tells
     * downstream proxies how to match future request headers to decide whether
     * the cached response can be used rather than requesting a fresh one from
     * the origin server..
     * 
     * @param value can be for example "Vary: *"
     * @return "Vary" HttpResponseHeader
     */
    public static HttpReponseField vary(final String value) {
        return new HttpReponseField("Vary", value);
    }

    /**
     * Create a HttpResponseHeader with name "Via". This httpHeader is : Informs
     * the client of proxies through which the response was sent..
     * 
     * @param value can be for example
     *            "Via: 1.0 fred, 1.1 nowhere.com (Apache/1.1)"
     * @return "Via" HttpResponseHeader
     */
    public static HttpReponseField via(final String value) {
        return new HttpReponseField("Via", value);
    }

    /**
     * Create a HttpResponseHeader with name "Warning". This httpHeader is : A
     * general warning about possible problems with the entity body. .
     * 
     * @param value can be for example "Warning: 199 Miscellaneous warning"
     * @return "Warning" HttpResponseHeader
     */
    public static HttpReponseField warning(final String value) {
        return new HttpReponseField("Warning", value);
    }

    /**
     * Create a HttpResponseHeader with name "WWW-Authenticate". This httpHeader
     * is : Indicates the authentication scheme that should be used to access
     * the requested entity. .
     * 
     * @param value can be for example "WWW-Authenticate: Basic"
     * @return "WWW-Authenticate" HttpResponseHeader
     */
    public static HttpReponseField wwwAuthenticate(final String value) {
        return new HttpReponseField("WWW-Authenticate", value);
    }

    public static HttpReponseField status(final StatusCode value) {
        return new HttpReponseField("status", value.toString());
    }
}
