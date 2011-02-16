package com.bloatit.web.actions;

import org.apache.commons.lang.NotImplementedException;

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
import com.bloatit.web.url.HandleJoinGroupInvitationActionUrl;
import com.bloatit.web.url.TeamPageUrl;

@ParamContainer("group/dojoin")
public class HandleJoinGroupInvitationAction extends LoggedAction {
    private final HandleJoinGroupInvitationActionUrl url;

    @RequestParam(level = Level.ERROR)
    private JoinGroupInvitation invite;

    @RequestParam(level = Level.ERROR)
    private Boolean accept;

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
            invite.accept();
            try {
                Group g = invite.getGroup();
                g.authenticate(session.getAuthToken());
                session.notifyGood(Context.tr("You are now a member of group " + g.getLogin()));
            } catch (UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Couldn't display a group name, while user should be part of it", e);
            }
            return new TeamPageUrl(invite.getGroup());
        }
        invite.refuse();
        return session.pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        throw new NotImplementedException();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged before you accept (or refuse) a group invite");
    }

    @Override
    protected void transmitParameters() {

    }
}
