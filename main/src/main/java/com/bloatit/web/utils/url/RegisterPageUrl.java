package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class RegisterPageUrl extends Url {
public static String getName() { return "member/create"; }
public com.bloatit.web.html.pages.RegisterPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.RegisterPage(this); }
public RegisterPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public RegisterPageUrl(){
    super(getName());
    try {
        this.login.setValue(Loaders.fromStr(java.lang.String.class, ""));
        this.password.setValue(Loaders.fromStr(java.lang.String.class, ""));
        this.email.setValue(Loaders.fromStr(java.lang.String.class, ""));
        this.country.setValue(Loaders.fromStr(java.lang.String.class, ""));
        this.lang.setValue(Loaders.fromStr(java.lang.String.class, ""));
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private UrlParameter<java.lang.String> login =     new UrlParameter<java.lang.String>("bloatit_login", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> password =     new UrlParameter<java.lang.String>("bloatit_password", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> email =     new UrlParameter<java.lang.String>("bloatit_email", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> country =     new UrlParameter<java.lang.String>("bloatit_country", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> lang =     new UrlParameter<java.lang.String>("bloatit_lang", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

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

public java.lang.String getEmail(){ 
    return this.email.getValue();
}

public void setEmail(java.lang.String arg){ 
    this.email.setValue(arg);
}

public java.lang.String getCountry(){ 
    return this.country.getValue();
}

public void setCountry(java.lang.String arg){ 
    this.country.setValue(arg);
}

public java.lang.String getLang(){ 
    return this.lang.getValue();
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
public RegisterPageUrl clone() { 
    RegisterPageUrl other = new RegisterPageUrl();
    other.login = this.login.clone();
    other.password = this.password.clone();
    other.email = this.email.clone();
    other.country = this.country.clone();
    other.lang = this.lang.clone();
    return other;
}
}
