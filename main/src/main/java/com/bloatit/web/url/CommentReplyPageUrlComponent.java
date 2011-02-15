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
public final class CommentReplyPageUrlComponent extends UrlComponent {
public CommentReplyPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public CommentReplyPageUrlComponent(com.bloatit.model.Comment targetComment) {
    this();
        this.targetComment.setValue(targetComment);
}
private CommentReplyPageUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Comment, com.bloatit.model.Comment> targetComment =  new UrlParameter<com.bloatit.model.Comment, com.bloatit.model.Comment>(null, new UrlParameterDescription<com.bloatit.model.Comment>("target", com.bloatit.model.Comment.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Comment>());

public com.bloatit.model.Comment getTargetComment(){ 
    return this.targetComment.getValue();
}

public UrlParameter<com.bloatit.model.Comment, com.bloatit.model.Comment> getTargetCommentParameter(){ 
    return this.targetComment;
}

public void setTargetComment(com.bloatit.model.Comment arg){ 
    this.targetComment.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(targetComment);
}

@Override 
public CommentReplyPageUrlComponent clone() { 
    CommentReplyPageUrlComponent other = new CommentReplyPageUrlComponent();
    other.targetComment = this.targetComment.clone();
    return other;
}
}
