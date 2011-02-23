package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Group;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestGroup extends RestElement{ 
    private Group model;

    protected RestGroup(Group model){
        this.model=model;
    }

    @REST(name = "groups", method = RequestMethod.GET)
    public static RestGroup getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "groups", method = RequestMethod.GET)
    public static RestList<RestGroup> getAll(){
        //TODO auto generated code
        return null;
    }

    Group getModel(){
        return model;
    }
}
