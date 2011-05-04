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

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.UserContentInterface;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.AddAttachementActionUrl;
import com.bloatit.web.url.AddAttachementPageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("usercontent/doattachfile")
public final class AddAttachementAction extends UserContentAction {

    @ParamConstraint(optionalErrorMsg = @tr("An attachment must be linked to a content"))
    @RequestParam
    private final UserContentInterface userContent;

    private final AddAttachementActionUrl url;

    public AddAttachementAction(final AddAttachementActionUrl url) {
        super(url, UserTeamRight.TALK);
        this.url = url;
        this.userContent = url.getUserContent();
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {
        propagateAttachedFileIfPossible(userContent);
        return session.pickPreferredPage();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected boolean verifyFile(final String filename) {
        // TODO make some generic check (file size ...)
        return true;
    }

    @Override
    protected Url doProcessErrors(ElveosUserToken userToken) {
        return Context.getSession().getLastVisitedPage();
    }

    @Override
    protected String getRefusalReason() {
        return "You must be logged in to add an attachment.";
    }

    @Override
    protected void doTransmitParameters() {
        // TODO make sure all the parameters are transmitted.
        session.addParameter(url.getUserContentParameter());
        session.addParameter(url.getAttachmentDescriptionParameter());
    }

}
