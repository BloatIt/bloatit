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
public final class RegisterPageUrlComponent extends UrlComponent {
public RegisterPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public RegisterPageUrlComponent(){
    super();
    try {
        this.country.setValue(Loaders.fromStr(java.lang.String.class, ""));
        this.lang.setValue(Loaders.fromStr(java.lang.String.class, ""));
    } catch (ConversionErrorException e) {
        Log.web().fatal(e);
        assert false ;
    }
}
private UrlParameter<java.lang.String, java.lang.String> country =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_country", java.lang.String.class, Role.SESSION, "", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> lang =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_lang", java.lang.String.class, Role.SESSION, "", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

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
    register(country);
    register(lang);
}

@Override 
public RegisterPageUrlComponent clone() { 
    RegisterPageUrlComponent other = new RegisterPageUrlComponent();
    other.country = this.country.clone();
    other.lang = this.lang.clone();
    return other;
}
}
