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
package com.bloatit.web.linkable.meta.bugreport;

import com.bloatit.framework.meta.MetaBug;
import com.bloatit.framework.meta.MetaBugManager;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.MetaBugDeleteActionUrl;
import com.bloatit.web.url.MetaBugsListPageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("meta/bug/dodelete")
public final class MetaBugDeleteAction extends LoggedElveosAction {

    @RequestParam
    private final String bugId;

    // Keep it for consistency.
    @SuppressWarnings("unused")
    private final MetaBugDeleteActionUrl url;

    public MetaBugDeleteAction(final MetaBugDeleteActionUrl url) {
        super(url);
        this.url = url;
        this.bugId = url.getBugId();
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        final MetaBug bug = MetaBugManager.getById(bugId);
        if (bug != null) {
            bug.delete();
            session.notifyGood("Bug deleted.");
            return new MetaBugsListPageUrl();
        }
        session.notifyError(Context.tr("Bug id ''{0}'' doesn't exist. Maybe it has already been deleted.", bugId));
        return new MetaBugsListPageUrl();
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
