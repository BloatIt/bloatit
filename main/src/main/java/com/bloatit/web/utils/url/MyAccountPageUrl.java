package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class MyAccountPageUrl extends Url {
public static String getName() { return "MyAccountPage"; }
public MyAccountPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public MyAccountPageUrl() {
    super(getName());
}


@Override 
protected void doRegister() { 
}

public MyAccountPageUrl clone() { 
    MyAccountPageUrl other = new MyAccountPageUrl();
    return other;
}
}
