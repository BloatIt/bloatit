package com.bloatit.rest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.framework.webserver.components.writers.QueryResponseStream;
import com.bloatit.framework.webserver.masters.HttpResponse;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;
import com.bloatit.rest.RestServer.RequestMethod;

/**
 * <p>
 * Base class to represent a ReST Resource
 * </p>
 * <p>
 * ReST (Representational State Transfer) is a style of software architecture
 * for distributed hypermedia systems such as the World Wide Web. The term
 * Representational State Transfer was introduced and defined in 2000 by Roy
 * Fielding in his doctoral dissertation. Fielding is one of the principal
 * authors of the Hypertext Transfer Protocol (HTTP) specification versions 1.0
 * and 1.1. (source: wikipedia)
 * </p>
 * <p>
 * Any rest resource must extend this class. They can then implement their
 * choices of methods from :
 * <li>{@link #doGet()}</li>
 * <li>{@link #doPost()}</li>
 * <li>{@link #doPut()}</li>
 * <li>{@link #doDelete()}</li> Any method not implemented will throw a
 * <code>NotAvailableRestMethodException</code>. The server will in turn inform
 * the client that this method is not available for this resource.<br />
 * <b>NOTE</b>: The <code>NotAvailableRestMethodException</code> means the
 * method is not available for this resource. It doesn't mean the client can't
 * access the method with its current rights.
 * </p>
 * <p>
 * Children classes need to use the {@link #add(XmlNode)} method to add xml
 * content to the RestResource. The generated Xml will be sent to the client
 * (with a header).
 * </p>
 */
public abstract class RestResource {

    /**
     * The list of nodes for this resource
     */
    private List<XmlNode> nodes;

    /**
     * The method used in the Request (<code>GET, POST, PUT, DELETE</code>)
     */
    protected RequestMethod requestMethod;

    /**
     * <p>
     * A request to access to a <code>ReST</code> resource is created.
     * </p>
     * 
     * @param requestMethod the method used for access, must be either
     *            <code>GET</code>, <code>POST</code>, <code>PUT </code>or
     *            <code>DELETE</code>
     * @see #doGet()
     * @see #doPost()
     * @see #doPut()
     * @see #doDelete()
     */
    public RestResource(RequestMethod requestMethod) {
        super();
        this.requestMethod = requestMethod;
        nodes = new ArrayList<XmlNode>();
    }

    public final void writeToHttp(final HttpResponse response) throws RestException, IOException {
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
    }

    /**
     * <p>
     * Adds a new node to the resource
     * </p>
     * 
     * @param node the node to add
     */
    public void add(XmlNode node) {
        nodes.add(node);
    }

    /**
     * @param outputStream the stream used to output the text
     */
    public void write(QueryResponseStream outputStream) {
        for (XmlNode node : nodes) {
            node.write(outputStream);
        }
    }

    /**
     * <p>
     * A method to implement that is used when answering a <code>GET</code>
     * message
     * </p>
     * <p>
     * <code>GET</code>s are used
     * <li>With no parameter to: Retrieve a representation of the addressed
     * member of the collection, expressed in an appropriate Internet media
     * type.</li>
     * <li>With a resource id to : List the URIs and perhaps other details of
     * the collection's members.</li>
     * </p>
     * 
     * @throws RestException when this method is not available for this rest
     *             resource (other methods may be implemented)
     * @throws InvalidRightException when the rest client doens't have
     *             sufficient rights to access this resource
     */
    protected void doGet() throws RestException {
        throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "GET doesn't exist for this ReST resource. Available for this resource: " + getAvailable() );
    }

    /**
     * <p>
     * A method to implement that is used when answering a <code>POST</code>
     * message
     * </p>
     * <p>
     * <code>POST</code>s are used
     * <li>With no parameter to: Treat the addressed member as a collection in
     * its own right and create a new entry in it.</li>
     * <li>With a resource id to : Create a new entry in the collection. The new
     * entry's URL is assigned automatically and is usually returned by the
     * operation.</li>
     * </p>
     * 
     * @throws RestException when this method is not available for this rest
     *             resource (other methods may be implemented)
     * @throws InvalidRightException when the rest client doens't have
     *             sufficient rights to access this resource
     */
    protected void doPost() throws RestException {
        throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "POST doesn't exist for this ReST resource. Available for this resource: :" + getAvailable());
    }

    /**
     * <p>
     * A method to implement that is used when answering a <code>PUT</code>
     * message
     * </p>
     * <p>
     * <code>PUT</code>s are used
     * <li>With no parameter to: Replace the addressed member of the collection,
     * or if it doesn't exist, create it.</li>
     * <li>With a resource id to : Replace the entire collection with another
     * collection.</li>
     * </p>
     * 
     * @throws RestException when this method is not available for this rest
     *             resource (other methods may be implemented)
     * @throws InvalidRightException when the rest client doens't have
     *             sufficient rights to access this resource
     */
    protected void doPut() throws RestException {
        throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "PUT doesn't exist for this ReST resource. Available for this resource: :" + getAvailable());
    }

    /**
     * <p>
     * A method to implement that is used when answering a <code>DELETE</code>
     * message
     * </p>
     * <p>
     * <code>DELETE</code>s are used
     * <li>With no parameter to: Delete the addressed member of the collection.</li>
     * <li>With a resource id to : Delete the entire collection.</li>
     * </p>
     * 
     * @throws RestException when this method is not available for this rest
     *             resource (other methods may be implemented)
     * @throws InvalidRightException when the rest client doens't have
     *             sufficient rights to access this resource
     */
    protected void doDelete() throws RestException {
        throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "DELETE doesn't exist for this ReST resource. Available for this resource: :" + getAvailable());
    }

    private String getAvailable() {
        Class<?> clazz = this.getClass();
        String result = "";
        for (Method method : clazz.getDeclaredMethods()) {
            String name = method.getName();
            if (name.equals("doGet") || name.equals("doPost") || name.equals("doPut") || name.equals("doDelete")) {
                result += name;
            }
        }
        
        return result;
    }
}
