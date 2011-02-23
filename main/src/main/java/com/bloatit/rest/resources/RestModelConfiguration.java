package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.ModelConfiguration;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestModelConfiguration extends RestElement{ 
    private ModelConfiguration model;

    protected RestModelConfiguration(ModelConfiguration model){
        this.model=model;
    }

    @REST(name = "modelconfigurations", method = RequestMethod.GET)
    public static RestModelConfiguration getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "modelconfigurations", method = RequestMethod.GET)
    public static RestList<RestModelConfiguration> getAll(){
        //TODO auto generated code
        return null;
    }

    ModelConfiguration getModel(){
        return model;
    }
}
