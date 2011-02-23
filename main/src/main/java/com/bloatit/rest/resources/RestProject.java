package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Project;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestProject extends RestElement{ 
    private Project model;

    protected RestProject(Project model){
        this.model=model;
    }

    @REST(name = "projects", method = RequestMethod.GET)
    public static RestProject getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "projects", method = RequestMethod.GET)
    public static RestList<RestProject> getAll(){
        //TODO auto generated code
        return null;
    }

    Project getModel(){
        return model;
    }
}
