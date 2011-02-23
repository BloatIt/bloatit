package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Commentable;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

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
