package com.bloatit.web.linkable.team;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
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
@ParamContainer("team/doacceptinvitation")
public final class HandleJoinTeamInvitationAction extends LoggedAction {
    @RequestParam(conversionErrorMsg = @tr("I cannot find the invitation number: ''%value%''."))
    @ParamConstraint(optionalErrorMsg = @tr("You have to specify an invitation."))
    private final JoinTeamInvitation invite;

    @RequestParam(conversionErrorMsg = @tr("I cannot understand if you have accepted the invitation. You wrote: ''%value%''."))
    @ParamConstraint(optionalErrorMsg = @tr("Does your invitation is accepted or refused?"))
    private final Boolean accept;

    // Keep it for consistency
    @SuppressWarnings("unused")
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
                session.notifyError(Context.tr("You cannot join a team you already belong in."));
                return session.getLastVisitedPage();
            }

            try {
                if (me.acceptInvitation(invite)) {
                    session.notifyGood(Context.tr("You are now a member of team ''{0}''.", g.getDisplayName()));
                } else {
                    session.notifyBad(Context.tr("You cannot join the team ''{0}'', maybe you already have discarded this invitation.", g.getDisplayName()));
                }
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                session.notifyBad(Context.tr("Ooops, we couldn't display team name. It's a bug, please notify us."));
                throw new ShallNotPassException("Couldn't display a team name, while user should be part of it.", e);
            }
            return new TeamPageUrl(invite.getTeam());
        }
        try {
            me.refuseInvitation(invite);
        } catch (final UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("Ooops, you tried to refuse a legitimate team invitation, but it failed. It's a bug please notify us."));
            throw new ShallNotPassException("User refuse a legitimate team invitation, but it failed.", e);
        }
        return session.getLastVisitedPage();
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member me) {
        return NO_ERROR;
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
