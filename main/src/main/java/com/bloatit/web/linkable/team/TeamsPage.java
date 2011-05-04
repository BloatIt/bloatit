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
package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Team;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.components.TeamListRenderer;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateTeamPageUrl;
import com.bloatit.web.url.TeamsPageUrl;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("team/list")
public final class TeamsPage extends ElveosPage {
    // Keep me here ! I am needed for the Url generation !
    private HtmlPagedList<Team> pagedTeamList;
    private final TeamsPageUrl url;

    public TeamsPage(final TeamsPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent(ElveosUserToken authToken) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateMain());

        layout.addRight(new SideBarButton(Context.tr("Create a team"), new CreateTeamPageUrl(), WebConfiguration.getImgTeam()));
        layout.addRight(new SideBarDocumentationBlock("describe_team"));

        return layout;
    }

    private HtmlElement generateMain() {
        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Teams list"), 1);
        master.add(new HtmlLink(new CreateTeamPageUrl().urlString(), Context.tr("Create a new team")));

        final PageIterable<Team> teamList = TeamManager.getAll();
        final HtmlRenderer<Team> teamRenderer = new TeamListRenderer();

        final TeamsPageUrl clonedUrl = url.clone();
        pagedTeamList = new HtmlPagedList<Team>(teamRenderer, teamList, clonedUrl, clonedUrl.getPagedTeamListUrl());

        master.add(pagedTeamList);

        return master;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Team listing page");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb(ElveosUserToken authToken) {
        return TeamsPage.generateBreadcrumb();
    }

    protected static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new TeamsPageUrl().getHtmlLink(tr("Teams")));
        return breadcrumb;
    }

}
