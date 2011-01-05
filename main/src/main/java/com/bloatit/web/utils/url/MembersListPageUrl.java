package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class MembersListPageUrl extends Url {
public static String getName() { return "memberList"; }
public com.bloatit.web.html.pages.MembersListPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.MembersListPage(this); }
public MembersListPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public MembersListPageUrl(){
    super(getName());
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
public MembersListPageUrl clone() { 
    MembersListPageUrl other = new MembersListPageUrl();
    other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
    return other;
}
}
