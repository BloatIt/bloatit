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
public final class LoginActionUrlComponent extends UrlComponent {
public LoginActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public LoginActionUrlComponent(){
    super();
}
private UrlParameter<java.lang.String, java.lang.String> login =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_login", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> password =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_password", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public java.lang.String getLogin(){ 
    return this.login.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getLoginParameter(){ 
    return this.login;
}

public void setLogin(java.lang.String arg){ 
    this.login.setValue(arg);
}

public java.lang.String getPassword(){ 
    return this.password.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getPasswordParameter(){ 
    return this.password;
}

public void setPassword(java.lang.String arg){ 
    this.password.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(login);
    register(password);
}

@Override 
public LoginActionUrlComponent clone() { 
    LoginActionUrlComponent other = new LoginActionUrlComponent();
    other.login = this.login.clone();
    other.password = this.password.clone();
    return other;
}
}
