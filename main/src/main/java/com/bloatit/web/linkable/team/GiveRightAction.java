/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.team;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.MemberNotInTeamException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.GiveRightActionUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * Action used to give a user a new right in a team
 */
@ParamContainer("teams/%team%/dogiveright")
public final class GiveRightAction extends LoggedElveosAction {
    @SuppressWarnings("unused")
    // Kept for consistency
    private final GiveRightActionUrl url;

    @RequestParam(role = Role.PAGENAME)
    private final Team team;

    @RequestParam
    private final Member targetMember;

    @RequestParam
    private final UserTeamRight right;

    @RequestParam
    private final Boolean give;

    public GiveRightAction(final GiveRightActionUrl url) {
        super(url);
        this.url = url;
        this.team = url.getTeam();
        this.targetMember = url.getTargetMember();
        this.right = url.getRight();
        this.give = url.getGive();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        if (right == UserTeamRight.CONSULT && !give) {
            if (!targetMember.canBeKickFromTeam(team, me)) {
                session.notifyWarning(Context.tr("You are not allowed to remove people in the team."));
                throw new ShallNotPassException("Cannot display a team name");
            }
        } else {
            if (!team.canChangeRight(me, targetMember, right, give)) {
                session.notifyWarning(Context.tr("You are not allowed to promote people in the team {0}.", team.getDisplayName()));
                return new TeamPageUrl(team);
            }
        }
        return NO_ERROR;
    }

    @Override
    public Url doProcessRestricted(final Member me) {

        if (right == UserTeamRight.CONSULT && !give) {
            // Remove member from team
            try {
                targetMember.kickFromTeam(team, me);
            } catch (final UnauthorizedOperationException e) {
                session.notifyWarning("For an obscure reason you cannot remove this member from the team, please warn us of the bug.");
                throw new ShallNotPassException("Cannot remove a member from a team", e);
            }
        } else {
            try {
                team.changeRight(me, targetMember, right, give);
            } catch (final UnauthorizedOperationException e) {
                waitingForJava7(e);
            } catch (final MemberNotInTeamException e) {
                waitingForJava7(e);
            }
        }

        return new TeamPageUrl(team);
    }

    private void waitingForJava7(final Exception e) {
        session.notifyWarning("For an obscure reason you cannot remove this member from the team, please warn us of the bug.");
        throw new ShallNotPassException("Cannot remove a member from a team", e);
    }

    @Override
    protected Url doProcessErrors() {
        return new PageNotFoundUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to promote or demote a user");
    }

    @Override
    protected void transmitParameters() {
        // Nothing
    }
}
