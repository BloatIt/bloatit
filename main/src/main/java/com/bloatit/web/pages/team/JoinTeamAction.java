package com.bloatit.web.pages.team;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * A class used to join a public team.
 * </p>
 */
@ParamContainer("team/dojoin")
public class JoinTeamAction extends LoggedAction {
    @SuppressWarnings("unused")
    private JoinTeamActionUrl url;

    @RequestParam(level = Level.ERROR)
    private Group targetTeam;

    public JoinTeamAction(JoinTeamActionUrl url) {
        super(url);
        this.targetTeam = url.getTargetTeam();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        Member me = session.getAuthToken().getMember();

        if (targetTeam.isPublic()) {
            try {
                me.addToPublicGroup(targetTeam);
            } catch (UnauthorizedOperationException e) {
                Log.web().fatal("User trie to join public group, but is not allowed to",e);
                session.notifyBad("Oops we had an internal issue preventing you to join group, please try again later.");
                return session.getLastVisitedPage();
            }
        } else {
            try {
                session.notifyBad("The team " + targetTeam.getLogin() + " is not public, you need an invitation to join it");
            } catch (UnauthorizedOperationException e) {
                Log.web().warn("Trying to display team name but not allowed to",e);
            }
            throw new RedirectException(session.getLastVisitedPage());
        }
        return new TeamPageUrl(targetTeam);
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        if (targetTeam != null) {
            return new TeamPageUrl(targetTeam);
        }
        return session.getLastVisitedPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged before you try to join a team.");
    }

    @Override
    protected void transmitParameters() {
        // Nothing
    }
}
