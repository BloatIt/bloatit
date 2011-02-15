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
public final class ContributePageUrlComponent extends UrlComponent {
public ContributePageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public ContributePageUrlComponent(com.bloatit.model.Demand targetIdea) {
    this();
        this.targetIdea.setValue(targetIdea);
}
private ContributePageUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> targetIdea =  new UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand>(null, new UrlParameterDescription<com.bloatit.model.Demand>("targetIdea", com.bloatit.model.Demand.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Demand>());

public com.bloatit.model.Demand getTargetIdea(){ 
    return this.targetIdea.getValue();
}

public UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> getTargetIdeaParameter(){ 
    return this.targetIdea;
}

public void setTargetIdea(com.bloatit.model.Demand arg){ 
    this.targetIdea.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(targetIdea);
}

@Override 
public ContributePageUrlComponent clone() { 
    ContributePageUrlComponent other = new ContributePageUrlComponent();
    other.targetIdea = this.targetIdea.clone();
    return other;
}
}
