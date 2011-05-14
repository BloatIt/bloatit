//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.model.Member;
import com.bloatit.model.UserContent;
import com.bloatit.model.admin.UserContentAdminListFactory;
import com.bloatit.model.right.AuthenticatedUserToken;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.UserContentAdminPageUrl;

public class UserContentAdminPageImplementation extends
        UserContentAdminPage<DaoUserContent, UserContent<DaoUserContent>, UserContentAdminListFactory.DefaultFactory> {

    private final UserContentAdminPageUrl url;

    public UserContentAdminPageImplementation(final UserContentAdminPageUrl url) {
        super(url, new UserContentAdminListFactory.DefaultFactory());
        this.url = url;
    }

    @Override
    protected String createPageTitle() {
        return tr("Administration UserContent");
    }

    @Override
    public final boolean isStable() {
        return true;
    }

    @Override
    protected void addColumns(final HtmlGenericTableModel<UserContent<DaoUserContent>> tableModel) {
        final UserContentAdminPageUrl clonedUrl = url.clone();

        addAsTeamColumn(tableModel, clonedUrl);
        addCreationDateColumn(tableModel, clonedUrl);
        addNbFilesColumn(tableModel);
        addIsDeletedColumn(tableModel, clonedUrl);
        addTypeColumn(tableModel);
    }

    @Override
    protected void addFormFilters(final HtmlBranch form) {
        addIsDeletedFilter(form, url);
        addHasFileFilter(form, url);
        addAsTeamFilter(form, url);
    }

    @Override
    protected Breadcrumb createBreadcrumb(Member member) {
        return UserContentAdminPageImplementation.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new UserContentAdminPageUrl().getHtmlLink(tr("User content administration")));

        return breadcrumb;
    }

}
