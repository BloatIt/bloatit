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
public final class AddProjectActionUrlComponent extends UrlComponent {
public AddProjectActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public AddProjectActionUrlComponent(){
    super();
}
private UrlParameter<java.lang.String, java.lang.String> shortDescription =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_project_short_description", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(10, false, 120, false, false, 2147483647, 2147483647, "The short description must have at least 10 chars.", "The short description must be 120 chars length max.", "You forgot to write a short description", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.lang.String, java.lang.String> description =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_project_description", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(-2147483648, false, 2147483647, false, true, 2147483647, 2147483647, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.lang.String, java.lang.String> projectName =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_idea_project", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(3, false, 100, false, false, 2147483647, 2147483647, "The project name must have at least 3 chars.", "The project name must be 1OO chars length max.", "The project name is requiered.", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.lang.String, java.lang.String> image =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("image", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> imageFileName =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("image/filename", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> imageContentType =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("image/contenttype", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> lang =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_idea_lang", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public java.lang.String getShortDescription(){ 
    return this.shortDescription.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getShortDescriptionParameter(){ 
    return this.shortDescription;
}

public void setShortDescription(java.lang.String arg){ 
    this.shortDescription.setValue(arg);
}

public java.lang.String getDescription(){ 
    return this.description.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getDescriptionParameter(){ 
    return this.description;
}

public void setDescription(java.lang.String arg){ 
    this.description.setValue(arg);
}

public java.lang.String getProjectName(){ 
    return this.projectName.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getProjectNameParameter(){ 
    return this.projectName;
}

public void setProjectName(java.lang.String arg){ 
    this.projectName.setValue(arg);
}

public java.lang.String getImage(){ 
    return this.image.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getImageParameter(){ 
    return this.image;
}

public void setImage(java.lang.String arg){ 
    this.image.setValue(arg);
}

public java.lang.String getImageFileName(){ 
    return this.imageFileName.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getImageFileNameParameter(){ 
    return this.imageFileName;
}

public void setImageFileName(java.lang.String arg){ 
    this.imageFileName.setValue(arg);
}

public java.lang.String getImageContentType(){ 
    return this.imageContentType.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getImageContentTypeParameter(){ 
    return this.imageContentType;
}

public void setImageContentType(java.lang.String arg){ 
    this.imageContentType.setValue(arg);
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
    register(shortDescription);
    register(description);
    register(projectName);
    register(image);
    register(imageFileName);
    register(imageContentType);
    register(lang);
}

@Override 
public AddProjectActionUrlComponent clone() { 
    AddProjectActionUrlComponent other = new AddProjectActionUrlComponent();
    other.shortDescription = this.shortDescription.clone();
    other.description = this.description.clone();
    other.projectName = this.projectName.clone();
    other.image = this.image.clone();
    other.imageFileName = this.imageFileName.clone();
    other.imageContentType = this.imageContentType.clone();
    other.lang = this.lang.clone();
    return other;
}
}
