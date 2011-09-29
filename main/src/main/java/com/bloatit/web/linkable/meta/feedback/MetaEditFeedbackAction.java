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

import java.io.IOException;

import com.bloatit.framework.meta.MetaFeedbackManager;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.MetaFeedbackListPageUrl;
import com.bloatit.web.url.MetaEditFeedbackActionUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("meta/feedback/doedit")
public final class MetaEditFeedbackAction extends LoggedElveosAction {

    private static final String FEEDBACK_DESCRIPTION = "feedback_description";

    @RequestParam(name = FEEDBACK_DESCRIPTION, role = Role.POST)
    @MaxConstraint(max = 800, message = @tr("The title must be %constraint% chars length max."))
    @MinConstraint(min = 10, message = @tr("The title must have at least %constraint% chars."))
    @NonOptional(@tr("Error you forgot to write a title"))
    private final String description;

    @RequestParam
    private final String feedbackId;

    private final MetaEditFeedbackActionUrl url;

    public MetaEditFeedbackAction(final MetaEditFeedbackActionUrl url) {
        super(url);
        this.url = url;
        this.description = url.getDescription();
        this.feedbackId = url.getFeedbackId();
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        try {
            MetaFeedbackManager.getById(feedbackId).update(description);
            session.notifyGood("Feedback updated.");
        } catch (final IOException e) {
            session.notifyError("A problem occur during the feedback update process! Please report this bug! :)");
            return doProcessErrors();
        }
        return new MetaFeedbackListPageUrl();
    }

    @Override
    protected Url doProcessErrors() {
        session.addParameter(url.getDescriptionParameter());
        return session.getLastVisitedPage();
    }

    @Override
    protected void transmitParameters() {
        // nothing
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You have to be logged in to edit a metabug.");
    }
}
