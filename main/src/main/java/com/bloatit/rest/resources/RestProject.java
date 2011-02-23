package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Project;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestProjectList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestProject extends RestElement<Project>{ 
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
    public static RestProjectList getAll(){
        //TODO auto generated code
        return null;
    }

    Project getModel(){
        return model;
    }
}
