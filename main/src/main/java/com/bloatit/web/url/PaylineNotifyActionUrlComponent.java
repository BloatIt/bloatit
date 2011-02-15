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
public final class PaylineNotifyActionUrlComponent extends UrlComponent {
public PaylineNotifyActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public PaylineNotifyActionUrlComponent(){
    super();
}
private UrlParameter<java.lang.String, java.lang.String> token =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("token", java.lang.String.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.INFO), new UrlParameterConstraints<java.lang.String>());

public java.lang.String getToken(){ 
    return this.token.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getTokenParameter(){ 
    return this.token;
}

public void setToken(java.lang.String arg){ 
    this.token.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(token);
}

@Override 
public PaylineNotifyActionUrlComponent clone() { 
    PaylineNotifyActionUrlComponent other = new PaylineNotifyActionUrlComponent();
    other.token = this.token.clone();
    return other;
}
}
