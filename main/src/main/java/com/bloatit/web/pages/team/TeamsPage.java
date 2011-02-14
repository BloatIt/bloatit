package com.bloatit.web.pages.team;

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
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.model.Group;
import com.bloatit.model.managers.GroupManager;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.CreateTeamPageUrl;
import com.bloatit.web.url.TeamPageUrl;
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
    private TeamsPageUrl url;

    public TeamsPage(TeamsPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        HtmlDiv master = new HtmlDiv("padding_box");
        add(master);
        master.add(new HtmlLink(new CreateTeamPageUrl().urlString(), Context.tr("Create a new team")));

        final PageIterable<Group> teamList = GroupManager.getGroups();
        final HtmlRenderer<Group> teamRenderer = new GroupListRenderer();

        final TeamsPageUrl clonedUrl = url.clone();
        pagedTeamList = new HtmlPagedList<Group>(teamRenderer, teamList, clonedUrl, clonedUrl.getPagedTeamListUrl());

        master.add(pagedTeamList);
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Teams management page");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private class GroupListRenderer implements HtmlRenderer<Group> {
        @Override
        public HtmlNode generate(Group team) {
            final TeamPageUrl teamUrl = new TeamPageUrl(team);
            try {
                HtmlLink htmlLink;
                htmlLink = teamUrl.getHtmlLink(team.getLogin());

                return new HtmlListItem(htmlLink);
            } catch (final UnauthorizedOperationException e) {
                Log.web().warn(e);
            }
            return new PlaceHolderElement();
        }
    }
}
