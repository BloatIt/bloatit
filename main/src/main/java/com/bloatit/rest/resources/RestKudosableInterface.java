package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.KudosableInterface;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestKudosableInterface extends RestElement{ 
    private KudosableInterface model;

    protected RestKudosableInterface(KudosableInterface model){
        this.model=model;
    }

    @REST(name = "kudosableinterfaces", method = RequestMethod.GET)
    public static RestKudosableInterface getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "kudosableinterfaces", method = RequestMethod.GET)
    public static RestList<RestKudosableInterface> getAll(){
        //TODO auto generated code
        return null;
    }

    KudosableInterface getModel(){
        return model;
    }
}
