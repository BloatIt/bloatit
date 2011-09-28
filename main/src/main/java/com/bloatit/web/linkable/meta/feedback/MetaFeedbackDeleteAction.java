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

import com.bloatit.framework.meta.MetaFeedback;
import com.bloatit.framework.meta.MetaFeedbackManager;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.MetaFeedbackListPageUrl;
import com.bloatit.web.url.MetaFeedbackDeleteActionUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("meta/feedback/dodelete")
public final class MetaFeedbackDeleteAction extends LoggedElveosAction {

    @RequestParam
    private final String feedbackId;

    // Keep it for consistency.
    @SuppressWarnings("unused")
    private final MetaFeedbackDeleteActionUrl url;

    public MetaFeedbackDeleteAction(final MetaFeedbackDeleteActionUrl url) {
        super(url);
        this.url = url;
        this.feedbackId = url.getFeedbackId();
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        final MetaFeedback bug = MetaFeedbackManager.getById(feedbackId);
        if (bug != null) {
            bug.delete();
            session.notifyGood("Feedback deleted.");
            return new MetaFeedbackListPageUrl();
        }
        session.notifyError(Context.tr("Feedback id ''{0}'' doesn't exist. Maybe it has already been deleted.", feedbackId));
        return new MetaFeedbackListPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You have to be logged in to delete a metaBug.");
    }

    @Override
    protected Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    protected void transmitParameters() {
        // Nothing to transmit. only get parameters.
    }

}
