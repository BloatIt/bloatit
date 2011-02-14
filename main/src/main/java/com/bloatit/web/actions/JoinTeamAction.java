package com.bloatit.web.actions;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.url.Url;

@ParamContainer("team/dojoin")
public class JoinTeamAction extends LoggedAction{
    private JoinTeamActionUrl url;

    public JoinTeamAction(JoinTeamActionUrl url) {
        super(url);
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        return null;
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        return null;
    }

    @Override
    protected String getRefusalReason() {
        return null;
    }

    @Override
    protected void transmitParameters() {
        
    }
}
