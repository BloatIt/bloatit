package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Release;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestRelease extends RestElement{ 
    private Release model;

    protected RestRelease(Release model){
        this.model=model;
    }

    @REST(name = "releases", method = RequestMethod.GET)
    public static RestRelease getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "releases", method = RequestMethod.GET)
    public static RestList<RestRelease> getAll(){
        //TODO auto generated code
        return null;
    }

    Release getModel(){
        return model;
    }
}
