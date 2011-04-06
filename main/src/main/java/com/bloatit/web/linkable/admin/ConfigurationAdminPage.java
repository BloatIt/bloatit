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

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.ConfigurationAdminPageUrl;

@ParamContainer("admin/configuration")
public class ConfigurationAdminPage extends AdminPage {
    private final ConfigurationAdminPageUrl url;
    
    /**
     * @param url
     */
    public ConfigurationAdminPage(ConfigurationAdminPageUrl url) {
        super(url);
        this.url = url;
    }

    /* (non-Javadoc)
     * @see com.bloatit.web.linkable.admin.AdminPage#createAdminContent()
     */
    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.bloatit.web.pages.LoggedPage#processErrors()
     */
    @Override
    public void processErrors() throws RedirectException {
        
    }

    /* (non-Javadoc)
     * @see com.bloatit.web.pages.master.MasterPage#getPageTitle()
     */
    @Override
    protected String getPageTitle() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.bloatit.web.pages.master.MasterPage#getBreadcrumb()
     */
    @Override
    protected Breadcrumb getBreadcrumb() {
        Breadcrumb breadcrumb = new Breadcrumb();

        // TODO Add admin hope page
        breadcrumb.pushLink(new PageNotFoundUrl().getHtmlLink("Admin"));
        breadcrumb.pushLink(url.getHtmlLink("Configuration"));

        return breadcrumb;
    }

    /* (non-Javadoc)
     * @see com.bloatit.framework.webserver.masters.GenericPage#isStable()
     */
    @Override
    public boolean isStable() {
        return false;
    }
}
