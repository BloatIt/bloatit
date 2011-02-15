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
public final class DemandPageUrlComponent extends UrlComponent {
public DemandPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public DemandPageUrlComponent(com.bloatit.model.Demand demand) {
    this();
        this.demand.setValue(demand);
}
private DemandPageUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> demand =  new UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand>(null, new UrlParameterDescription<com.bloatit.model.Demand>("id", com.bloatit.model.Demand.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Demand>());
private DemandTabPaneUrl demandTabPaneUrl = new DemandTabPaneUrl();

public com.bloatit.model.Demand getDemand(){ 
    return this.demand.getValue();
}

public UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> getDemandParameter(){ 
    return this.demand;
}

public void setDemand(com.bloatit.model.Demand arg){ 
    this.demand.setValue(arg);
}

public java.lang.String getTitle(){ 
    if (demand.getValue() != null) {
    try{
        return demand.getValue().getTitle();
    } catch (Exception e) {
    // do nothing.
    }
    }
    return null;
}

public DemandTabPaneUrl getDemandTabPaneUrl(){ 
    return this.demandTabPaneUrl;
}

public void setDemandTabPaneUrl(DemandTabPaneUrl arg){ 
    this.demandTabPaneUrl = arg;
}


@Override 
protected void doRegister() { 
    register(demand);
    register(demandTabPaneUrl);
}

@Override 
public DemandPageUrlComponent clone() { 
    DemandPageUrlComponent other = new DemandPageUrlComponent();
    other.demand = this.demand.clone();
    other.demandTabPaneUrl = this.demandTabPaneUrl.clone();
    return other;
}
}
