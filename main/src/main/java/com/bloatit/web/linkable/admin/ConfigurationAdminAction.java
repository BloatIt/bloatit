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
package com.bloatit.web.linkable.admin;

import java.util.List;

import com.bloatit.common.ReloadableConfiguration;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.url.ConfigurationAdminActionUrl;
import com.bloatit.web.url.ConfigurationAdminPageUrl;

@ParamContainer("admin/doConfigure")
public class ConfigurationAdminAction extends AdminAction {
    private ConfigurationAdminActionUrl url;

    @RequestParam(role = Role.POST)
    private List<String> toReload;

    public ConfigurationAdminAction(ConfigurationAdminActionUrl url) {
        super(url);
        this.url = url;
        this.toReload = url.getToReload();
    }

    @Override
    protected Url doProcessAdmin() {
        for (String conf : toReload) {
            for (ReloadableConfiguration availableConf : ReloadableConfiguration.getConfigurations()) {
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
        // TODO
    }
}
