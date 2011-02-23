package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.ModelConfiguration;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

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
