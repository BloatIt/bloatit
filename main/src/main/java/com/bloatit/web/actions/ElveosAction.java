package com.bloatit.web.actions;

import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.right.AuthToken;

public abstract class ElveosAction extends Action {
    private final AuthToken authToken;

    public ElveosAction(Url url) {
        super(url);
        this.authToken = (AuthToken) session.getAuthToken();
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

    protected abstract Url checkRightsAndEverything(AuthToken token);

    protected abstract Url doProcess(AuthToken token);

    protected abstract Url doProcessErrors(AuthToken token);

    protected abstract void transmitParameters();
}
