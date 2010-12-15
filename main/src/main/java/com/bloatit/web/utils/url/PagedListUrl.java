package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class PagedListUrl extends UrlComponent {
public PagedListUrl() {
    super(); 
}
public PagedListUrl(Parameters params) {
    this();
    parseParameters(params);
}
private java.lang.Integer currentPage;
private java.lang.Integer pageSize;

public java.lang.Integer getCurrentPage(){ 
    return this.currentPage;
}

public void setCurrentPage(java.lang.Integer arg0){ 
    this.currentPage = arg0;
}

public java.lang.Integer getPageSize(){ 
    return this.pageSize;
}

public void setPageSize(java.lang.Integer arg0){ 
    this.pageSize = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("current_page", getCurrentPage(), java.lang.Integer.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("page_size", getPageSize(), java.lang.Integer.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}

public PagedListUrl clone() { 
    PagedListUrl other = new PagedListUrl();
    other.currentPage = this.currentPage;
    other.pageSize = this.pageSize;
    return other;
}
}
