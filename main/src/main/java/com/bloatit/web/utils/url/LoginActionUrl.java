package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class LoginActionUrl extends Url {
public static String getName() { return "LoginAction"; }
public LoginActionUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public LoginActionUrl() {
    super(getName());
}
private java.lang.String login;
private java.lang.String password;

public java.lang.String getLogin(){ 
    return this.login;
}

public void setLogin(java.lang.String arg0){ 
    this.login = arg0;
}

public java.lang.String getPassword(){ 
    return this.password;
}

public void setPassword(java.lang.String arg0){ 
    this.password = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("bloatit_login", getLogin(), java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("bloatit_password", getPassword(), java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}

public LoginActionUrl clone() { 
    LoginActionUrl other = new LoginActionUrl();
    other.login = this.login;
    other.password = this.password;
    return other;
}
}
