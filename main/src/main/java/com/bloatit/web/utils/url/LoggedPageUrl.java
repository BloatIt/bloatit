package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;

@SuppressWarnings("unused")
public class LoggedPageUrl extends UrlComponent {
public LoggedPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public LoggedPageUrl(){
    super();
}


@Override 
protected void doRegister() { 
}

public LoggedPageUrl clone() { 
    LoggedPageUrl other = new LoggedPageUrl();
    return other;
}
}
