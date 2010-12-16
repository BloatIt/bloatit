package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class AccountChargingActionUrl extends Url {
public static String getName() { return "AccountChargingAction"; }
public com.bloatit.web.actions.AccountChargingAction createPage() throws RedirectException{ 
    return new com.bloatit.web.actions.AccountChargingAction(this); }
public AccountChargingActionUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public AccountChargingActionUrl() {
    super(getName());
}
private java.math.BigDecimal amount;

public java.math.BigDecimal getAmount(){ 
    return this.amount;
}

public void setAmount(java.math.BigDecimal arg0){ 
    this.amount = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("chargeAmount", getAmount(), java.math.BigDecimal.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}

public AccountChargingActionUrl clone() { 
    AccountChargingActionUrl other = new AccountChargingActionUrl();
    other.amount = this.amount;
    return other;
}
}
