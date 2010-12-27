package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class AccountChargingActionUrl extends Url {
    public static String getName() {
        return "action/account/charging";
    }

    @Override
    public com.bloatit.web.actions.AccountChargingAction createPage() throws RedirectException {
        return new com.bloatit.web.actions.AccountChargingAction(this);
    }

    public AccountChargingActionUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public AccountChargingActionUrl() {
        super(getName());
    }

    private UrlParameter<java.math.BigDecimal> amount = new UrlParameter<java.math.BigDecimal>("chargeAmount", null, java.math.BigDecimal.class,
            Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

    public java.math.BigDecimal getAmount() {
        return this.amount.getValue();
    }

    public void setAmount(final java.math.BigDecimal arg) {
        this.amount.setValue(arg);
    }

    @Override
    protected void doRegister() {
        register(amount);
    }

    @Override
    public AccountChargingActionUrl clone() {
        final AccountChargingActionUrl other = new AccountChargingActionUrl();
        other.amount = this.amount.clone();
        return other;
    }
}
