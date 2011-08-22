package com.bloatit.web.linkable.contribution;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Contribution;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedElveosAction;
import com.bloatit.web.url.CancelContributionActionUrl;

@ParamContainer("contribution/docancel/%contribution%")
public class CancelContributionAction extends LoggedElveosAction {
    private CancelContributionActionUrl url;

    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the contribution number: ''%value%''."))
    @NonOptional(@tr("You have to specify a contribution number."))
    private Contribution contribution;

    public CancelContributionAction(CancelContributionActionUrl url) {
        super(url);
        this.url = url;
        this.contribution = url.getContribution();
    }

    @Override
    protected Url checkRightsAndEverything(Member me) {
        return null;
    }

    @Override
    protected Url doProcessRestricted(Member me) {
        return null;
    }

    @Override
    protected Url doProcessErrors() {
        return null;
    }

    @Override
    protected String getRefusalReason() {
        return null;
    }

    @Override
    protected void transmitParameters() {

    }
}
