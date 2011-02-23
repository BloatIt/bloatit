package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.UserContentInterface;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestUserContentInterface extends RestElement{ 
    private UserContentInterface model;

    protected RestUserContentInterface(UserContentInterface model){
        this.model=model;
    }

    @REST(name = "usercontentinterfaces", method = RequestMethod.GET)
    public static RestUserContentInterface getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "usercontentinterfaces", method = RequestMethod.GET)
    public static RestList<RestUserContentInterface> getAll(){
        //TODO auto generated code
        return null;
    }

    UserContentInterface getModel(){
        return model;
    }
}
