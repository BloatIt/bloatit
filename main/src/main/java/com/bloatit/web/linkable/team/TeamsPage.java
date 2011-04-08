package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.model.Team;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.TeamListRenderer;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.meta.bugreport.SideBarBugReportBlock;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
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
    private HtmlPagedList<Team> pagedTeamList;
    private final TeamsPageUrl url;

    public TeamsPage(final TeamsPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateMain());
        layout.addRight(new SideBarDocumentationBlock("describe_team"));
        

        add(layout);
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
    protected String getPageTitle() {
        return Context.tr("Team listing page");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return TeamsPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new TeamsPageUrl().getHtmlLink(tr("Teams")));

        return breadcrumb;
    }

}
