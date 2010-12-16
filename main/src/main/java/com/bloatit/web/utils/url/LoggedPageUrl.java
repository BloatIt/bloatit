package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class LoggedPageUrl extends Url {
public static String getName() { return "LoggedPage"; }
public LoggedPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public LoggedPageUrl() {
    super(getName());
}


@Override 
protected void doRegister() { 
}

public LoggedPageUrl clone() { 
    LoggedPageUrl other = new LoggedPageUrl();
    return other;
}
}
