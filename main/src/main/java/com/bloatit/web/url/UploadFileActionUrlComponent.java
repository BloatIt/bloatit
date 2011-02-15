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
public final class UploadFileActionUrlComponent extends UrlComponent {
public UploadFileActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public UploadFileActionUrlComponent(){
    super();
}
private UrlParameter<java.lang.String, java.lang.String> wow =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("wow", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> fichier =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("fichier", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public java.lang.String getWow(){ 
    return this.wow.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getWowParameter(){ 
    return this.wow;
}

public void setWow(java.lang.String arg){ 
    this.wow.setValue(arg);
}

public java.lang.String getFichier(){ 
    return this.fichier.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getFichierParameter(){ 
    return this.fichier;
}

public void setFichier(java.lang.String arg){ 
    this.fichier.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(wow);
    register(fichier);
}

@Override 
public UploadFileActionUrlComponent clone() { 
    UploadFileActionUrlComponent other = new UploadFileActionUrlComponent();
    other.wow = this.wow.clone();
    other.fichier = this.fichier.clone();
    return other;
}
}
