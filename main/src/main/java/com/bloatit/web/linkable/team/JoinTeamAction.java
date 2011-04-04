package com.bloatit.web.linkable.team;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.specific.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
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

    @RequestParam()
    private final Team targetTeam;

    public JoinTeamAction(final JoinTeamActionUrl url) {
        super(url);
        this.targetTeam = url.getTargetTeam();
    }

    @Override
    public Url doProcessRestricted(Member authenticatedMember) {
        final Member me = session.getAuthToken().getMember();

        if (targetTeam.isPublic()) {
            try {
                me.addToPublicTeam(targetTeam);
            } catch (final UnauthorizedOperationException e) {
                Log.web().fatal("User tries to join public team, but is not allowed to", e);
                session.notifyBad("Oops we had an internal issue preventing you to join team, please try again later.");
                return session.getLastVisitedPage();
            }
        } else {
            try {
                session.notifyBad("The team " + targetTeam.getLogin() + " is not public, you need an invitation to join it");
            } catch (final UnauthorizedOperationException e) {
                Log.web().warn("Trying to display team name but not allowed to", e);
            }
            return session.getLastVisitedPage();
        }
        return new TeamPageUrl(targetTeam);
    }

    @Override
    protected Url doProcessErrors() {
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
