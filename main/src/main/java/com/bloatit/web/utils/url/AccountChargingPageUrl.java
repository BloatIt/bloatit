package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class AccountChargingPageUrl extends Url {
    public static String getName() {
        return "charging";
    }

    @Override
    public com.bloatit.web.html.pages.AccountChargingPage createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.AccountChargingPage(this);
    }

    public AccountChargingPageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public AccountChargingPageUrl() {
        super(getName());
    }

    @Override
    protected void doRegister() {
    }

    @Override
    public AccountChargingPageUrl clone() {
        final AccountChargingPageUrl other = new AccountChargingPageUrl();
        return other;
    }
}
