package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Comment;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

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
