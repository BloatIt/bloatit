package com.bloatit.framework.webprocessor.url;

import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;

public class UrlString extends Url {

    private final String url;

    public UrlString(final String url) {
        super();
        this.url = url;
    }

    @Override
    public Url clone() {
        return new UrlString(url);
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

    @Override
    public boolean isAction() {
        return false;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    protected void doGetParametersAsStrings(final Parameters parameters) {
        // nothing to do. There are no parameters.
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.AUTO;
    }

}
