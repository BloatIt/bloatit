package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class LoggedPageUrl extends UrlComponent {
public LoggedPageUrl(Parameters params) {
    super();
    parseParameters(params);
}
public LoggedPageUrl() {
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
