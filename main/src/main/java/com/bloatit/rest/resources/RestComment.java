package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Comment;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestCommentList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestComment extends RestElement<Comment>{ 
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
    public static RestCommentList getAll(){
        //TODO auto generated code
        return null;
    }

    Comment getModel(){
        return model;
    }
}
