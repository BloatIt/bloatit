/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;

import java.util.Locale;

import com.bloatit.data.DaoBug.Level;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Batch;
import com.bloatit.model.Bug;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.web.url.AddProjectPageUrl;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.ReportBugActionUrl;

/**
 * A response to a form used to create a new idea
 */
@ParamContainer("demand/bug/doreport")
public final class ReportBugAction extends Action {

    public static final String BUG_TITLE = "bug_title";
    public static final String BUG_DESCRIPTION = "bug_description";
    public static final String BUG_LEVEL = "bug_level";
    public static final String BUG_BATCH = "bug_batch";
    public static final String LANGUAGE_CODE = "bug_description_language";

    @RequestParam(name = BUG_TITLE, role = Role.POST)
    @ParamConstraint(max = "120",
                     maxErrorMsg = @tr("The short description must be 120 chars length max."), //
                     min = "10", minErrorMsg = @tr("The short description must have at least 10 chars."),
                     optionalErrorMsg = @tr("You forgot to write a short description"))
    private final String title;

    @RequestParam(name = BUG_DESCRIPTION, role = Role.POST)
    @ParamConstraint(optional = true)
    private final String description;

    @RequestParam(name = LANGUAGE_CODE, role = Role.POST)
    private final String lang;
    private final ReportBugActionUrl url;

    @RequestParam(name = BUG_LEVEL, role = Role.POST)
    private final Level level;

    @RequestParam(name = BUG_BATCH, role = Role.POST)
    private final Batch batch;

    public ReportBugAction(final ReportBugActionUrl url) {
        super(url);
        this.url = url;

        this.title = url.getTitle();
        this.description = url.getDescription();
        this.lang = url.getLang();
        this.level = url.getLevel();
        this.batch = url.getBatch();

    }

    @Override
    protected Url doProcess() throws RedirectException {
        session.notifyList(url.getMessages());
        if (!DemandManager.canCreate(session.getAuthToken())) {
            // TODO: use BugManager and not DemandManager here
            session.notifyError(Context.tr("You must be logged in to report a bug."));
            return new LoginPageUrl();
        }
        final Locale langLocale = new Locale(lang);


        final Bug bug = batch.addBug(session.getAuthToken().getMember(), title, description, langLocale, level);

        bug.authenticate(session.getAuthToken());

        final BugPageUrl to = new BugPageUrl(bug);

        return to;
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyList(url.getMessages());

        session.addParameter(url.getTitleParameter());
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getBatchParameter());
        session.addParameter(url.getLevelParameter());
        session.addParameter(url.getLangParameter());

        return new AddProjectPageUrl();
    }
}