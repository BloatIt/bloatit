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

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.UserContentInterface;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.url.AddAttachementActionUrl;
import com.bloatit.web.url.LoginPageUrl;

/**
 * A response to a form used to create a new idea
 */
@ParamContainer("usercontent/attachfile")
public final class AddAttachementAction extends Action {

    public static final String USER_CONTENT = "user_content";
    public static final String ATTACHEMENT_CODE = "attachement";
    public static final String ATTACHEMENT_NAME_CODE = "attachement/filename";
    public static final String ATTACHEMENT_CONTENT_TYPE_CODE = "attachement/contenttype";
    public static final String ATTACHEMENT_DESCRIPTION_CODE = "attachement_description";

    @ParamConstraint(optionalErrorMsg = @tr("An attachement must be linked to a content"))
    @RequestParam(name = USER_CONTENT)
    private final UserContentInterface userContent;

    @ParamConstraint
    @RequestParam(name = ATTACHEMENT_CODE, role = Role.POST)
    private final String attachement;

    @ParamConstraint
    @RequestParam(name = ATTACHEMENT_NAME_CODE, role = Role.POST)
    @Optional
    private final String attachementFileName;

    @ParamConstraint
    @RequestParam(name = ATTACHEMENT_DESCRIPTION_CODE, role = Role.POST)
    private final String attachementDescription;

    @Optional
    @ParamConstraint
    @RequestParam(name = ATTACHEMENT_CONTENT_TYPE_CODE, role = Role.POST)
    private final String attachementContentType;
    private final AddAttachementActionUrl url;

    public AddAttachementAction(final AddAttachementActionUrl url) {
        super(url);
        this.url = url;

        this.userContent = url.getUserContent();
        this.attachement = url.getAttachement();
        this.attachementFileName = url.getAttachementFileName();
        this.attachementContentType = url.getAttachementContentType();
        this.attachementDescription = url.getAttachementDescription();

    }

    @Override
    protected Url doProcess() {
        session.notifyList(url.getMessages());
        if (!DemandManager.canCreate(session.getAuthToken())) {
            // TODO: use UserContentManager and not DemandManager here
            session.notifyError(Context.tr("You must be logged in to report a bug."));
            return new LoginPageUrl();
        }

        final FileMetadata attachementFileMedatata = FileMetadataManager.createFromTempFile(session.getAuthToken().getMember(),
                                                                                            attachement,
                                                                                            attachementFileName,
                                                                                            attachementDescription);

        userContent.addFile(attachementFileMedatata);

        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());

        if (userContent != null) {
            return redirectWithError();
        }
        return Context.getSession().getLastVisitedPage();
    }

    public Url redirectWithError() {
        session.addParameter(url.getUserContentParameter());
        session.addParameter(url.getAttachementDescriptionParameter());
        return Context.getSession().getLastVisitedPage();
    }

}
