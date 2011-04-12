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
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AdminHomePageUrl;
import com.bloatit.web.url.ConfigurationAdminPageUrl;

@ParamContainer("admin/home")
public class AdminHomePage extends AdminPage {
    private final AdminHomePageUrl url;

    public AdminHomePage(final AdminHomePageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateAdminHome());
        // layout.addRight(new SideBarDocumentationBlock("markdown"));
        return layout;
    }

    private HtmlElement generateAdminHome() {
        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Administration home page"), 1);
        master.add(new ConfigurationAdminPageUrl().getHtmlLink("Manage configuration files"));
        return master;
    }

    @Override
    public void processErrors() throws RedirectException {
        // TODO do something here.
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Administration home page");
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        final Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.pushLink(new PageNotFoundUrl().getHtmlLink("Admin"));

        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return true;
    }

}
