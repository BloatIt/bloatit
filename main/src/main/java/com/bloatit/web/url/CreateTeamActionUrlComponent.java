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
public final class CreateTeamActionUrlComponent extends UrlComponent {
public CreateTeamActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public CreateTeamActionUrlComponent(){
    super();
}
private UrlParameter<java.lang.String, java.lang.String> login =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_login", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(4, false, 30, false, false, 2147483647, 2147483647, "Number of characters team name has to be superior to 4", "Number of characters for team has to be inferior to 30", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.lang.String, java.lang.String> email =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_email", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(4, false, 30, false, false, 2147483647, 2147483647, "Number of characters for email has to be superior to 5", "Number of characters for email address has to be inferior to 30", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.lang.String, java.lang.String> right =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_team_rights", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public java.lang.String getLogin(){ 
    return this.login.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getLoginParameter(){ 
    return this.login;
}

public void setLogin(java.lang.String arg){ 
    this.login.setValue(arg);
}

public java.lang.String getEmail(){ 
    return this.email.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getEmailParameter(){ 
    return this.email;
}

public void setEmail(java.lang.String arg){ 
    this.email.setValue(arg);
}

public java.lang.String getRight(){ 
    return this.right.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getRightParameter(){ 
    return this.right;
}

public void setRight(java.lang.String arg){ 
    this.right.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(login);
    register(email);
    register(right);
}

@Override 
public CreateTeamActionUrlComponent clone() { 
    CreateTeamActionUrlComponent other = new CreateTeamActionUrlComponent();
    other.login = this.login.clone();
    other.email = this.email.clone();
    other.right = this.right.clone();
    return other;
}
}
