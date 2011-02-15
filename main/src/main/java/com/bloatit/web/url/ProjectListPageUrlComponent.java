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
public final class ProjectListPageUrlComponent extends UrlComponent {
public ProjectListPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public ProjectListPageUrlComponent(){
    super();
}
private HtmlPagedListUrl pagedProjectListUrl = new HtmlPagedListUrl();

public HtmlPagedListUrl getPagedProjectListUrl(){ 
    return this.pagedProjectListUrl;
}

public void setPagedProjectListUrl(HtmlPagedListUrl arg){ 
    this.pagedProjectListUrl = arg;
}


@Override 
protected void doRegister() { 
    register(pagedProjectListUrl);
}

@Override 
public ProjectListPageUrlComponent clone() { 
    ProjectListPageUrlComponent other = new ProjectListPageUrlComponent();
    other.pagedProjectListUrl = this.pagedProjectListUrl.clone();
    return other;
}
}
