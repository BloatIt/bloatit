package com.bloatit.framework.webserver.url;

public class UrlStringBinder extends Url {

    private final String url;

    public UrlStringBinder(final String url) {
        super("");
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

    @Override
    protected void doConstructUrl(final StringBuilder sb) {
        // nothing to do here. All the work is done in Url.
    }

    @Override
    public void addParameter(final String key, final String value) {
        // nothing to do here. There is no parameters in UrlStringBinder
    }

    @Override
    public Messages getMessages() {
        return new Messages();
    }

}
