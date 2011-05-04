package com.bloatit.web.actions;

import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;

public abstract class ElveosAction extends Action {
    private final ElveosUserToken authToken;

    public ElveosAction(Url url) {
        super(url);
        this.authToken = (ElveosUserToken) session.getUserToken();
    }

    @Override
    protected final Url checkRightsAndEverything() {
        return checkRightsAndEverything(authToken);
    }

    @Override
    protected final Url doProcess() {
        return doProcess(authToken);
    }

    @Override
    protected final Url doProcessErrors() {
        return doProcessErrors(authToken);
    }

    protected abstract Url checkRightsAndEverything(ElveosUserToken token);

    protected abstract Url doProcess(ElveosUserToken token);

    protected abstract Url doProcessErrors(ElveosUserToken token);

    protected abstract void transmitParameters();
}
