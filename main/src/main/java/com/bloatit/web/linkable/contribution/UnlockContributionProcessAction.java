package com.bloatit.web.linkable.contribution;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.UnlockContributionProcessActionUrl;

@ParamContainer("contribution/unlock")
public final class UnlockContributionProcessAction extends LoggedAction {

    @RequestParam
    private final ContributionProcess process;

    public UnlockContributionProcessAction(final UnlockContributionProcessActionUrl url) {
        super(url);
        process = url.getProcess();
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        process.setLock(false);
        return new CheckContributionPageUrl(process);
    }

    @Override
    protected Url doProcessErrors() {
        return session.pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You cannot modify a contribution without being logged.");
    }

    @Override
    protected void transmitParameters() {
        // nothing to do
    }

}
