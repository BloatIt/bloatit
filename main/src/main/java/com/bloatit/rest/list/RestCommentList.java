package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Comment;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestComment;

@XmlRootElement
public class RestCommentList extends RestListBinder<RestComment, Comment> {
    public RestCommentList(PageIterable<Comment> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "comments")
    @XmlElement(name = "comment")
    public RestCommentList getComments() {
        return this;
    }
}

