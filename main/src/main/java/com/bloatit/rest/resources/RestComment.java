package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Comment;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestCommentList;

@XmlRootElement
public class RestComment extends RestElement<Comment> {
    private final Comment model;

    protected RestComment(final Comment model) {
        this.model = model;
    }

    @REST(name = "comments", method = RequestMethod.GET)
    public static RestComment getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "comments", method = RequestMethod.GET)
    public static RestCommentList getAll() {
        // TODO auto generated code
        return null;
    }

    Comment getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
