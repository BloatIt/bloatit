package com.bloatit.web.linkable.team;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
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
public final class JoinTeamAction extends LoggedAction {
    @SuppressWarnings("unused")
    private JoinTeamActionUrl url;

    @RequestParam()
    private final Team targetTeam;

    public JoinTeamAction(final JoinTeamActionUrl url) {
        super(url);
        this.targetTeam = url.getTargetTeam();
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        if (targetTeam.isPublic()) {
            try {
                me.addToPublicTeam(targetTeam);
            } catch (final UnauthorizedOperationException e) {
                session.notifyBad(Context.tr("Oops we had an internal issue preventing you to join team. It's a bug, please notify us."));
                throw new ShallNotPassException("User tries to join public team, but is not allowed to", e);
            }
        } else {
            try {
                session.notifyBad(Context.tr("The team {0} is not public, you need an invitation to join it.", targetTeam.getLogin()));
            } catch (final UnauthorizedOperationException e) {
                session.notifyBad(Context.tr("Oops we had an internal issue preventing you to see a team name. It's a bug, please notify us."));
                throw new ShallNotPassException("Couldn't display team name", e);
            }
            return session.getLastVisitedPage();
        }
        return new TeamPageUrl(targetTeam);
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member me) {
        return NO_ERROR;
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
