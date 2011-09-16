package com.bloatit.web.linkable.process;

import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.linkable.master.WebProcess;

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
