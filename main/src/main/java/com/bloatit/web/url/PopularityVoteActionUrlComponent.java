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
public final class PopularityVoteActionUrlComponent extends UrlComponent {
public PopularityVoteActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public PopularityVoteActionUrlComponent(com.bloatit.model.KudosableInterface targetKudosable, java.lang.Boolean voteUp) {
    this();
        this.targetKudosable.setValue(targetKudosable);
        this.voteUp.setValue(voteUp);
}
private PopularityVoteActionUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.KudosableInterface, com.bloatit.model.KudosableInterface> targetKudosable =  new UrlParameter<com.bloatit.model.KudosableInterface, com.bloatit.model.KudosableInterface>(null, new UrlParameterDescription<com.bloatit.model.KudosableInterface>("targetKudosable", com.bloatit.model.KudosableInterface.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.KudosableInterface>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> voteUp =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("voteUp", java.lang.Boolean.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());

public com.bloatit.model.KudosableInterface getTargetKudosable(){ 
    return this.targetKudosable.getValue();
}

public UrlParameter<com.bloatit.model.KudosableInterface, com.bloatit.model.KudosableInterface> getTargetKudosableParameter(){ 
    return this.targetKudosable;
}

public void setTargetKudosable(com.bloatit.model.KudosableInterface arg){ 
    this.targetKudosable.setValue(arg);
}

public java.lang.Boolean getVoteUp(){ 
    return this.voteUp.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getVoteUpParameter(){ 
    return this.voteUp;
}

public void setVoteUp(java.lang.Boolean arg){ 
    this.voteUp.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(targetKudosable);
    register(voteUp);
}

@Override 
public PopularityVoteActionUrlComponent clone() { 
    PopularityVoteActionUrlComponent other = new PopularityVoteActionUrlComponent();
    other.targetKudosable = this.targetKudosable.clone();
    other.voteUp = this.voteUp.clone();
    return other;
}
}
