package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class LogoutActionUrl extends Url {
public static String getName() { return "action/logout"; }
public com.bloatit.web.actions.LogoutAction createPage() throws RedirectException{ 
    return new com.bloatit.web.actions.LogoutAction(this); }
public LogoutActionUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public LogoutActionUrl(){
    super(getName());
}


@Override 
protected void doRegister() { 
}

@Override 
public LogoutActionUrl clone() { 
    LogoutActionUrl other = new LogoutActionUrl();
    return other;
}
}
