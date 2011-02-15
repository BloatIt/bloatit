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
public final class PaylinePageUrlComponent extends UrlComponent {
public PaylinePageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public PaylinePageUrlComponent(java.lang.String ack) {
    this();
        this.ack.setValue(ack);
}
private PaylinePageUrlComponent(){
    super();
}
private UrlParameter<java.lang.String, java.lang.String> token =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("token", java.lang.String.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.INFO), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> ack =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("ack", java.lang.String.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public java.lang.String getToken(){ 
    return this.token.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getTokenParameter(){ 
    return this.token;
}

public void setToken(java.lang.String arg){ 
    this.token.setValue(arg);
}

public java.lang.String getAck(){ 
    return this.ack.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getAckParameter(){ 
    return this.ack;
}

public void setAck(java.lang.String arg){ 
    this.ack.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(token);
    register(ack);
}

@Override 
public PaylinePageUrlComponent clone() { 
    PaylinePageUrlComponent other = new PaylinePageUrlComponent();
    other.token = this.token.clone();
    other.ack = this.ack.clone();
    return other;
}
}
