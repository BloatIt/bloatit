package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;
import com.bloatit.model.Project;
import com.bloatit.rest.list.RestDemandList;
import com.bloatit.rest.list.RestProjectList;

/**
 * <p>
 * Representation of a Project for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Project that needs to be called
 * through the ReST RPC. Every such method needs to be mapped with the
 * {@code @REST} interface.
 * <p>
 * ReST uses the four HTTP request methods <code>GET</code>, <code>POST</code>,
 * <code>PUT</code>, <code>DELETE</code> each with their own meaning. Please
 * only bind the according to the following:
 * <li>GET list: List the URIs and perhaps other details of the collection's
 * members.</li>
 * <li>GET list/id: Retrieve a representation of the addressed member of the
 * collection, expressed in an appropriate Internet media type.</li>
 * <li>POST list: Create a new entry in the collection. The new entry's URL is
 * assigned automatically and is usually returned by the operation.</li>
 * <li>POST list/id: Treat the addressed member as a collection in its own right
 * and create a new entry in it.</li>
 * <li>PUT list: Replace the entire collection with another collection.</li>
 * <li>PUT list/id: Replace the addressed member of the collection, or if it
 * doesn't exist, create it.</li>
 * <li>DELETE list: Delete the entire collection.</li>
 * <li>DELETE list/id: Delete the addressed member of the collection.</li>
 * </p>
 * </p>
 * <p>
 * This class will be serialized as XML (or maybe JSON who knows) to be sent
 * over to the client RPC. Hence this class needs to be annotated to indicate
 * which methods (and/or fields) are to be matched in the XML data. For this
 * use:
 * <li>@XmlRootElement at the root of the class</li>
 * <li>@XmlElement on each method/attribute that will yield <i>complex</i> data</li>
 * <li>@XmlAttribute on each method/attribute that will yield <i>simple</i> data
 * </li>
 * <li>Methods that return a list need to be annotated with @XmlElement and to
 * return a RestProjectList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestProject extends RestElement<Project> {
    private Project model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestProject() {
    }

    protected RestProject(Project model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestProject matching the <code>id</code>
     * </p>
     * 
     * @param id the id of the RestProject
     */
    @REST(name = "projects", method = RequestMethod.GET)
    public static RestProject getById(int id) {
        // TODO auto generated code
        // RestProject restProject = new
        // RestProject(ProjectManager.getProjectById(id));
        // if (restProject.isNull()) {
        // return null;
        // }
        // return restProject;
        return null;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestProject
     * </p>
     */
    @REST(name = "projects", method = RequestMethod.GET)
    public static RestProjectList getAll() {
        // TODO auto generated code
        return null;
    }

    // ---------------------------------------------------------------------------------------
    // -- XML Getters
    // ---------------------------------------------------------------------------------------

    // TODO Generate

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

    /**
     * @see com.bloatit.model.Project#getName()
     */
    // @XmlElement
    public String getName() throws RestException {
        // TODO auto-generated code stub
        try {
            String name = model.getName();
            return name;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getName on Project", e);
        }
    }

    /**
     * @see com.bloatit.model.Project#getDescription()
     */
    // @XmlElement
    public RestDescription getDescription() throws RestException {
        // TODO auto-generated code stub
        try {
            RestDescription description = new RestDescription(model.getDescription());
            return description;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getDescription on Project", e);
        }
    }

    /**
     * @see com.bloatit.model.Project#getDemands()
     */
    // @XmlElement
    public RestDemandList getDemands() throws RestException {
        // TODO auto-generated code stub
        try {
            return new RestDemandList(model.getDemands());
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getDemands on Project", e);
        }
    }

    /**
     * @see com.bloatit.model.Project#getImage()
     */
    // @XmlElement
    public RestFileMetadata getImage() throws RestException {
        // TODO auto-generated code stub
        try {
            RestFileMetadata image = new RestFileMetadata(model.getImage());
            return image;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getImage on Project", e);
        }
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(Project model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Project getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
