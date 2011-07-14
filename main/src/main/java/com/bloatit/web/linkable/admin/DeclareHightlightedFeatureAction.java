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
package com.bloatit.web.linkable.admin;

import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.admin.master.AdminAction;
import com.bloatit.web.url.DeclareHightlightedFeatureActionUrl;
import com.bloatit.web.url.HightlightedFeatureAdminPageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("admin/dodeclarehightlightedfeature")
public final class DeclareHightlightedFeatureAction extends AdminAction {
    @RequestParam(role = Role.POST)
    @MaxConstraint(max = 80, message = @tr("The title must be 80 chars length max."))
    @NonOptional(@tr("You forgot to write a title"))
    private final String title;

    @RequestParam(role = Role.POST)
    private final Integer position;

    @RequestParam(role = Role.POST)
    private final DateLocale activationDate;

    @RequestParam(role = Role.POST)
    private final DateLocale desactivationDate;

    @RequestParam(role = Role.POST)
    private final Feature feature;

    private final DeclareHightlightedFeatureActionUrl url;

    public DeclareHightlightedFeatureAction(final DeclareHightlightedFeatureActionUrl url) {
        super(url);
        this.url = url;

        this.title = url.getTitle();
        this.position = url.getPosition();
        this.activationDate = url.getActivationDate();
        this.desactivationDate = url.getDesactivationDate();
        this.feature = url.getFeature();
    }

    @Override
    protected Url doProcessErrors() {
        return new HightlightedFeatureAdminPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessAdmin() {
        new HighlightFeature(feature, position, title, activationDate.getJavaDate(), desactivationDate.getJavaDate());
        return new HightlightedFeatureAdminPageUrl();
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getActivationDateParameter());
        session.addParameter(url.getDesactivationDateParameter());
        session.addParameter(url.getFeatureParameter());
        session.addParameter(url.getPositionParameter());
        session.addParameter(url.getTitleParameter());

    }

}
