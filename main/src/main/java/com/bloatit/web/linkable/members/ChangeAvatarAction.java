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
package com.bloatit.web.linkable.members;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.ChangeAvatarActionUrl;

/**
 * A response to a form used to change user avatar
 */
@ParamContainer("member/changeavatar")
public final class ChangeAvatarAction extends UserContentAction {
    @ParamConstraint(optionalErrorMsg = @tr("An avatar must be linked to a member"))
    @RequestParam
    private final Member member;

    private final ChangeAvatarActionUrl url;

    public ChangeAvatarAction(final ChangeAvatarActionUrl url) {
        super(url, UserTeamRight.TALK);
        this.url = url;
        this.member = url.getMember();
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {
        member.setAvatar(getFile());
        session.notifyGood(tr("Avatar change to ''{0}''", getAttachementFileName()));
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member me) {
        // TODO create a member.canAccessAvatar.
        return NO_ERROR;
    }
    
    @Override
    protected boolean verifyFile(final String filename) {
        final FileConstraintChecker fcc = new FileConstraintChecker(filename);
        if (fcc.isImageAvatar() != null) {
            for (final String message : fcc.isImageAvatar()) {
                session.notifyBad(message);
            }
            return false;
        }
        return true;
    }

    @Override
    protected Url doProcessErrors() {
        // TODO can we use something else than pickPreferredPage
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You have to be logged to change your avatar.");
    }

    @Override
    protected void doTransmitParameters() {
        session.addParameter(url.getMemberParameter());
    }
}
