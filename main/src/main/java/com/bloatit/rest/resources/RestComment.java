package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Comment;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestComment extends RestElement{ 
    private Comment model;

    protected RestComment(Comment model){
        this.model=model;
    }

    @REST(name = "comments", method = RequestMethod.GET)
    public static RestComment getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "comments", method = RequestMethod.GET)
    public static RestList<RestComment> getAll(){
        //TODO auto generated code
        return null;
    }

    Comment getModel(){
        return model;
    }
}
