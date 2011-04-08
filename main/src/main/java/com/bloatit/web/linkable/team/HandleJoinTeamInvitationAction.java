package com.bloatit.web.linkable.team;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.JoinTeamInvitation;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.HandleJoinTeamInvitationActionUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * Action used to accept or refuse team invitations
 * </p>
 */
@ParamContainer("team/dojoin")
public class HandleJoinTeamInvitationAction extends LoggedAction {
    @RequestParam()
    private final JoinTeamInvitation invite;

    @RequestParam()
    private final Boolean accept;

    private final HandleJoinTeamInvitationActionUrl url;

    public HandleJoinTeamInvitationAction(final HandleJoinTeamInvitationActionUrl url) {
        super(url);
        this.url = url;
        this.accept = url.getAccept();
        this.invite = url.getInvite();
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        if (accept) {
            final Team g = invite.getTeam();

            if (me.isInTeam(g)) {
                session.notifyError(Context.tr("You cannot join a team you already belong in"));
                return session.getLastVisitedPage();
            }

            try {
                me.acceptInvitation(invite);
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                session.notifyBad(Context.tr("Ooops, you tried to accept a legitimate team invitation, but it failed, please notify us."));
                throw new ShallNotPassException("User accepted a legitimate team invitation, but it failed", e);
            }

            try {
                session.notifyGood(Context.tr("You are now a member of team ''" + g.getLogin() + "''"));
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                session.notifyBad(Context.tr("Ooops, we couldn't display team name. It's a bug, please notify us."));
                throw new ShallNotPassException("Couldn't display a team name, while user should be part of it", e);
            }
            return new TeamPageUrl(invite.getTeam());
        }
        try {
            me.refuseInvitation(invite);
        } catch (final UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("Ooops, you tried to accept a legitimate team invitation, but it failed. It's a bug please notify us."));
            throw new ShallNotPassException("User accepted a legitimate team invitation, but it failed", e);
        }
        return session.getLastVisitedPage();
    }

    @Override
    protected Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged before you accept (or refuse) a team invite");
    }

    @Override
    protected void transmitParameters() {
        // Nothing to transmit
    }
}
