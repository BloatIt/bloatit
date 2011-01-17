package com.bloatit.web.utils.url;

import com.bloatit.web.server.Linkable;

public class UrlStringBinder extends Url {

    private final String url;

    public UrlStringBinder(String url) {
        super("");
        this.url = url;
    }

    @Override
    public Linkable createPage() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doRegister() {
        throw new UnsupportedOperationException();

    }

    @Override
    public UrlComponent clone() {
        return new UrlStringBinder(url);
    }

    @Override
    protected void constructUrl(StringBuilder sb) {
        sb.append(url);
    }


}
