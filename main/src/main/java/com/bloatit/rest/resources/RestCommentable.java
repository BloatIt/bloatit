package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Commentable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestCommentable extends RestElement{ 
    private Commentable model;

    protected RestCommentable(Commentable model){
        this.model=model;
    }

    @REST(name = "commentables", method = RequestMethod.GET)
    public static RestCommentable getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "commentables", method = RequestMethod.GET)
    public static RestList<RestCommentable> getAll(){
        //TODO auto generated code
        return null;
    }

    Commentable getModel(){
        return model;
    }
}
