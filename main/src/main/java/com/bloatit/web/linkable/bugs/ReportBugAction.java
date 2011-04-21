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
package com.bloatit.web.linkable.bugs;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Bug;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.Team;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.ReportBugActionUrl;
import com.bloatit.web.url.ReportBugPageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("feature/bug/doreport")
public final class ReportBugAction extends UserContentAction {

    @ParamConstraint(optionalErrorMsg = @tr("A new bug must be linked to a milestone"))
    @RequestParam(role = Role.GET)
    private final Milestone milestone;

    @RequestParam(role = Role.POST)
    @ParamConstraint(max = "120",
                     maxErrorMsg = @tr("The short description must be 120 chars length max."), //
                     min = "10", minErrorMsg = @tr("The short description must have at least 10 chars."),
                     optionalErrorMsg = @tr("You forgot to write a short description"))
    private final String title;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a bug description"), min = "10",
                     minErrorMsg = @tr("The description must have at least 10 chars."))
    @RequestParam(role = Role.POST)
    private final String description;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a bug level"))
    @RequestParam(suggestedValue = "MINOR", role = Role.POST)
    private final BindedLevel level;

    private final ReportBugActionUrl url;

    public ReportBugAction(final ReportBugActionUrl url) {
        super(url, UserTeamRight.TALK);
        this.url = url;

        this.title = url.getTitle();
        this.description = url.getDescription();
        this.level = url.getLevel();
        this.milestone = url.getMilestone();
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {
        Bug bug;
        try {
            bug = milestone.addBug(title, description, getLocale(), level.getLevel());
            propagateAttachedFileIfPossible(bug);
            return new BugPageUrl(bug);
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException(e);
        }
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member me) {
        if (getLocale() == null) {
            session.notifyBad(Context.tr("You have to specify the description language."));
            return new ReportBugPageUrl(milestone.getOffer());
        }
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        if (milestone != null) {
            return new ReportBugPageUrl(milestone.getOffer());
        }
        return new PageNotFoundUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged in to report a bug.");
    }

    @Override
    protected void doTransmitParameters() {
        session.addParameter(url.getTitleParameter());
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getMilestoneParameter());
        session.addParameter(url.getLevelParameter());
        session.addParameter(url.getAttachmentDescriptionParameter());
    }

    @Override
    protected boolean verifyFile(final String filename) {
        // TODO verify the file
        return true;
    }

}
