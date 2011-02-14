package com.bloatit.web.pages.team;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Group;
import com.bloatit.web.pages.LoggedPage;

@ParamContainer("team/join")
public class JoinTeamPage extends LoggedPage {

    private JoinTeamPageUrl url;

    @RequestParam(level = Level.ERROR)
    private Group targetTeam;

    public JoinTeamPage(JoinTeamPageUrl url) {
        super(url);
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        return null;
    }

    @Override
    public String getRefusalReason() {
        return null;
    }

    @Override
    protected String getPageTitle() {
        return null;
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
