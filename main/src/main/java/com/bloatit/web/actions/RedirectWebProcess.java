package com.bloatit.web.actions;

import com.bloatit.framework.webprocessor.url.Url;

public class RedirectWebProcess extends WebProcess {

    Url redirectUrl;

    public RedirectWebProcess(final Url url) {
        super(url);
        redirectUrl = url;
    }

    @Override
    protected synchronized Url notifyChildClosed(final WebProcess subProcess) {
        close();
        return redirectUrl;
    }

    @Override
    protected void doLoad() {

    }

    @Override
    protected Url doProcess() {
        return null;
    }

    @Override
    protected Url doProcessErrors() {
        return null;
    }

}
