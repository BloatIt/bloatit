package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class AccountChargingPageUrl extends Url {
public AccountChargingPageUrl() {
    super("AccountChargingPage"); 
}
public AccountChargingPageUrl(Parameters params) {
    this();
    parseParameters(params);
}


@Override 
protected void doRegister() { 
}

public AccountChargingPageUrl clone() { 
    AccountChargingPageUrl other = new AccountChargingPageUrl();
    return other;
}
}
