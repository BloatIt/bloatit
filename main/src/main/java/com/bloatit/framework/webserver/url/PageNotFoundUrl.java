package com.bloatit.framework.webserver.url;

import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.utils.SessionParameters;

@SuppressWarnings("unused")
public final class PageNotFoundUrl extends Url implements Cloneable {
    public static String getName() {
        return "pagenotfound";
    }

    public PageNotFoundUrl(final Parameters params, final SessionParameters session) {
        this();
    }

    public PageNotFoundUrl() {
        super(getName(), UrlComponent.getEmptyComponent());
    }

    @Override
    public PageNotFoundUrl clone() {
        final PageNotFoundUrl other = new PageNotFoundUrl();
        return other;
    }
}
