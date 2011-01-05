package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Linkable;

public abstract class Url extends UrlComponent {

    private final String name;

    protected Url(final String name) {
        super();
        this.name = name;
    }

    @Override
    protected void constructUrl(final StringBuilder sb) {
        if (Context.getSession() != null) {
            sb.append("/").append(Context.getLocalizator().getCode());
        }
        sb.append("/").append(name);
        super.constructUrl(sb);
    }

    public abstract Linkable createPage() throws RedirectException;

}
