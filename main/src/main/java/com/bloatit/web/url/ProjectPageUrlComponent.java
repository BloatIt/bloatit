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
public final class ProjectPageUrlComponent extends UrlComponent {
public ProjectPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public ProjectPageUrlComponent(com.bloatit.model.Project project) {
    this();
        this.project.setValue(project);
}
private ProjectPageUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Project, com.bloatit.model.Project> project =  new UrlParameter<com.bloatit.model.Project, com.bloatit.model.Project>(null, new UrlParameterDescription<com.bloatit.model.Project>("id", com.bloatit.model.Project.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Project>(-2147483648, false, 2147483647, false, false, 2147483647, 2147483647, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, "The id of the project is incorrect or missing", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));

public com.bloatit.model.Project getProject(){ 
    return this.project.getValue();
}

public UrlParameter<com.bloatit.model.Project, com.bloatit.model.Project> getProjectParameter(){ 
    return this.project;
}

public void setProject(com.bloatit.model.Project arg){ 
    this.project.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(project);
}

@Override 
public ProjectPageUrlComponent clone() { 
    ProjectPageUrlComponent other = new ProjectPageUrlComponent();
    other.project = this.project.clone();
    return other;
}
}
