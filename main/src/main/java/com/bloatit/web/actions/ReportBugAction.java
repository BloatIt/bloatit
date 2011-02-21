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
package com.bloatit.web.actions;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.Locale;

import com.bloatit.data.DaoBug.Level;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.components.form.Displayable;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Batch;
import com.bloatit.model.Bug;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.ReportBugActionUrl;
import com.bloatit.web.url.ReportBugPageUrl;

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
    public static final String ATTACHEMENT_CODE = "attachement";
    public static final String ATTACHEMENT_NAME_CODE = "attachement/filename";
    public static final String ATTACHEMENT_CONTENT_TYPE_CODE = "attachement/contenttype";
    public static final String ATTACHEMENT_DESCRIPTION_CODE = "attachement_description";

    @RequestParam(name = BUG_TITLE, role = Role.POST)
    @ParamConstraint(max = "120", maxErrorMsg = @tr("The short description must be 120 chars length max."), //
    min = "10", minErrorMsg = @tr("The short description must have at least 10 chars."), optionalErrorMsg = @tr("You forgot to write a short description"))
    private final String title;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a bug description"))
    @RequestParam(name = BUG_DESCRIPTION, role = Role.POST)
    private final String description;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a description language"))
    @RequestParam(name = LANGUAGE_CODE, role = Role.POST)
    private final String lang;
    private final ReportBugActionUrl url;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a bug level"))
    @RequestParam(name = BUG_LEVEL, defaultValue = "MINOR", role = Role.POST)
    private final BindedLevel level;

    @ParamConstraint(optionalErrorMsg = @tr("A new bug must be linked to a milestone"))
    @RequestParam(name = BUG_BATCH, role = Role.GET)
    private final Batch batch;

    @ParamConstraint(optional=true)
    @RequestParam(name = ATTACHEMENT_CODE, role = Role.POST)
    private final String attachement;

    @ParamConstraint(optional=true)
    @RequestParam(name = ATTACHEMENT_NAME_CODE, role = Role.POST)
    private final String attachementFileName;

    @ParamConstraint(optional=true)
    @RequestParam(name = ATTACHEMENT_DESCRIPTION_CODE, role = Role.POST)
    private final String attachementDescription;

    @ParamConstraint(optional=true)
    @RequestParam(name = ATTACHEMENT_CONTENT_TYPE_CODE, role = Role.POST)
    private final String attachementContentType;

    public ReportBugAction(final ReportBugActionUrl url) {
        super(url);
        this.url = url;

        this.title = url.getTitle();
        this.description = url.getDescription();
        this.lang = url.getLang();
        this.level = url.getLevel();
        this.batch = url.getBatch();
        this.attachement = url.getAttachement();
        this.attachementFileName = url.getAttachementFileName();
        this.attachementContentType = url.getAttachementContentType();
        this.attachementDescription = url.getAttachementDescription();

    }

    @Override
    protected Url doProcess() throws RedirectException {
        session.notifyList(url.getMessages());
        if (!DemandManager.canCreate(session.getAuthToken())) {
            // TODO: use BugManager and not DemandManager here
            session.notifyError(Context.tr("You must be logged in to report a bug."));
            return new LoginPageUrl();
        }

        if(attachement != null && (attachementDescription == null || attachementDescription.isEmpty())) {
            session.notifyError(Context.tr("You must enter a description of the attachement if you add an attachement."));
            return redirectWithError();
        }

        final Locale langLocale = new Locale(lang);

        final Bug bug = batch.addBug(session.getAuthToken().getMember(), title, description, langLocale, level.getLevel());

        final FileMetadata attachementFileMedatata = FileMetadataManager.createFromTempFile(session.getAuthToken().getMember(),
                                                                              attachement,
                                                                              attachementFileName,
                                                                              attachementDescription);

        bug.addFile(attachementFileMedatata);

        final BugPageUrl to = new BugPageUrl(bug);

        return to;
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyList(url.getMessages());

        if (batch != null) {
            return redirectWithError();
        } else {
            return new PageNotFoundUrl();
        }

    }

    public Url redirectWithError() {
        session.addParameter(url.getTitleParameter());
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getBatchParameter());
        session.addParameter(url.getLevelParameter());
        session.addParameter(url.getLangParameter());
        session.addParameter(url.getAttachementDescriptionParameter());
        return new ReportBugPageUrl(batch.getOffer());
    }

    public enum BindedLevel implements Displayable {
        FATAL(Level.FATAL, tr("FATAL")), MAJOR(Level.MAJOR, tr("MAJOR")), MINOR(Level.MINOR, tr("MINOR"));

        private final String label;
        private final Level level;

        private BindedLevel(Level level, String label) {
            this.level = level;
            this.label = label;
        }

        @Override
        public String getDisplayName() {
            return label;
        }

        public Level getLevel() {
            return level;
        }

    }

}
