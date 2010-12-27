package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class OfferActionUrl extends Url {
    public static String getName() {
        return "action/offer";
    }

    @Override
    public com.bloatit.web.actions.OfferAction createPage() throws RedirectException {
        return new com.bloatit.web.actions.OfferAction(this);
    }

    public OfferActionUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public OfferActionUrl() {
        super(getName());
    }

    @Override
    protected void doRegister() {
    }

    @Override
    public OfferActionUrl clone() {
        final OfferActionUrl other = new OfferActionUrl();
        return other;
    }
}
