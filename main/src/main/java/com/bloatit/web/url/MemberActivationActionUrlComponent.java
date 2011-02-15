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
public final class MemberActivationActionUrlComponent extends UrlComponent {
public MemberActivationActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public MemberActivationActionUrlComponent(java.lang.String login, java.lang.String key) {
    this();
        this.login.setValue(login);
        this.key.setValue(key);
}
private MemberActivationActionUrlComponent(){
    super();
}
private UrlParameter<java.lang.String, java.lang.String> login =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("member", java.lang.String.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> key =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("key", java.lang.String.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public java.lang.String getLogin(){ 
    return this.login.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getLoginParameter(){ 
    return this.login;
}

public void setLogin(java.lang.String arg){ 
    this.login.setValue(arg);
}

public java.lang.String getKey(){ 
    return this.key.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getKeyParameter(){ 
    return this.key;
}

public void setKey(java.lang.String arg){ 
    this.key.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(login);
    register(key);
}

@Override 
public MemberActivationActionUrlComponent clone() { 
    MemberActivationActionUrlComponent other = new MemberActivationActionUrlComponent();
    other.login = this.login.clone();
    other.key = this.key.clone();
    return other;
}
}
