package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class SpecialsPageUrl extends Url {
    public static String getName() {
        return "special";
    }

    @Override
    public com.bloatit.web.html.pages.SpecialsPage createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.SpecialsPage(this);
    }

    public SpecialsPageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public SpecialsPageUrl() {
        super(getName());
    }

    @Override
    protected void doRegister() {
    }

    @Override
    public SpecialsPageUrl clone() {
        final SpecialsPageUrl other = new SpecialsPageUrl();
        return other;
    }
}
