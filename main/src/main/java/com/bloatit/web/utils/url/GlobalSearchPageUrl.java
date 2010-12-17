package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class GlobalSearchPageUrl extends Url {
public static String getName() { return "GlobalSearchPage"; }
public com.bloatit.web.html.pages.GlobalSearchPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.GlobalSearchPage(this); }
public GlobalSearchPageUrl(Parameters params, Parameters session) {
    super(getName());
    parseParameters(params, false);
    parseParameters(session, true);
}
public GlobalSearchPageUrl() {
    super(getName());
    try {
        this.searchString.setValue(Loaders.fromStr(java.lang.String.class, ""));
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private Parameter<java.lang.String> searchString =     new Parameter<java.lang.String>("global_search", null, java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private HtmlPagedListUrl pagedMemberListUrl = new HtmlPagedListUrl();

public java.lang.String getSearchString(){ 
    return this.searchString.getValue();
}

public void setSearchString(java.lang.String arg){ 
    this.searchString.setValue(arg);
}

public HtmlPagedListUrl getPagedMemberListUrl(){ 
    return this.pagedMemberListUrl;
}

public void setPagedMemberListUrl(HtmlPagedListUrl arg){ 
    this.pagedMemberListUrl = arg;
}


@Override 
protected void doRegister() { 
    register(searchString);
    register(pagedMemberListUrl);
}

public GlobalSearchPageUrl clone() { 
    GlobalSearchPageUrl other = new GlobalSearchPageUrl();
    other.searchString = this.searchString.clone();
    other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
    return other;
}
}
