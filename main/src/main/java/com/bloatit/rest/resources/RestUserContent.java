package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.UserContent;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestUserContent extends RestElement{ 
    private UserContent model;

    protected RestUserContent(UserContent model){
        this.model=model;
    }

    @REST(name = "usercontents", method = RequestMethod.GET)
    public static RestUserContent getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "usercontents", method = RequestMethod.GET)
    public static RestList<RestUserContent> getAll(){
        //TODO auto generated code
        return null;
    }

    UserContent getModel(){
        return model;
    }
}
