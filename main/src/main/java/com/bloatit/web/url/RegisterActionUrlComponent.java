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
public final class RegisterActionUrlComponent extends UrlComponent {
public RegisterActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public RegisterActionUrlComponent(){
    super();
}
private UrlParameter<java.lang.String, java.lang.String> login =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_login", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(4, false, 15, false, false, 2147483647, 2147483647, "Number of characters for login has to be superior to 4", "Number of characters for login has to be inferior to 15", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.lang.String, java.lang.String> password =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_password", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(4, false, 15, false, false, 2147483647, 2147483647, "Number of characters for password has to be superior to 4", "Number of characters for password has to be inferior to 15", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.lang.String, java.lang.String> email =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_email", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(4, false, 30, false, false, 2147483647, 2147483647, "Number of characters for email has to be superior to 5", "Number of characters for email address has to be inferior to 30", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.lang.String, java.lang.String> country =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_country", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> lang =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_lang", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

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

public java.lang.String getEmail(){ 
    return this.email.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getEmailParameter(){ 
    return this.email;
}

public void setEmail(java.lang.String arg){ 
    this.email.setValue(arg);
}

public java.lang.String getCountry(){ 
    return this.country.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getCountryParameter(){ 
    return this.country;
}

public void setCountry(java.lang.String arg){ 
    this.country.setValue(arg);
}

public java.lang.String getLang(){ 
    return this.lang.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getLangParameter(){ 
    return this.lang;
}

public void setLang(java.lang.String arg){ 
    this.lang.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(login);
    register(password);
    register(email);
    register(country);
    register(lang);
}

@Override 
public RegisterActionUrlComponent clone() { 
    RegisterActionUrlComponent other = new RegisterActionUrlComponent();
    other.login = this.login.clone();
    other.password = this.password.clone();
    other.email = this.email.clone();
    other.country = this.country.clone();
    other.lang = this.lang.clone();
    return other;
}
}
