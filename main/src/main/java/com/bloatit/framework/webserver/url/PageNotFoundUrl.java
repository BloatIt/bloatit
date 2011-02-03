package com.bloatit.framework.webserver.url;

import com.bloatit.framework.utils.Parameters;

@SuppressWarnings("unused")
public final class PageNotFoundUrl extends Url implements Cloneable {
    public static String getName() {
        return "pageNotFound";
    }

    @Override
    public com.bloatit.web.pages.PageNotFound createPage() {
        return new com.bloatit.web.pages.PageNotFound(this);
    }

    public PageNotFoundUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(session, true);
        parseParameters(params, false);
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
