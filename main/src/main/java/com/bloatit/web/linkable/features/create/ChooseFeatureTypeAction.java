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
package com.bloatit.web.linkable.features.create;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedElveosAction;
import com.bloatit.web.url.ChooseFeatureTypeActionUrl;
import com.bloatit.web.url.ChooseFeatureTypePageUrl;
import com.bloatit.web.url.IndexPageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("feature/create/%process%/dochoosetype")
public final class ChooseFeatureTypeAction extends LoggedElveosAction {
    
    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You forgot to write a title"))
    private final CreateFeatureProcess.FeatureCreationType type;

    
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    @RequestParam(role = Role.PAGENAME)
    CreateFeatureProcess process;

    private final ChooseFeatureTypeActionUrl url;

    public ChooseFeatureTypeAction(final ChooseFeatureTypeActionUrl url) {
        super(url);
        this.url = url;

        this.process = url.getProcess();
        this.type = url.getType();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    
    @Override
    protected Url doProcessErrors() {
        if(process == null) {
            return new IndexPageUrl();
        }
        return new ChooseFeatureTypePageUrl(process);
    }

    @Override
    protected String getRefusalReason() {
        return "You have to be logged to create a new feature request.";
    }

    @Override
    protected Url doProcessRestricted(Member me) {
        process.setType(type);
        return process.getCreationPage();
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getProcessParameter());
        session.addParameter(url.getTypeParameter());
    }

    
}
