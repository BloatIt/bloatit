package com.bloatit.web.utils.url;


@SuppressWarnings("unused")
public final class LoggedPageUrl extends UrlComponent {
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

@Override 
public LoggedPageUrl clone() { 
    LoggedPageUrl other = new LoggedPageUrl();
    return other;
}
}
