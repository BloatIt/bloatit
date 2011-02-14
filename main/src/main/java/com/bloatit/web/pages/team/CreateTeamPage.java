package com.bloatit.web.pages.team;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.CreateTeamPageUrl;

@ParamContainer("team/create")
public class CreateTeamPage extends MasterPage{
    private CreateTeamPageUrl url;
    
    public CreateTeamPage(CreateTeamPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Create a new team");
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
