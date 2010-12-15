package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class AccountChargingActionUrl extends Url {
public AccountChargingActionUrl() {
    super("AccountChargingAction"); 
}
public AccountChargingActionUrl(Parameters params) {
    this();
    parseParameters(params);
}


@Override 
protected void doRegister() { 
}

public AccountChargingActionUrl clone() { 
    AccountChargingActionUrl other = new AccountChargingActionUrl();
    return other;
}
}
