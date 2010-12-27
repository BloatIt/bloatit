package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class PageNotFoundUrl extends Url {
    public static String getName() {
        return "pageNotFound";
    }

    @Override
    public com.bloatit.web.html.pages.PageNotFound createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.PageNotFound(this);
    }

    public PageNotFoundUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public PageNotFoundUrl() {
        super(getName());
    }

    @Override
    protected void doRegister() {
    }

    @Override
    public PageNotFoundUrl clone() {
        final PageNotFoundUrl other = new PageNotFoundUrl();
        return other;
    }
}
