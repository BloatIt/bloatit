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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.meta.MetaBugManager;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.MetaReportBugActionUrl;

/**
 * An action used to create a bug
 */
@ParamContainer("meta/bugreport/doreport")
public final class MetaReportBugAction extends Action {

    public static final String BUG_DESCRIPTION = "bug_description";
    public static final String BUG_URL = "bug_url";

    @RequestParam(name = BUG_DESCRIPTION, role = Role.POST)
    @ParamConstraint(max = "800", maxErrorMsg = @tr("The title must be 800 chars length max."), //
    min = "1", minErrorMsg = @tr("The title must have at least 10 chars."), //
    optionalErrorMsg = @tr("Error you forgot to write a title"))
    private final String description;

    @RequestParam(name = BUG_URL, role = Role.POST)
    private final String bugUrl;

    private final MetaReportBugActionUrl url;

    public MetaReportBugAction(final MetaReportBugActionUrl url) {
        super(url);
        this.url = url;
        this.description = url.getDescription();
        this.bugUrl = url.getBugUrl();
    }

    @Override
    protected Url doProcess() {
        // TODO: define if you have to be logged to report a bug
        String bugReport = "";
        bugReport += "* **Url:** " + bugUrl + "\n";
        try {
            bugReport += "* **Author:** " + (session.isLogged() ? session.getAuthToken().getMember().getDisplayName() : "not logged") + "\n";
        } catch (final UnauthorizedOperationException e) {
            session.notifyError(Context.tr("An error prevented us from displaying user information. Please notify us."));
            throw new ShallNotPassException("User cannot access user information", e);
        }
        bugReport += "* **Date:** " + new SimpleDateFormat().format(new Date()) + "\n";
        bugReport += "\n";
        bugReport += description;

        if (MetaBugManager.reportBug(bugReport)) {
            session.notifyGood("Bug reported, Thanks!");
        } else {
            session.notifyError("A problem occur during the bug report process! Please report this bug! :)");
        }
        
        // TODO: add link system in documentation
        return session.getLastVisitedPage();
    }

    @Override
    protected Url doProcessErrors() {
        session.addParameter(url.getDescriptionParameter());
        return session.getLastVisitedPage();
    }
}
