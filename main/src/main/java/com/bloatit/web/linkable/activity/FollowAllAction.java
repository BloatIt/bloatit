/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.activity;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.FollowAllActionUrl;

/**
 * A response to a form used to assess any <code>kudosable</code> on the bloatit
 * website
 */
@ParamContainer("activity/follow/all")
public final class FollowAllAction extends LoggedElveosAction {

    @RequestParam()
    @NonOptional(@tr("You must indicate a follow state"))
    private final Boolean follow;

    @RequestParam()
    @NonOptional(@tr("You must indicate a follow mail state"))
    private final Boolean followMail;

    public FollowAllAction(final FollowAllActionUrl url) {
        super(url);
        follow = url.getFollow();
        followMail = url.getFollowMail();
    }

    @Override
    public Url doProcessRestricted(final Member me) throws UnauthorizedOperationException {

        if (follow) {
            me.setGlobalFollow(true);
            me.setGlobalFollowWithMail(followMail);
        } else {
            me.setGlobalFollow(false);
            me.setGlobalFollowWithMail(false);
        }

        return session.pickPreferredPage();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return session.pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return "You must be logged to follow something";
    }

    @Override
    protected void transmitParameters() {
        // Nothing to save
    }

}
