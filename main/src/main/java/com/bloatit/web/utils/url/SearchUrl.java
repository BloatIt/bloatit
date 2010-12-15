package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class SearchUrl extends Url {
public SearchUrl() {
    super("search"); 
}
public SearchUrl(Parameters params) {
    this();
    parseParameters(params);
}
private java.lang.String searchString;
private PagedListUrl pagedMemberListUrl = new PagedListUrl();

public java.lang.String getSearchString(){ 
    return this.searchString;
}

public void setSearchString(java.lang.String arg0){ 
    this.searchString = arg0;
}

public PagedListUrl getPagedMemberListUrl(){ 
    return this.pagedMemberListUrl;
}

public void setPagedMemberListUrl(PagedListUrl arg0){ 
    this.pagedMemberListUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("global_search", getSearchString(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(pagedMemberListUrl);
}

public SearchUrl clone() { 
    SearchUrl other = new SearchUrl();
    other.searchString = this.searchString;
    other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
    return other;
}
}
