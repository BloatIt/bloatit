package com.bloatit.web.linkable.members.tabs.dashboard;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Follow;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.StopFollowActionUrl;

@ParamContainer("follows/%follow%/docancel")
public class StopFollowAction extends LoggedElveosAction {

    @NonOptional(@tr("You have to specify a follow number."))
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the follow number: ''%value%''."))
    private final Follow follow;

    @SuppressWarnings("unused")
    // For consistency sake
    private StopFollowActionUrl url;

    public StopFollowAction(StopFollowActionUrl url) {
        super(url);
        this.follow = url.getFollow();
        this.url = url;
    }

    @Override
    protected Url doProcessRestricted(Member me) {
        follow.delete();
        MemberPageUrl memberPageUrl = new MemberPageUrl(me);
        memberPageUrl.setActiveTabKey(MemberPage.DASHBOARD_TAB);
        return memberPageUrl;
    }

    @Override
    protected Url checkRightsAndEverything(Member me) {
        if (follow.getActor().equals(me)) {
            return NO_ERROR;
        }
        return new MemberPageUrl(me);
    }

    @Override
    protected Url doProcessErrors() {
        return new IndexPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to stop following content");
    }

    @Override
    protected void transmitParameters() {

    }
}
