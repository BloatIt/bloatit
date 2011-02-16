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
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.HandleJoinGroupInvitationActionUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * Action used to accept or refuse group invitations
 * </p>
 */
@ParamContainer("group/dojoin")
public class HandleJoinGroupInvitationAction extends LoggedAction {
    @RequestParam(level = Level.ERROR)
    private JoinGroupInvitation invite;

    @RequestParam(level = Level.ERROR)
    private Boolean accept;

    private final HandleJoinGroupInvitationActionUrl url;

    public HandleJoinGroupInvitationAction(HandleJoinGroupInvitationActionUrl url) {
        super(url);
        this.url = url;
        this.accept = url.getAccept();
        this.invite = url.getInvite();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        invite.authenticate(session.getAuthToken());
        if (accept) {
            Group g = invite.getGroup();
            g.authenticate(session.getAuthToken());
            Member me = session.getAuthToken().getMember();
            me.authenticate(session.getAuthToken());
            
            if(me.isInGroup(g)){
                session.notifyError(Context.tr("You cannot join a group you already belong in"));
                throw new RedirectException(session.getLastVisitedPage());
            }
            
            invite.accept();
            try {
                session.notifyGood(Context.tr("You are now a member of group ''" + g.getLogin() + "''"));
            } catch (UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Couldn't display a group name, while user should be part of it", e);
            }
            return new TeamPageUrl(invite.getGroup());
        }
        invite.refuse();
        return session.getLastVisitedPage();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyList(url.getMessages());
        return session.getLastVisitedPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged before you accept (or refuse) a group invite");
    }

    @Override
    protected void transmitParameters() {
        // Nothing to transmit
    }
}
