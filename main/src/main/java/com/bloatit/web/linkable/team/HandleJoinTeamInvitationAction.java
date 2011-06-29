//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.team;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.JoinTeamInvitation;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPrivateReadOnlyAccessException;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.HandleJoinTeamInvitationActionUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * Action used to accept or refuse team invitations
 * </p>
 */
@ParamContainer("team/invitation/doacceptinvitation")
public final class HandleJoinTeamInvitationAction extends LoggedAction {
    @RequestParam(message = @tr("I cannot find the invitation number: ''%value%''."))
    @NonOptional(@tr("You have to specify an invitation."))
    private final JoinTeamInvitation invite;

    @RequestParam(message = @tr("I cannot understand if you have accepted the invitation. You wrote: ''%value%''."))
    @NonOptional(@tr("Does your invitation is accepted or refused?"))
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
            Team team;
            try {
                team = invite.getTeam();
            } catch (final UnauthorizedPrivateReadOnlyAccessException e1) {
                session.notifyWarning(Context.tr("This invitation is not yours, you are not allowed to see it."));
                return session.getLastVisitedPage();
            } catch (final UnauthorizedOperationException e) {
                session.notifyWarning(Context.tr("This invitation is not yours, you are not allowed to see it."));
                return session.getLastVisitedPage();
            }

            if (me.isInTeam(team)) {
                session.notifyError(Context.tr("You cannot join a team you already belong in."));
                return session.getLastVisitedPage();
            }

            try {
                if (me.acceptInvitation(invite)) {
                    session.notifyGood(Context.tr("You are now a member of team ''{0}''.", team.getDisplayName()));
                } else {
                    session.notifyWarning(Context.tr("You cannot join the team ''{0}'', maybe you already have discarded this invitation.",
                                                 team.getDisplayName()));
                }
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                session.notifyWarning(Context.tr("Ooops, we couldn't display team name. It's a bug, please notify us."));
                throw new ShallNotPassException("Couldn't display a team name, while user should be part of it.", e);
            }
            return new TeamPageUrl(team);
        }
        try {
            me.refuseInvitation(invite);
        } catch (final UnauthorizedOperationException e) {
            session.notifyWarning(Context.tr("Ooops, you tried to refuse a legitimate team invitation, but it failed. It's a bug please notify us."));
            throw new ShallNotPassException("User refuse a legitimate team invitation, but it failed.", e);
        }
        return session.getLastVisitedPage();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors(final ElveosUserToken userToken) {
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
