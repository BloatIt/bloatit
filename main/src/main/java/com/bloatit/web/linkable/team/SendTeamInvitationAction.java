package com.bloatit.web.linkable.team;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.SendTeamInvitationActionUrl;

/**
 * <p>
 * An action used to send team invitations
 * </p>
 */
@ParamContainer("invitation/dosend")
public class SendTeamInvitationAction extends LoggedAction {
    public final static String TEAM_JOIN_CODE = "bloatit_join_team";
    public final static String RECEIVER_CODE = "bloatit_join_receiver";

    @RequestParam(name = TEAM_JOIN_CODE, role = Role.POST)
    private final Team team;

    @RequestParam(name = RECEIVER_CODE, role = Role.POST)
    private final Member receiver;

    private final SendTeamInvitationActionUrl url;

    public SendTeamInvitationAction(final SendTeamInvitationActionUrl url) {
        super(url);
        this.url = url;
        this.team = url.getTeam();
        this.receiver = url.getReceiver();
    }

    @Override
    public Url doProcessRestricted(Member authenticatedMember) {
        final Member me = session.getAuthToken().getMember();

        try {
            me.sendInvitation(receiver, team);
            session.notifyGood("Invitation sent to " + receiver.getDisplayName() + " for team " + team.getLogin());
        } catch (final UnauthorizedOperationException e) {
            e.printStackTrace();
        }
        return session.getLastVisitedPage();
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());
        return session.getLastVisitedPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to send an invitation");
    }

    @Override
    protected void transmitParameters() {
        throw new NotImplementedException();
    }
}
