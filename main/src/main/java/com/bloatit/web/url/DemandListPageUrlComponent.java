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
public final class DemandListPageUrlComponent extends UrlComponent {
public DemandListPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public DemandListPageUrlComponent(){
    super();
    try {
        this.filter.setValue(Loaders.fromStr(java.lang.String.class, "in progress"));
        this.sort.setValue(Loaders.fromStr(java.lang.String.class, "popularity"));
        this.searchString.setValue(Loaders.fromStr(java.lang.String.class, ""));
    } catch (ConversionErrorException e) {
        Log.web().fatal(e);
        assert false ;
    }
}
private UrlParameter<java.lang.String, java.lang.String> filter =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("filter", java.lang.String.class, Role.GET, "in progress", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> sort =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("sort", java.lang.String.class, Role.GET, "popularity", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> searchString =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("search_string", java.lang.String.class, Role.GET, "", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>());
private HtmlPagedListUrl pagedDemandListUrl = new HtmlPagedListUrl();

public java.lang.String getFilter(){ 
    return this.filter.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getFilterParameter(){ 
    return this.filter;
}

public void setFilter(java.lang.String arg){ 
    this.filter.setValue(arg);
}

public java.lang.String getSort(){ 
    return this.sort.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getSortParameter(){ 
    return this.sort;
}

public void setSort(java.lang.String arg){ 
    this.sort.setValue(arg);
}

public java.lang.String getSearchString(){ 
    return this.searchString.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getSearchStringParameter(){ 
    return this.searchString;
}

public void setSearchString(java.lang.String arg){ 
    this.searchString.setValue(arg);
}

public HtmlPagedListUrl getPagedDemandListUrl(){ 
    return this.pagedDemandListUrl;
}

public void setPagedDemandListUrl(HtmlPagedListUrl arg){ 
    this.pagedDemandListUrl = arg;
}


@Override 
protected void doRegister() { 
    register(filter);
    register(sort);
    register(searchString);
    register(pagedDemandListUrl);
}

@Override 
public DemandListPageUrlComponent clone() { 
    DemandListPageUrlComponent other = new DemandListPageUrlComponent();
    other.filter = this.filter.clone();
    other.sort = this.sort.clone();
    other.searchString = this.searchString.clone();
    other.pagedDemandListUrl = this.pagedDemandListUrl.clone();
    return other;
}
}
