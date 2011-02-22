package com.bloatit.framework.rest;

import java.io.IOException;
import java.util.Set;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.SessionManager;
import com.bloatit.framework.webserver.masters.HttpResponse;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;
import com.bloatit.framework.xcgiserver.HttpHeader;
import com.bloatit.framework.xcgiserver.HttpPost;
import com.bloatit.framework.xcgiserver.XcgiProcessor;

/**
 * <p>
 * Rest server is an asbtract class describing the functions of a server
 * handling RPC calls via ReST
 * </p>
 * <p>
 * <b>Note</b>: This server is exclusive. It will consume any request part of
 * his {@link #getResourcesDirectories()}. Therefore if you want to have 2
 * different rest servers consuming different rest requests, ensure they handle
 * different rest directories. Otherwise the first server will always assume he
 * is the one that should handle the request, and return an error when he
 * doesn't know how to handle it properly.
 * </p>
 */
public abstract class RestServer implements XcgiProcessor {

    private static final String UTF_8 = "UTF-8";

    public enum RequestMethod {
        GET, POST, PUT, DELETE;
    }

    /**
     * <p>
     * Constructs a new server in charge of handling rest requests
     * </p>
     */
    public RestServer() {
    }

    /**
     * <p>
     * Processes requests. If requests is a request to a <code>ReST</code>
     * resource, writes this resource to the <code>response</code>
     * </p>
     *
     * @return <code>true</code> if the request was a ReST request,
     *         <code>false</code> otherwise
     */
    @Override
    public final boolean process(HttpHeader httpHeader, HttpPost post, HttpResponse response) throws IOException {
        //TODO: blinder dans le cas ou même pas de slash
        final String scriptName = httpHeader.getScriptName().substring(1);

        boolean found = false;
        for (String directory : getResourcesDirectories()) {
            if (scriptName.equals(directory)) {
                found = true;
            }
        }
        if (!found) {
            return false;
        }

        RestHeader header = new RestHeader(httpHeader);

        String restResource = header.getResourceName();
        Log.rest().trace("Received a rest request for resource: " + restResource);

        RequestMethod requestMethod;
        String requestMethodString = httpHeader.getRequestMethod();
        if (requestMethodString == null) {
            Log.web().fatal("Received a rest request with no method. Should be either GET, POST, PUT or DELETE");
            return true;
        }

        try {
            requestMethod = RequestMethod.valueOf(requestMethodString);
        } catch (IllegalArgumentException e) {
            throw new FatalErrorException("Received an invalid request method. Should be either GET, POST, PUT or DELETE", e);
        }

        final Parameters parameters = header.getParameters();

        final Session session = findSession(httpHeader);

        RestResource resource = constructRestResource(restResource, requestMethod, parameters, session);
        if (resource == null) {
            try {
                response.setStatus(StatusCode.ERROR_404_NOT_FOUND);
                generateErrorResource(StatusCode.ERROR_404_NOT_FOUND, "Couldn't find resource " + restResource).writeToHttp(response);
                return true;
            } catch (RestException e) {
                // Should never happen
                throw new FatalErrorException("Couldn't generate a valid error message, got a Rest exception");
            }
        }

        try {
            resource.writeToHttp(response);
            Log.rest().trace("Wrote resource: " + resource + " for method " + requestMethod);
        } catch (RestException e) {
            try {
                Log.rest().warn(requestMethod + " " + resource + " not available: " + e.getStatus());
                response.setStatus(e.getStatus());
                generateErrorResource(e).writeToHttp(response);
                return true;
            } catch (RestException e1) {
                // Should never happen
                throw new FatalErrorException("Couldn't generate a valid error message, got a Rest exception");
            }
        }

        return true;
    }

    /**
     * <p>
     * Indicates the list of <i>directories</i> that can contain a
     * <code>ReST</code> resource
     * </p>
     * <p>
     * Example: if structure is
     * <code>http://example.com/rest/{@code <resource>}</code> or
     * <code>http://example.com/ws/{@code <resource>}</code> then this method
     * will return a set containing {"rest", "ws"}.
     * </p>
     *
     * @return a list of directories that can contain ReST resources
     */
    protected abstract Set<String> getResourcesDirectories();

    /**
     * Generates a matching <code>RestResource</code> for the given
     * <code>statusCode</code> and <code>message</code>
     *
     * @param statusCode the error code
     * @param message the error message
     * @return the RestResource used to inform the client he encountered an
     *         error
     */
    protected abstract RestResource generateErrorResource(StatusCode status, String message);

    /**
     * Generates a matching <code>RestResource</code> for the given
     * <code>RestException</code>
     *
     * @param exception the exception underlying the creation of this rest
     *            resource
     * @return the RestResource used to inform the client he encountered an
     *         error
     */
    protected abstract RestResource generateErrorResource(RestException exception);

    /**
     * <p>
     * Constructs the ReSt resource matching the <code>pageCode</code>
     * </p>
     * <p>
     * Contract :
     * <li>This method is <b>never</b> called if the pageCode does not match a
     * <code>ReST</code> resource. Rest resources always start with a String
     * grom {@link #getResourcesDirectories()}</li>
     * <li>This method never returns an error page, but returns
     * <code>null</code> whenever an error happens. Server is in charge of
     * generating error messages.
     * </p>
     *
     * @param pageCode the name of the page trying to be accessed. It always
     *            start with one of the string included in
     *            {@link #getResourcesDirectories()}. For example rest/demands
     * @param requestMethod
     * @param params the parameters of the request: <code>POST</code> +
     *            <code>GET</code> + <code>http Header</code> parameters
     * @param session the user session
     * @return Rest resource matching the <code>pageCode</code>, or
     *         <code>null</code> if no ReST resource exists.
     */
    protected abstract RestResource constructRestResource(final String pageCode,
                                                          RequestMethod requestMethod,
                                                          final Parameters params,
                                                          final Session session);

    /**
     * Finds the session based on header informations (mainly cookies)
     *
     * @param header the header of the request
     * @return the user session, or a new session if he didn't have one
     */
    private Session findSession(final HttpHeader header) {
        final String key = header.getHttpCookie().get("session_key");
        Session sessionByKey = null;
        if (key != null && (sessionByKey = SessionManager.getByKey(key)) != null) {
            if (sessionByKey.isExpired()) {
                SessionManager.destroySession(sessionByKey);
                // A new session will be create
            } else {
                sessionByKey.resetExpirationTime();
                return sessionByKey;
            }
        }
        return SessionManager.createSession();
    }
}
