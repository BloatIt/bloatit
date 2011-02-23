package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Bug;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestBugList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestBug extends RestElement<Bug>{ 
    private Bug model;

    protected RestBug(Bug model){
        this.model=model;
    }

    @REST(name = "bugs", method = RequestMethod.GET)
    public static RestBug getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "bugs", method = RequestMethod.GET)
    public static RestBugList getAll(){
        //TODO auto generated code
        return null;
    }

    Bug getModel(){
        return model;
    }
}
