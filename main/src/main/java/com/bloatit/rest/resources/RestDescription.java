package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Description;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestDescriptionList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestDescription extends RestElement<Description>{ 
    private Description model;

    protected RestDescription(Description model){
        this.model=model;
    }

    @REST(name = "descriptions", method = RequestMethod.GET)
    public static RestDescription getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "descriptions", method = RequestMethod.GET)
    public static RestDescriptionList getAll(){
        //TODO auto generated code
        return null;
    }

    Description getModel(){
        return model;
    }
}
