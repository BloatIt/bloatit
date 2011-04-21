package com.bloatit.web.linkable.errors;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.NotFoundActionUrl;

@ParamContainer("dopagenotfound")
public class NotFoundAction extends Action {

    public NotFoundAction(NotFoundActionUrl url) {
        super(url);
    }

    @Override
    protected Url doProcess() {
        Context.getLocalizator().forceLanguageReset();
        return new PageNotFoundUrl();
    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        // Doesn't happen
        return null;
    }

    @Override
    protected void transmitParameters() {
        // No parameter
    }

}
