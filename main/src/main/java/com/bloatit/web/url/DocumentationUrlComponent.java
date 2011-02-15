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
public final class DocumentationUrlComponent extends UrlComponent {
public DocumentationUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public DocumentationUrlComponent(){
    super();
    try {
        this.docTarget.setValue(Loaders.fromStr(java.lang.String.class, "home"));
    } catch (ConversionErrorException e) {
        Log.web().fatal(e);
        assert false ;
    }
}
private UrlParameter<java.lang.String, java.lang.String> docTarget =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("doc", java.lang.String.class, Role.GET, "home", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public java.lang.String getDocTarget(){ 
    return this.docTarget.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getDocTargetParameter(){ 
    return this.docTarget;
}

public void setDocTarget(java.lang.String arg){ 
    this.docTarget.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(docTarget);
}

@Override 
public DocumentationUrlComponent clone() { 
    DocumentationUrlComponent other = new DocumentationUrlComponent();
    other.docTarget = this.docTarget.clone();
    return other;
}
}
