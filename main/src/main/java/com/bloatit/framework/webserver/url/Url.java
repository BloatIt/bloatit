package com.bloatit.framework.webserver.url;

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.masters.Linkable;

/**
 * Represent a web Url. A Url is a kind of {@link UrlComponent}, with a page name. It also
 * can have a ahchor part.
 */
public abstract class Url extends UrlComponent {

    private final String name;
    private String anchor = null;

    /**
     * Create a Url using its name.
     */
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
        if (anchor != null) {
            sb.append("#").append(anchor);
        }
    }

    public abstract Linkable createPage();

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(final String anchor) {
        this.anchor = anchor;
    }

}
