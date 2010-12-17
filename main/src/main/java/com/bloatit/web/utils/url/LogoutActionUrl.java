package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class LogoutActionUrl extends Url {
public static String getName() { return "LogoutAction"; }
public com.bloatit.web.actions.LogoutAction createPage() throws RedirectException{ 
    return new com.bloatit.web.actions.LogoutAction(this); }
public LogoutActionUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public LogoutActionUrl() {
    super(getName());
}


@Override 
protected void doRegister() { 
}

public LogoutActionUrl clone() { 
    LogoutActionUrl other = new LogoutActionUrl();
    return other;
}
}
