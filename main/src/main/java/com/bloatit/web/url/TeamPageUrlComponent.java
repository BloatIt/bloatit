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
public final class TeamPageUrlComponent extends UrlComponent {
public TeamPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public TeamPageUrlComponent(com.bloatit.model.Group targetTeam) {
    this();
        this.targetTeam.setValue(targetTeam);
}
private TeamPageUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Group, com.bloatit.model.Group> targetTeam =  new UrlParameter<com.bloatit.model.Group, com.bloatit.model.Group>(null, new UrlParameterDescription<com.bloatit.model.Group>("targetTeam", com.bloatit.model.Group.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Group>());

public com.bloatit.model.Group getTargetTeam(){ 
    return this.targetTeam.getValue();
}

public UrlParameter<com.bloatit.model.Group, com.bloatit.model.Group> getTargetTeamParameter(){ 
    return this.targetTeam;
}

public void setTargetTeam(com.bloatit.model.Group arg){ 
    this.targetTeam.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(targetTeam);
}

@Override 
public TeamPageUrlComponent clone() { 
    TeamPageUrlComponent other = new TeamPageUrlComponent();
    other.targetTeam = this.targetTeam.clone();
    return other;
}
}
