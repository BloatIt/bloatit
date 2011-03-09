package com.bloatit.web.linkable.team;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlRenderer;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Group;
import com.bloatit.model.managers.GroupManager;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.TeamListRenderer;
import com.bloatit.web.pages.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.CreateTeamPageUrl;
import com.bloatit.web.url.TeamsPageUrl;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("team/list")
public class TeamsPage extends MasterPage {
    // Keep me here ! I am needed for the Url generation !
    private HtmlPagedList<Group> pagedTeamList;
    private final TeamsPageUrl url;

    public TeamsPage(final TeamsPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true);
        layout.addLeft(generateMain());
        layout.addRight(new SideBarDocumentationBlock("describe_team"));

        add(layout);
    }

    private HtmlElement generateMain() {
        final HtmlDiv master = new HtmlDiv();
        master.add(new HtmlLink(new CreateTeamPageUrl().urlString(), Context.tr("Create a new team")));

        final PageIterable<Group> teamList = GroupManager.getAll();
        final HtmlRenderer<Group> teamRenderer = new TeamListRenderer();

        final TeamsPageUrl clonedUrl = url.clone();
        pagedTeamList = new HtmlPagedList<Group>(teamRenderer, teamList, clonedUrl, clonedUrl.getPagedTeamListUrl());

        master.add(pagedTeamList);

        return master;
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Team listing page");
    }

    @Override
    public boolean isStable() {
        return true;
    }

}
