package com.bloatit.web.pages.team;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.model.right.RightManager.Action;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("team")
public class TeamPage extends MasterPage {
    private TeamPageUrl url;

    @RequestParam(level = Level.ERROR)
    private Group targetTeam;

    public TeamPage(TeamPageUrl url) {
        super(url);
        this.url = url;
        this.targetTeam = url.getTargetTeam();
    }

    @Override
    protected void doCreate() throws RedirectException {
        HtmlDiv master = new HtmlDiv("padding_box");
        add(master);

        targetTeam.authenticate(session.getAuthToken());

        try {
            HtmlTitleBlock title = new HtmlTitleBlock(targetTeam.getLogin(), 1);
            master.add(title);

            if (targetTeam.isPublic()) {
                title.addText("Join group");
            } else {
                title.addText("Send a request to join group");
            }

            if (targetTeam.canAccessEmail(Action.READ)) {
                title.addText("email : " + targetTeam.getEmail());
            }

            PageIterable<Member> members = targetTeam.getMembers();
            for (Member m : members) {
                title.addText(m.getDisplayName());
            }

        } catch (UnauthorizedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Teams management page");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
