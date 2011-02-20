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
    private final JoinGroupInvitation invite;

    @RequestParam(level = Level.ERROR)
    private final Boolean accept;

    private final HandleJoinGroupInvitationActionUrl url;

    public HandleJoinGroupInvitationAction(final HandleJoinGroupInvitationActionUrl url) {
        super(url);
        this.url = url;
        this.accept = url.getAccept();
        this.invite = url.getInvite();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        final Member me = session.getAuthToken().getMember();
        if (accept) {
            final Group g = invite.getGroup();

            if (me.isInGroup(g)) {
                session.notifyError(Context.tr("You cannot join a group you already belong in"));
                throw new RedirectException(session.getLastVisitedPage());
            }

            try {
                me.acceptInvitation(invite);
            } catch (final UnauthorizedOperationException e1) {
                // Should never happen
                Log.web().fatal("User accepted a legitimate group invitation, but it failed", e1);
                session.notifyBad(Context.tr("Oops looks like we had an issue with accepting this group invitation. Please try again later."));
                throw new RedirectException(session.getLastVisitedPage());
            }

            try {
                session.notifyGood(Context.tr("You are now a member of group ''" + g.getLogin() + "''"));
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Couldn't display a group name, while user should be part of it", e);
            }
            return new TeamPageUrl(invite.getGroup());
        }
        try {
            me.refuseInvitation(invite);
        } catch (final UnauthorizedOperationException e) {
            Log.web().fatal("User accepted a legitimate group invitation, but it failed", e);
            session.notifyBad(Context.tr("Oops looks like we had an issue with refusing this group invitation. Please try again later."));
            throw new RedirectException(session.getLastVisitedPage());
        }
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
