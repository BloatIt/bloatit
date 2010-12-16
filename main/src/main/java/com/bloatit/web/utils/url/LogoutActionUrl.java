package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class LogoutActionUrl extends Url {
public static String getName() { return "LogoutAction"; }
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
