/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.admin.configuration;

import java.util.List;

import com.bloatit.common.ReloadableConfiguration;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.admin.master.AdminAction;
import com.bloatit.web.url.ConfigurationAdminActionUrl;
import com.bloatit.web.url.ConfigurationAdminPageUrl;

@ParamContainer("admin/doconfigure")
public class ConfigurationAdminAction extends AdminAction {
    @RequestParam(name = "toreload", role = Role.POST)
    private final List<String> toReload;

    @SuppressWarnings("unchecked")
    public ConfigurationAdminAction(final ConfigurationAdminActionUrl url) {
        super(url);
        this.toReload = url.getToReload();
    }

    @Override
    protected Url doProcessAdmin() {
        for (final String conf : toReload) {
            for (final ReloadableConfiguration availableConf : ReloadableConfiguration.getConfigurations()) {
                if (availableConf.getName().equals(conf)) {
                    availableConf.reload();
                    break;
                }
            }
        }
        return new ConfigurationAdminPageUrl();
    }

    @Override
    protected Url doProcessErrors() {
        return null;
    }

    @Override
    protected void transmitParameters() {
        // Nothing to do
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }
}
