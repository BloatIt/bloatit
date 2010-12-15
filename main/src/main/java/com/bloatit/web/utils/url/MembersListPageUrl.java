package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class MembersListPageUrl extends Url {
public MembersListPageUrl() {
    super("MembersListPage"); 
}
public MembersListPageUrl(Parameters params) {
    this();
    parseParameters(params);
}
private HtmlPagedListUrl pagedMemberListUrl = new HtmlPagedListUrl();

public HtmlPagedListUrl getPagedMemberListUrl(){ 
    return this.pagedMemberListUrl;
}

public void setPagedMemberListUrl(HtmlPagedListUrl arg0){ 
    this.pagedMemberListUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(pagedMemberListUrl);
}

public MembersListPageUrl clone() { 
    MembersListPageUrl other = new MembersListPageUrl();
    other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
    return other;
}
}
