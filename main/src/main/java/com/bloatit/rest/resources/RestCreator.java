package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Creator;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestCreator extends RestElement{ 
    private Creator model;

    protected RestCreator(Creator model){
        this.model=model;
    }

    @REST(name = "creators", method = RequestMethod.GET)
    public static RestCreator getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "creators", method = RequestMethod.GET)
    public static RestList<RestCreator> getAll(){
        //TODO auto generated code
        return null;
    }

    Creator getModel(){
        return model;
    }
}
