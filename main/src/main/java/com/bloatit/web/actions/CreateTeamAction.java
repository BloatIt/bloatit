package com.bloatit.web.actions;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.url.CreateTeamActionUrl;

@ParamContainer("team/docreate")
public class CreateTeamAction extends LoggedAction{
    private CreateTeamActionUrl url;
    
    public CreateTeamAction(CreateTeamActionUrl url){
        super(url);
        this.url = url;
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        throw new NotImplementedException();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        throw new NotImplementedException();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to create a new team");
    }

    @Override
    protected void transmitParameters() {
        
    }
}
