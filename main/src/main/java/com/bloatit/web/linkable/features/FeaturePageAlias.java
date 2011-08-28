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
package com.bloatit.web.linkable.features;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.model.Feature;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.pages.master.AliasAction;
import com.bloatit.web.url.FeaturePageAliasUrl;
import com.bloatit.web.url.FeaturePageUrl;

@ParamContainer("f/%feature%")
public final class FeaturePageAlias extends AliasAction {

    @SuppressWarnings("unused")
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the feature number: ''%value%''."))
    @NonOptional(@tr("You have to specify a feature number."))
    private Feature feature;

    public FeaturePageAlias(final FeaturePageAliasUrl url) {
        super(url, new FeaturePageUrl(url.getFeature(), FeatureTabKey.description, true));
    }

}
