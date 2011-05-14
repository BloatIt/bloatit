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
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * A class used to join a public team.
 * </p>
 */
@ParamContainer("team/dojoin")
public final class JoinTeamAction extends LoggedAction {
    @SuppressWarnings("unused")
    private JoinTeamActionUrl url;

    @RequestParam()
    private final Team targetTeam;

    public JoinTeamAction(final JoinTeamActionUrl url) {
        super(url);
        this.targetTeam = url.getTargetTeam();
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        if (targetTeam.isPublic()) {
            try {
                me.addToPublicTeam(targetTeam);
                session.notifyGood(Context.tr("You are now a member of team ''{0}''.", targetTeam.getDisplayName()));
            } catch (final UnauthorizedOperationException e) {
                session.notifyBad(Context.tr("Oops we had an internal issue preventing you to join team. It's a bug, please notify us."));
                throw new ShallNotPassException("User tries to join public team, but is not allowed to", e);
            }
        } else {
            session.notifyBad(Context.tr("The team {0} is not public, you need an invitation to join it.", targetTeam.getDisplayName()));
            return session.getLastVisitedPage();
        }
        return new TeamPageUrl(targetTeam);
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors(final ElveosUserToken userToken) {
        if (targetTeam != null) {
            return new TeamPageUrl(targetTeam);
        }
        return session.getLastVisitedPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged before you try to join a team.");
    }

    @Override
    protected void transmitParameters() {
        // Nothing
    }
}
