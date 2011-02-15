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
public final class CreateDemandActionUrlComponent extends UrlComponent {
public CreateDemandActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public CreateDemandActionUrlComponent(){
    super();
}
private UrlParameter<java.lang.String, java.lang.String> description =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("description", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(10, false, 80, false, false, 2147483647, 2147483647, "The title must have at least 10 chars.", "The title must be 80 chars length max.", "Error you forgot to write a title", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.lang.String, java.lang.String> specification =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("specification", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<com.bloatit.model.Project, com.bloatit.model.Project> project =  new UrlParameter<com.bloatit.model.Project, com.bloatit.model.Project>(null, new UrlParameterDescription<com.bloatit.model.Project>("project", com.bloatit.model.Project.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Project>());
private UrlParameter<java.lang.String, java.lang.String> lang =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("bloatit_idea_lang", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());

public java.lang.String getDescription(){ 
    return this.description.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getDescriptionParameter(){ 
    return this.description;
}

public void setDescription(java.lang.String arg){ 
    this.description.setValue(arg);
}

public java.lang.String getSpecification(){ 
    return this.specification.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getSpecificationParameter(){ 
    return this.specification;
}

public void setSpecification(java.lang.String arg){ 
    this.specification.setValue(arg);
}

public com.bloatit.model.Project getProject(){ 
    return this.project.getValue();
}

public UrlParameter<com.bloatit.model.Project, com.bloatit.model.Project> getProjectParameter(){ 
    return this.project;
}

public void setProject(com.bloatit.model.Project arg){ 
    this.project.setValue(arg);
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
    register(description);
    register(specification);
    register(project);
    register(lang);
}

@Override 
public CreateDemandActionUrlComponent clone() { 
    CreateDemandActionUrlComponent other = new CreateDemandActionUrlComponent();
    other.description = this.description.clone();
    other.specification = this.specification.clone();
    other.project = this.project.clone();
    other.lang = this.lang.clone();
    return other;
}
}
