package com.bloatit.web.linkable.login;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;

@ParamContainer("password/dorecover")
public class RecoverPasswordAction extends Action {

    public RecoverPasswordAction(Url url) {
        super(url);
    }

    @Override
    protected Url checkRightsAndEverything() {
        return null;
    }

    @Override
    protected Url doProcess() {
        return null;
    }

    @Override
    protected Url doProcessErrors() {
        return null;
    }

    @Override
    protected void transmitParameters() {

    }
}
