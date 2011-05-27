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

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AdminHomePageUrl;
import com.bloatit.web.url.ConfigurationAdminPageUrl;
import com.bloatit.web.url.ExceptionAdministrationPageUrl;
import com.bloatit.web.url.FeatureAdminPageUrl;
import com.bloatit.web.url.HightlightedFeatureAdminPageUrl;
import com.bloatit.web.url.KudosableAdminPageUrl;
import com.bloatit.web.url.MilestoneAdminPageUrl;
import com.bloatit.web.url.MoneyWithdrawalAdminPageUrl;
import com.bloatit.web.url.UserContentAdminPageUrl;

@ParamContainer("admin")
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
        return layout;
    }

    private HtmlElement generateAdminHome() {
        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Administration home page"), 1);
        final HtmlList list = new HtmlList();
        master.add(list);
        list.add(new ConfigurationAdminPageUrl().getHtmlLink("Manage configuration files"));
        list.add(new MilestoneAdminPageUrl().getHtmlLink("Milestones"));
        list.add(new UserContentAdminPageUrl().getHtmlLink("User content"));
        list.add(new KudosableAdminPageUrl().getHtmlLink("Kudosables"));
        list.add(new FeatureAdminPageUrl().getHtmlLink("Feature"));
        list.add(new HightlightedFeatureAdminPageUrl().getHtmlLink("Hightlighted Features"));
        list.add(new MoneyWithdrawalAdminPageUrl().getHtmlLink("Manage money withdrawals"));
        list.add(new ExceptionAdministrationPageUrl().getHtmlLink("Manage exceptions"));
        return master;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Administration home page");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.pushLink(new PageNotFoundUrl().getHtmlLink("Admin"));

        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
