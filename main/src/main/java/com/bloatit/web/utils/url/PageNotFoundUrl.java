package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class PageNotFoundUrl extends Url {
public static String getName() { return "pageNotFound"; }
public com.bloatit.web.html.pages.PageNotFound createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.PageNotFound(this); }
public PageNotFoundUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public PageNotFoundUrl(){
    super(getName());
}


@Override 
protected void doRegister() { 
}

@Override 
public PageNotFoundUrl clone() { 
    PageNotFoundUrl other = new PageNotFoundUrl();
    return other;
}
}
