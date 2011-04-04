package com.bloatit.web.linkable.team;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.specific.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.url.Url;
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
    public Url doProcessRestricted(Member authenticatedMember) {
        final Member me = session.getAuthToken().getMember();
        if (accept) {
            final Team g = invite.getTeam();

            if (me.isInTeam(g)) {
                session.notifyError(Context.tr("You cannot join a team you already belong in"));
                return session.getLastVisitedPage();
            }

            try {
                me.acceptInvitation(invite);
            } catch (final UnauthorizedOperationException e1) {
                // Should never happen
                Log.web().fatal("User accepted a legitimate team invitation, but it failed", e1);
                session.notifyBad(Context.tr("Oops looks like we had an issue with accepting this team invitation. Please try again later."));
                return session.getLastVisitedPage();
            }

            try {
                session.notifyGood(Context.tr("You are now a member of team ''" + g.getLogin() + "''"));
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Couldn't display a team name, while user should be part of it", e);
            }
            return new TeamPageUrl(invite.getTeam());
        }
        try {
            me.refuseInvitation(invite);
        } catch (final UnauthorizedOperationException e) {
            Log.web().fatal("User accepted a legitimate team invitation, but it failed", e);
            session.notifyBad(Context.tr("Oops looks like we had an issue with refusing this team invitation. Please try again later."));
            return session.getLastVisitedPage();
        }
        return session.getLastVisitedPage();
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());
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
