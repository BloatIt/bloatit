package com.bloatit.web.utils.url;


public class IndexPageUrl extends Url {
    public IndexPageUrl() {
        super("IndexPage");
    }

    public IndexPageUrl(final Parameters params) {
        this();
        parseParameters(params);
    }

    @Override
    protected void doRegister() {
    }

    @Override
    public IndexPageUrl clone() {
        final IndexPageUrl other = new IndexPageUrl();
        return other;
    }
}
