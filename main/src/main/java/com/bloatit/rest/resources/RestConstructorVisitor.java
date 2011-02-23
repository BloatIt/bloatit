package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.DataVisitorConstructor;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestConstructorVisitor extends RestElement{ 
    private final DataVisitorConstructor model;

    protected RestConstructorVisitor(DataVisitorConstructor model){
        this.model=model;
    }

    @REST(name = "constructorvisitors", method = RequestMethod.GET)
    public static RestConstructorVisitor getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "constructorvisitors", method = RequestMethod.GET)
    public static RestList<RestConstructorVisitor> getAll(){
        //TODO auto generated code
        return null;
    }

    DataVisitorConstructor getModel(){
        return model;
    }
}
