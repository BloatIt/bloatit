package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class MyAccountPageUrl extends Url {
public static String getName() { return "myaccount"; }
public com.bloatit.web.html.pages.MyAccountPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.MyAccountPage(this); }
public MyAccountPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public MyAccountPageUrl(){
    super(getName());
}


@Override 
protected void doRegister() { 
}

@Override 
public MyAccountPageUrl clone() { 
    MyAccountPageUrl other = new MyAccountPageUrl();
    return other;
}
}
