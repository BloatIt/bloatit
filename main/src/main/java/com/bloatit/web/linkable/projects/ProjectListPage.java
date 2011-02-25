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
package com.bloatit.web.linkable.projects;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlListItem;
import com.bloatit.framework.webserver.components.HtmlRenderer;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Project;
import com.bloatit.model.managers.ProjectManager;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.AddProjectPageUrl;
import com.bloatit.web.url.ProjectListPageUrl;
import com.bloatit.web.url.ProjectPageUrl;

@ParamContainer("project/list")
public final class ProjectListPage extends MasterPage {

    // Keep me here ! I am needed for the Url generation !
    private HtmlPagedList<Project> pagedProjectList;
    private final ProjectListPageUrl url;

    public ProjectListPage(final ProjectListPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {

        final HtmlDiv box = new HtmlDiv("padding_box");

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock("Project list", 1);
        final PageIterable<Project> projectList = ProjectManager.getAll();
        final HtmlRenderer<Project> projectItemRenderer = new HtmlRenderer<Project>() {
            @Override
            public XmlNode generate(final Project project) {
                final ProjectPageUrl memberUrl = new ProjectPageUrl(project);
                try {
                    HtmlLink htmlLink;
                    htmlLink = memberUrl.getHtmlLink(project.getName());

                    return new HtmlListItem(htmlLink);
                } catch (final UnauthorizedOperationException e) {
                    Log.web().warn(e);
                }
                return new PlaceHolderElement();
            }
        };

        // TODO: avoid conflict
        final ProjectListPageUrl clonedUrl = url.clone();
        pagedProjectList = new HtmlPagedList<Project>(projectItemRenderer, projectList, clonedUrl, clonedUrl.getPagedProjectListUrl());

        pageTitle.add(new AddProjectPageUrl().getHtmlLink(tr("Add a software")));
        pageTitle.add(pagedProjectList);

        box.add(pageTitle);
        add(box);

    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Project list");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
