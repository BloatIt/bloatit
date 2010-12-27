package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class IndexPageUrl extends Url {
    public static String getName() {
        return "index";
    }

    @Override
    public com.bloatit.web.html.pages.IndexPage createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.IndexPage(this);
    }

    public IndexPageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public IndexPageUrl() {
        super(getName());
    }

    @Override
    protected void doRegister() {
    }

    @Override
    public IndexPageUrl clone() {
        final IndexPageUrl other = new IndexPageUrl();
        return other;
    }
}
