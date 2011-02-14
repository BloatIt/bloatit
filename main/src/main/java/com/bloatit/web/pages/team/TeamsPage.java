package com.bloatit.web.pages.team;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.TeamsPageUrl;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("team/list")
public class TeamsPage extends MasterPage {
    private TeamsPageUrl url;

    public TeamsPage(TeamsPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        HtmlDiv master = new HtmlDiv("padding_box");
        add(master);
        
        
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
