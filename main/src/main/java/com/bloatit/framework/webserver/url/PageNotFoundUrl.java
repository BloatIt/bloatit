package com.bloatit.framework.webserver.url;

import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.utils.SessionParameters;

@SuppressWarnings("unused")
public final class PageNotFoundUrl extends Url implements Cloneable {
    public static String getName() {
        return "pagenotfound";
    }

    @Override
    public com.bloatit.web.pages.PageNotFound createPage() {
        return new com.bloatit.web.pages.PageNotFound(this);
    }

    public PageNotFoundUrl(final Parameters params, final SessionParameters session) {
        this();
        parseSessionParameters(session);
        parseParameters(params);
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
