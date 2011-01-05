package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.ConversionErrorException;

@SuppressWarnings("unused")
public final class HtmlPagedListUrl extends UrlComponent {
public HtmlPagedListUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public HtmlPagedListUrl(){
    super();
    try {
        this.currentPage.setValue(Loaders.fromStr(java.lang.Integer.class, "1"));
        this.pageSize.setValue(Loaders.fromStr(java.lang.Integer.class, "10"));
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private UrlParameter<java.lang.Integer> currentPage =     new UrlParameter<java.lang.Integer>("current_page", null, java.lang.Integer.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.Integer> pageSize =     new UrlParameter<java.lang.Integer>("page_size", null, java.lang.Integer.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

public java.lang.Integer getCurrentPage(){ 
    return this.currentPage.getValue();
}

public void setCurrentPage(java.lang.Integer arg){ 
    this.currentPage.setValue(arg);
}

public java.lang.Integer getPageSize(){ 
    return this.pageSize.getValue();
}

public void setPageSize(java.lang.Integer arg){ 
    this.pageSize.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(currentPage);
    register(pageSize);
}

@Override 
public HtmlPagedListUrl clone() { 
    HtmlPagedListUrl other = new HtmlPagedListUrl();
    other.currentPage = this.currentPage.clone();
    other.pageSize = this.pageSize.clone();
    return other;
}
}
