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
public final class FileResourceUrlComponent extends UrlComponent {
public FileResourceUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public FileResourceUrlComponent(com.bloatit.model.FileMetadata file) {
    this();
        this.file.setValue(file);
}
private FileResourceUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.FileMetadata, com.bloatit.model.FileMetadata> file =  new UrlParameter<com.bloatit.model.FileMetadata, com.bloatit.model.FileMetadata>(null, new UrlParameterDescription<com.bloatit.model.FileMetadata>("id", com.bloatit.model.FileMetadata.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.FileMetadata>(-2147483648, false, 2147483647, false, false, 2147483647, 2147483647, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, "The id of the resource is incorrect or missing", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));

public com.bloatit.model.FileMetadata getFile(){ 
    return this.file.getValue();
}

public UrlParameter<com.bloatit.model.FileMetadata, com.bloatit.model.FileMetadata> getFileParameter(){ 
    return this.file;
}

public void setFile(com.bloatit.model.FileMetadata arg){ 
    this.file.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(file);
}

@Override 
public FileResourceUrlComponent clone() { 
    FileResourceUrlComponent other = new FileResourceUrlComponent();
    other.file = this.file.clone();
    return other;
}
}
