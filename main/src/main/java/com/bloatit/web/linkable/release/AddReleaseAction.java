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
package com.bloatit.web.linkable.release;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.utils.FileConstraintChecker.SizeUnit;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.AddReleaseActionUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("release/doadd")
public final class AddReleaseAction extends UserContentAction {

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "10", minErrorMsg = @tr("The description must have at least 10 chars."), //
                     optionalErrorMsg = @tr("You forgot to write a description"))
    private final String description;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "1", minErrorMsg = @tr("The version should be something like ''1.2.3''."), //
                     optionalErrorMsg = @tr("You forgot to write a version."))
    private final String version;

    @RequestParam
    private final Milestone milestone;

    private final AddReleaseActionUrl url;

    public AddReleaseAction(final AddReleaseActionUrl url) {
        super(url, UserTeamRight.TALK);
        this.url = url;

        this.description = url.getDescription();
        this.milestone = url.getMilestone();
        this.version = url.getVersion();
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {
        try {
            milestone.addRelease(description, version, getLocale(), getFile());
            session.notifyGood(Context.tr("Release created successfully!"));

        } catch (final UnauthorizedOperationException e) {
            session.notifyError(Context.tr("Failed to create the release."));
            throw new ShallNotPassException("Fail to create a release.", e);
        }
        return session.pickPreferredPage();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        if (milestone.canAccessRelease(Action.WRITE)) {
            return NO_ERROR;
        }
        session.notifyError(Context.tr("You are not allowed to add a release on this milestone."));
        return doProcessErrors();
    }

    @Override
    protected boolean verifyFile(final String filename) {
        final FileConstraintChecker fcc = new FileConstraintChecker(filename);
        if (!fcc.exists() || !fcc.isFileSmaller(1, SizeUnit.GBYTE)) {
            session.notifyBad(Context.tr("File format error: Your file is to big."));
            return false;
        }
        return true;
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to create a release.");
    }

    @Override
    protected Url doProcessErrors(final ElveosUserToken userToken) {
        return session.getLastVisitedPage();
    }

    @Override
    protected void doTransmitParameters() {
        session.addParameter(url.getDescriptionParameter());
    }

}
