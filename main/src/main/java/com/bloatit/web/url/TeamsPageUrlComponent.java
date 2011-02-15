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
public final class TeamsPageUrlComponent extends UrlComponent {
public TeamsPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public TeamsPageUrlComponent(){
    super();
}
private HtmlPagedListUrl pagedTeamListUrl = new HtmlPagedListUrl();

public HtmlPagedListUrl getPagedTeamListUrl(){ 
    return this.pagedTeamListUrl;
}

public void setPagedTeamListUrl(HtmlPagedListUrl arg){ 
    this.pagedTeamListUrl = arg;
}


@Override 
protected void doRegister() { 
    register(pagedTeamListUrl);
}

@Override 
public TeamsPageUrlComponent clone() { 
    TeamsPageUrlComponent other = new TeamsPageUrlComponent();
    other.pagedTeamListUrl = this.pagedTeamListUrl.clone();
    return other;
}
}
