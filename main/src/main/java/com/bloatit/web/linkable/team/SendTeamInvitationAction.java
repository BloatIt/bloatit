/*
 * 
 */
package com.bloatit.web.linkable.team;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.SendTeamInvitationActionUrl;

/**
 * <p>
 * An action used to send team invitations
 * </p>
 */
@ParamContainer("teams/%team%/dosendinvitation")
public final class SendTeamInvitationAction extends LoggedElveosAction {
    @RequestParam(role = Role.PAGENAME)
    private final Team team;

    @RequestParam(role = Role.POST)
    private final Member receiver;

    private final SendTeamInvitationActionUrl url;

    public SendTeamInvitationAction(final SendTeamInvitationActionUrl url) {
        super(url);
        this.url = url;
        this.team = url.getTeam();
        this.receiver = url.getReceiver();
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        if (!me.hasInviteTeamRight(team)) {
            session.notifyWarning(Context.tr("You are not allowed to send invitations for this team."));
            return session.getLastVisitedPage();
        }

        try {
            me.sendInvitation(receiver, team);
            session.notifyGood(Context.tr("Invitation sent to {0} for team {1}.", receiver.getDisplayName(), team.getDisplayName()));
        } catch (final UnauthorizedOperationException e) {
            session.notifyWarning(Context.tr("Oops, an error prevented us from sending this invitation. Please notify us of the bug."));
            throw new ShallNotPassException("User couldn't send a team invitation, while he should be able to", e);
        }
        return session.getLastVisitedPage();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to send an invitation");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getReceiverParameter());
    }
}
