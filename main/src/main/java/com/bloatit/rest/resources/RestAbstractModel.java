package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.AbstractModel;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestAbstractModel extends RestElement{ 
    private AbstractModel model;

    protected RestAbstractModel(AbstractModel model){
        this.model=model;
    }

    @REST(name = "abstractmodels", method = RequestMethod.GET)
    public static RestAbstractModel getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "abstractmodels", method = RequestMethod.GET)
    public static RestList<RestAbstractModel> getAll(){
        //TODO auto generated code
        return null;
    }

    AbstractModel getModel(){
        return model;
    }
}
