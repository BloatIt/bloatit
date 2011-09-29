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
package com.bloatit.web.linkable.meta.feedback;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bloatit.framework.meta.MetaFeedbackManager;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.linkable.master.ElveosAction;
import com.bloatit.web.url.MetaReportFeedbackActionUrl;

/**
 * An action used to create a feedback
 */
@ParamContainer("meta/feedback/doreport")
public final class MetaReportFeedbackAction extends ElveosAction {

    private static final String FEEDBACK_DESCRIPTION = "feedback_description";
    protected static final String FEEDBACK_URL = "feedback_url";

    @RequestParam(name = FEEDBACK_DESCRIPTION, role = Role.POST)
    @MaxConstraint(max = 80000, message = @tr("The description must be %constraint% chars length max."))
    @MinConstraint(min = 10, message = @tr("The description must have at least %constraint% chars."))
    @NonOptional(@tr("Error you forgot to write a description"))
    private final String description;

    @RequestParam(name = FEEDBACK_URL, role = Role.POST)
    private final String feedbackUrl;

    private final MetaReportFeedbackActionUrl url;

    public MetaReportFeedbackAction(final MetaReportFeedbackActionUrl url) {
        super(url);
        this.url = url;
        this.description = url.getDescription();
        this.feedbackUrl = url.getFeedbackUrl();
    }

    @Override
    protected Url doProcess() {
        String bugReport = "";
        bugReport += "* **Url:** " + feedbackUrl + "\n";
        bugReport += "* **Author:** " + (AuthToken.isAuthenticated() ? AuthToken.getMember().getLogin() : "not logged") + "\n";
        bugReport += "* **Date:** " + new SimpleDateFormat().format(new Date()) + "\n";
        bugReport += "\n";
        bugReport += description;

        if (MetaFeedbackManager.reportBug(bugReport)) {
            session.notifyGood("Feedback reported, Thanks!");
        } else {
            session.notifyError("A problem occur during the feedback report process! Please report this bug! :)");
        }
        return session.getLastVisitedPage();
    }

    @Override
    protected Url doProcessErrors() {
        session.addParameter(url.getDescriptionParameter());
        return session.getLastVisitedPage();
    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR; // Nothing else to check
    }

    @Override
    protected void transmitParameters() {
        // nothing
    }
}
