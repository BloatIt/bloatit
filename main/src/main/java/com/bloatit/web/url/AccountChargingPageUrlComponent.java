package com.bloatit.web.url;

import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ConversionErrorException;
import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.*;
import com.bloatit.framework.webserver.url.*;
import com.bloatit.framework.webserver.url.Loaders.*;

@SuppressWarnings("unused")
public final class AccountChargingPageUrlComponent extends UrlComponent {
public AccountChargingPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public AccountChargingPageUrlComponent(){
    super();
}


@Override 
protected void doRegister() { 
}

@Override 
public AccountChargingPageUrlComponent clone() { 
    AccountChargingPageUrlComponent other = new AccountChargingPageUrlComponent();
    return other;
}
}
