package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class LogoutActionUrl extends Url {
public LogoutActionUrl() {
    super("LogoutAction"); 
}
public LogoutActionUrl(Parameters params) {
    this();
    parseParameters(params);
}


@Override 
protected void doRegister() { 
}

public LogoutActionUrl clone() { 
    LogoutActionUrl other = new LogoutActionUrl();
    return other;
}
}
