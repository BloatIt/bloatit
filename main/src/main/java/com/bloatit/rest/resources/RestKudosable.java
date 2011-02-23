package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Kudosable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestKudosable extends RestElement{ 
    private Kudosable model;

    protected RestKudosable(Kudosable model){
        this.model=model;
    }

    @REST(name = "kudosables", method = RequestMethod.GET)
    public static RestKudosable getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "kudosables", method = RequestMethod.GET)
    public static RestList<RestKudosable> getAll(){
        //TODO auto generated code
        return null;
    }

    Kudosable getModel(){
        return model;
    }
}
