package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Model;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestModel extends RestElement{ 
    private Model model;

    protected RestModel(Model model){
        this.model=model;
    }

    @REST(name = "models", method = RequestMethod.GET)
    public static RestModel getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "models", method = RequestMethod.GET)
    public static RestList<RestModel> getAll(){
        //TODO auto generated code
        return null;
    }

    Model getModel(){
        return model;
    }
}
