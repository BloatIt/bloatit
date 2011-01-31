package com.bloatit.web.utils.url;

import com.bloatit.web.server.Context;
import com.bloatit.web.server.Linkable;

public abstract class Url extends UrlComponent {

    private final String name;
    private String anchor = null;

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
        if(anchor != null) {
            sb.append("#").append(anchor);
        }
    }

    public abstract Linkable createPage();

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

}
