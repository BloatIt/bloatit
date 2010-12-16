package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class LoginPageUrl extends Url {
public static String getName() { return "LoginPage"; }
public LoginPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public LoginPageUrl() {
    super(getName());
}


@Override 
protected void doRegister() { 
}

public LoginPageUrl clone() { 
    LoginPageUrl other = new LoginPageUrl();
    return other;
}
}
