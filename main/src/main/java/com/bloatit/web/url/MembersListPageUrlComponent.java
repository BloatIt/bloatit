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
public final class MembersListPageUrlComponent extends UrlComponent {
public MembersListPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public MembersListPageUrlComponent(){
    super();
}
private HtmlPagedListUrl pagedMemberListUrl = new HtmlPagedListUrl();

public HtmlPagedListUrl getPagedMemberListUrl(){ 
    return this.pagedMemberListUrl;
}

public void setPagedMemberListUrl(HtmlPagedListUrl arg){ 
    this.pagedMemberListUrl = arg;
}


@Override 
protected void doRegister() { 
    register(pagedMemberListUrl);
}

@Override 
public MembersListPageUrlComponent clone() { 
    MembersListPageUrlComponent other = new MembersListPageUrlComponent();
    other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
    return other;
}
}
