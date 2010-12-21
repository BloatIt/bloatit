package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class AccountChargingActionUrl extends Url {
public static String getName() { return "account/charging"; }
public com.bloatit.web.actions.AccountChargingAction createPage() throws RedirectException{ 
    return new com.bloatit.web.actions.AccountChargingAction(this); }
public AccountChargingActionUrl(Parameters params, Parameters session) {
    super(getName());
    parseParameters(params, false);
    parseParameters(session, true);
}
public AccountChargingActionUrl() {
    super(getName());
}
private Parameter<java.math.BigDecimal> amount =     new Parameter<java.math.BigDecimal>("chargeAmount", null, java.math.BigDecimal.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

public java.math.BigDecimal getAmount(){ 
    return this.amount.getValue();
}

public void setAmount(java.math.BigDecimal arg){ 
    this.amount.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(amount);
}

public AccountChargingActionUrl clone() { 
    AccountChargingActionUrl other = new AccountChargingActionUrl();
    other.amount = this.amount.clone();
    return other;
}
}
