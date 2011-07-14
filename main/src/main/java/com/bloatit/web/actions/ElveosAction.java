package com.bloatit.web.actions;

import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;

public abstract class ElveosAction extends Action {

    public ElveosAction(final Url url) {
        super(url);
    }

    @Override
    protected abstract Url checkRightsAndEverything();

    @Override
    protected abstract Url doProcess();

    @Override
    protected abstract Url doProcessErrors();

    @Override
    protected abstract void transmitParameters();
}
