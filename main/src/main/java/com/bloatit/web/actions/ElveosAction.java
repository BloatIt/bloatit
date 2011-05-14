package com.bloatit.web.actions;

import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;

public abstract class ElveosAction extends Action {
    private final ElveosUserToken userToken;

    public ElveosAction(final Url url) {
        super(url);
        this.userToken = (ElveosUserToken) session.getUserToken();
    }

    @Override
    protected final Url checkRightsAndEverything() {
        return checkRightsAndEverything(userToken);
    }

    @Override
    protected final Url doProcess() {
        return doProcess(userToken);
    }

    @Override
    protected final Url doProcessErrors() {
        return doProcessErrors(userToken);
    }

    protected abstract Url checkRightsAndEverything(ElveosUserToken token);

    protected abstract Url doProcess(ElveosUserToken token);

    protected abstract Url doProcessErrors(ElveosUserToken token);

    @Override
    protected abstract void transmitParameters();
}
