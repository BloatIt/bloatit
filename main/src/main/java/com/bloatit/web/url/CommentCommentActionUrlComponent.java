package com.bloatit.web.url;

import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ConversionErrorException;
import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.*;
import com.bloatit.framework.webserver.url.*;
import com.bloatit.framework.webserver.url.Loaders.*;

@SuppressWarnings("unused")
public final class CommentCommentActionUrlComponent extends UrlComponent {
public CommentCommentActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public CommentCommentActionUrlComponent(com.bloatit.model.Comment targetComment) {
    this();
        this.targetComment.setValue(targetComment);
}
private CommentCommentActionUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Comment, com.bloatit.model.Comment> targetComment =  new UrlParameter<com.bloatit.model.Comment, com.bloatit.model.Comment>(null, new UrlParameterDescription<com.bloatit.model.Comment>("target", com.bloatit.model.Comment.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Comment>());
private UrlParameter<java.lang.String, java.lang.String> comment =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_comment_content", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public com.bloatit.model.Comment getTargetComment(){ 
    return this.targetComment.getValue();
}

public UrlParameter<com.bloatit.model.Comment, com.bloatit.model.Comment> getTargetCommentParameter(){ 
    return this.targetComment;
}

public void setTargetComment(com.bloatit.model.Comment arg){ 
    this.targetComment.setValue(arg);
}

public java.lang.String getComment(){ 
    return this.comment.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getCommentParameter(){ 
    return this.comment;
}

public void setComment(java.lang.String arg){ 
    this.comment.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(targetComment);
    register(comment);
}

@Override 
public CommentCommentActionUrlComponent clone() { 
    CommentCommentActionUrlComponent other = new CommentCommentActionUrlComponent();
    other.targetComment = this.targetComment.clone();
    other.comment = this.comment.clone();
    return other;
}
}
