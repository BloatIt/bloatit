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

import com.bloatit.common.Log;
import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.GiveRightActionUrl;
import com.bloatit.web.url.TeamPageUrl;

@ParamContainer("team/dogiveright")
public class GiveRightAction extends LoggedAction {
    private final GiveRightActionUrl url;

    @RequestParam()
    private final Group targetTeam;

    @RequestParam
    private final Member targetMember;

    @RequestParam
    private final UserGroupRight right;

    @RequestParam
    private final Boolean give;

    public GiveRightAction(GiveRightActionUrl url) {
        super(url);
        this.url = url;
        this.targetTeam = url.getTargetTeam();
        this.targetMember = url.getTargetMember();
        this.right = url.getRight();
        this.give = url.getGive();
    }

    @Override
    public Url doProcessRestricted(Member authenticatedMember) {
        Member connected = session.getAuthToken().getMember();
        connected.authenticate(session.getAuthToken());

        if (!connected.canPromote(targetTeam)) {
            try {
                session.notifyBad(Context.tr("You are not allowed to promote people in the group: " + targetTeam.getLogin()));
            } catch (UnauthorizedOperationException e) {
                Log.web().warn("Couldn't display a group name", e);
                session.notifyBad(Context.tr("You are not allowed to promote people in this group"));
            }
            return new TeamPageUrl(targetTeam);
        }

        if (give) {
            targetMember.addGroupRight(targetTeam, right);
        } else {
            targetMember.removeGroupRight(targetTeam, right);
        }

        return new TeamPageUrl(targetTeam);
    }

    @Override
    protected Url doProcessErrors() {
        return null;
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to promote or demote a user");
    }

    @Override
    protected void transmitParameters() {

    }

}
