package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class DemandsUrl extends Url {
public DemandsUrl() {
    super("demands"); 
}
public DemandsUrl(Parameters params) {
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

public DemandsUrl clone() { 
    DemandsUrl other = new DemandsUrl();
    other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
    return other;
}
}
