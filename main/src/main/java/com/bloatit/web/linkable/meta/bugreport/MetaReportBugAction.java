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

import com.bloatit.framework.meta.MetaBugManager;
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
import com.bloatit.web.url.MetaReportBugActionUrl;

/**
 * An action used to create a bug
 */
@ParamContainer("meta/bugreport/doreport")
public final class MetaReportBugAction extends ElveosAction {

    private static final String BUG_DESCRIPTION = "bug_description";
    protected static final String BUG_URL = "bug_url";

    @RequestParam(name = BUG_DESCRIPTION, role = Role.POST)
    @MaxConstraint(max = 80000, message = @tr("The description must be %constraint% chars length max."))
    @MinConstraint(min = 10, message = @tr("The description must have at least %constraint% chars."))
    @NonOptional(@tr("Error you forgot to write a description"))
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
        String bugReport = "";
        bugReport += "* **Url:** " + bugUrl + "\n";
        bugReport += "* **Author:** " + (AuthToken.isAuthenticated() ? AuthToken.getMember().getLogin() : "not logged") + "\n";
        bugReport += "* **Date:** " + new SimpleDateFormat().format(new Date()) + "\n";
        bugReport += "\n";
        bugReport += description;

        if (MetaBugManager.reportBug(bugReport)) {
            session.notifyGood("Bug reported, Thanks!");
        } else {
            session.notifyError("A problem occur during the bug report process! Please report this bug! :)");
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
