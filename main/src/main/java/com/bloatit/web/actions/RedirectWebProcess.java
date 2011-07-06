package com.bloatit.web.actions;

import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;

public class RedirectWebProcess extends WebProcess {


    Url redirectUrl;

    public RedirectWebProcess(Url url) {
        super(url);
        redirectUrl = url;
    }

    @Override
    protected synchronized Url notifyChildClosed(WebProcess subProcess) {
        close();
        return redirectUrl;
    }


    @Override
    protected void doLoad() {

    }

    @Override
    protected Url doProcess(ElveosUserToken token) {
        return null;
    }

    @Override
    protected Url doProcessErrors(ElveosUserToken token) {
        return null;
    }

}
