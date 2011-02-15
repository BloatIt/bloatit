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
public final class AdministrationActionUrlComponent extends UrlComponent {
public AdministrationActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public AdministrationActionUrlComponent(){
    super();
}
private UrlParameter<java.util.List, com.bloatit.model.Demand> demands =  new UrlParameter<java.util.List, com.bloatit.model.Demand>(new java.util.ArrayList(), new UrlParameterDescription<com.bloatit.model.Demand>("ids", com.bloatit.model.Demand.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Demand>());

public java.util.List getDemands(){ 
    return this.demands.getValue();
}

public UrlParameter<java.util.List, com.bloatit.model.Demand> getDemandsParameter(){ 
    return this.demands;
}

public void setDemands(java.util.List arg){ 
    this.demands.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(demands);
}

@Override 
public AdministrationActionUrlComponent clone() { 
    AdministrationActionUrlComponent other = new AdministrationActionUrlComponent();
    other.demands = this.demands.clone();
    return other;
}
}
