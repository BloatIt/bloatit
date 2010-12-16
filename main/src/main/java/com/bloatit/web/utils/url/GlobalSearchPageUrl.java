package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class GlobalSearchPageUrl extends Url {
public static String getName() { return "GlobalSearchPage"; }
public com.bloatit.web.html.pages.GlobalSearchPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.GlobalSearchPage(this); }
public GlobalSearchPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public GlobalSearchPageUrl() {
    super(getName());
    try {
        this.searchString = Loaders.fromStr(java.lang.String.class, "");
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private java.lang.String searchString;
private HtmlPagedListUrl pagedMemberListUrl = new HtmlPagedListUrl();

public java.lang.String getSearchString(){ 
    return this.searchString;
}

public void setSearchString(java.lang.String arg0){ 
    this.searchString = arg0;
}

public HtmlPagedListUrl getPagedMemberListUrl(){ 
    return this.pagedMemberListUrl;
}

public void setPagedMemberListUrl(HtmlPagedListUrl arg0){ 
    this.pagedMemberListUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("global_search", getSearchString(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(pagedMemberListUrl);
}

public GlobalSearchPageUrl clone() { 
    GlobalSearchPageUrl other = new GlobalSearchPageUrl();
    other.searchString = this.searchString;
    other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
    return other;
}
}
