package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class LoginActionUrl extends Url {
public static String getName() { return "LoginAction"; }
public com.bloatit.web.actions.LoginAction createPage() throws RedirectException{ 
    return new com.bloatit.web.actions.LoginAction(this); }
public LoginActionUrl(Parameters params, Parameters session) {
    super(getName());
    parseParameters(params, false);
    parseParameters(session, true);
}
public LoginActionUrl() {
    super(getName());
}
private Parameter<java.lang.String> login =     new Parameter<java.lang.String>("bloatit_login", null, java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private Parameter<java.lang.String> password =     new Parameter<java.lang.String>("bloatit_password", null, java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

public java.lang.String getLogin(){ 
    return this.login.getValue();
}

public void setLogin(java.lang.String arg){ 
    this.login.setValue(arg);
}

public java.lang.String getPassword(){ 
    return this.password.getValue();
}

public void setPassword(java.lang.String arg){ 
    this.password.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(login);
    register(password);
}

public LoginActionUrl clone() { 
    LoginActionUrl other = new LoginActionUrl();
    other.login = this.login.clone();
    other.password = this.password.clone();
    return other;
}
}
