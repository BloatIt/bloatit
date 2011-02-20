package com.bloatit.rest;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.webserver.masters.HttpResponse;
import com.bloatit.framework.webserver.masters.Linkable;

/**
 * <p>
 * Base class to represent a ReST Resource
 * </p>
 * <p>
 * ReST (Representational State Transfer) is a style of software architecture for
 * distributed hypermedia systems such as the World Wide Web. The term Representational
 * State Transfer was introduced and defined in 2000 by Roy Fielding in his doctoral
 * dissertation. Fielding is one of the principal authors of the Hypertext Transfer
 * Protocol (HTTP) specification versions 1.0 and 1.1.
 * </p>
 */
public abstract class RestResource implements Linkable {
    enum RequestMethod {
        GET, POST, PUT, DELETE;
    }

    protected RequestMethod requestMethod;

    /**
     * <p>
     * A request to access to a <code>ReST</code> resource is created.
     * </p>
     * 
     * @param requestMethod the method used for access, must be either <code>GET</code>,
     *        <code>POST</code>, <code>PUT </code>or <code>DELETE</code>
     * @see #doGet()
     * @see #doPost()
     * @see #doPut()
     * @see #doDelete()
     */
    public RestResource(final String requestMethod) {
        super();
        try {
            this.requestMethod = RequestMethod.valueOf(requestMethod);
        } catch (final IllegalArgumentException e) {
            throw new FatalErrorException(requestMethod + "is not a valid Request Method. Must be GET, POST, PUT or DELETE", e);
        }
    }

    @Override
    public final void writeToHttp(final HttpResponse response) throws IOException {
        try {
            switch (requestMethod) {
            case POST:
                doPost();
                break;
            case PUT:
                doPut();
                break;
            case DELETE:
                doDelete();
                break;
            case GET:
            default:
                doGet();
                break;
            }
            response.writeRestResource(this);
        } catch (final NotAvailableRestMethodException e) {
            Log.rest().error("Not available method " + requestMethod.toString() + " for this resource " + getClass(), e);
            // TODO write here 404 error
        } catch (final InvalidRightException e) {
            Log.rest().warn("User has unsifficient rights to use this method", e);
            // TODO write here insufficient right error
        }
    }

    public RestResource add() {

        return this;
    }

    /**
     * <p>
     * A method to implement that is used when answering a <code>GET</code> message
     * </p>
     * <p>
     * <code>GET</code>s are used
     * <li>With no parameter to: Retrieve a representation of the addressed member of the
     * collection, expressed in an appropriate Internet media type.</li>
     * <li>With a resource id to : List the URIs and perhaps other details of the
     * collection's members.</li>
     * </p>
     * 
     * @throws NotAvailableRestMethodException when this method is not available for this
     *         rest resource (other methods may be implemented)
     * @throws InvalidRightException when the rest client doens't have sufficient rights
     *         to access this resource
     */
    protected abstract void doGet() throws NotAvailableRestMethodException, InvalidRightException;

    /**
     * <p>
     * A method to implement that is used when answering a <code>POST</code> message
     * </p>
     * <p>
     * <code>POST</code>s are used
     * <li>With no parameter to: Treat the addressed member as a collection in its own
     * right and create a new entry in it.</li>
     * <li>With a resource id to : Create a new entry in the collection. The new entry's
     * URL is assigned automatically and is usually returned by the operation.</li>
     * </p>
     * 
     * @throws NotAvailableRestMethodException when this method is not available for this
     *         rest resource (other methods may be implemented)
     * @throws InvalidRightException when the rest client doens't have sufficient rights
     *         to access this resource
     */
    protected abstract void doPost() throws NotAvailableRestMethodException, InvalidRightException;

    /**
     * <p>
     * A method to implement that is used when answering a <code>PUT</code> message
     * </p>
     * <p>
     * <code>PUT</code>s are used
     * <li>With no parameter to: Replace the addressed member of the collection, or if it
     * doesn't exist, create it.</li>
     * <li>With a resource id to : Replace the entire collection with another collection.</li>
     * </p>
     * 
     * @throws NotAvailableRestMethodException when this method is not available for this
     *         rest resource (other methods may be implemented)
     * @throws InvalidRightException when the rest client doens't have sufficient rights
     *         to access this resource
     */
    protected abstract void doPut() throws NotAvailableRestMethodException, InvalidRightException;

    /**
     * <p>
     * A method to implement that is used when answering a <code>DELETE</code> message
     * </p>
     * <p>
     * <code>DELETE</code>s are used
     * <li>With no parameter to: Delete the addressed member of the collection.</li>
     * <li>With a resource id to : Delete the entire collection.</li>
     * </p>
     * 
     * @throws NotAvailableRestMethodException when this method is not available for this
     *         rest resource (other methods may be implemented)
     * @throws InvalidRightException when the rest client doens't have sufficient rights
     *         to access this resource
     */
    protected abstract void doDelete() throws NotAvailableRestMethodException, InvalidRightException;
}
