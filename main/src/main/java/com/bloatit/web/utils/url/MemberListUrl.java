package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class MemberListUrl extends Url {
public MemberListUrl() {
    super("memberList"); 
}
public MemberListUrl(Parameters params) {
    this();
    parseParameters(params);
}
private PagedListUrl pagedMemberListUrl = new PagedListUrl();

public PagedListUrl getPagedMemberListUrl(){ 
    return this.pagedMemberListUrl;
}

public void setPagedMemberListUrl(PagedListUrl arg0){ 
    this.pagedMemberListUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(pagedMemberListUrl);
}

public MemberListUrl clone() { 
    MemberListUrl other = new MemberListUrl();
    other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
    return other;
}
}
