package com.bloatit.web.pages.team;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.TeamPageUrl;

@ParamContainer("team/dojoin")
public class JoinTeamAction extends LoggedAction {
    @SuppressWarnings("unused")
    private JoinTeamActionUrl url;

    @RequestParam(level = Level.ERROR)
    private Group targetTeam;

    @RequestParam(defaultValue = "")
    private String justification;

    public JoinTeamAction(JoinTeamActionUrl url) {
        super(url);
        this.targetTeam = url.getTargetTeam();
        // this.justification = url.ge
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        targetTeam.authenticate(session.getAuthToken());
        Member me = session.getAuthToken().getMember();
        me.authenticate(session.getAuthToken());

        try {
            if (targetTeam.isPublic()) {
                me.addToPublicGroup(targetTeam);
            } else {
                // me.
            }
            return new TeamPageUrl(targetTeam);
        } catch (UnauthorizedOperationException e) {
            // TODO Auto-generated catch block
            throw new NotImplementedException();
        }
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        throw new NotImplementedException();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged before you try to join a team.");
    }

    @Override
    protected void transmitParameters() {

    }
}
