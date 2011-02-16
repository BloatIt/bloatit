package com.bloatit.framework.webserver.url;


public class UrlStringBinder extends Url {

    private final String url;

    public UrlStringBinder(final String url) {
        super("", UrlComponent.getEmptyComponent());
        this.url = url;
    }

    @Override
    public Url clone() {
        return new UrlStringBinder(url);
    }

    @Override
    public String urlString() {
        return url;
    }

}
