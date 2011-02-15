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
public final class IdeaCommentActionUrlComponent extends UrlComponent {
public IdeaCommentActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public IdeaCommentActionUrlComponent(com.bloatit.model.Demand targetIdea) {
    this();
        this.targetIdea.setValue(targetIdea);
}
private IdeaCommentActionUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> targetIdea =  new UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand>(null, new UrlParameterDescription<com.bloatit.model.Demand>("target", com.bloatit.model.Demand.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Demand>());
private UrlParameter<java.lang.String, java.lang.String> comment =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_comment_content", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public com.bloatit.model.Demand getTargetIdea(){ 
    return this.targetIdea.getValue();
}

public UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> getTargetIdeaParameter(){ 
    return this.targetIdea;
}

public void setTargetIdea(com.bloatit.model.Demand arg){ 
    this.targetIdea.setValue(arg);
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
    register(targetIdea);
    register(comment);
}

@Override 
public IdeaCommentActionUrlComponent clone() { 
    IdeaCommentActionUrlComponent other = new IdeaCommentActionUrlComponent();
    other.targetIdea = this.targetIdea.clone();
    other.comment = this.comment.clone();
    return other;
}
}
