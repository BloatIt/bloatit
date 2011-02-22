package com.bloatit.web.pages.team;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.SendGroupInvitationActionUrl;

/**
 * <p>
 * An action used to send group invitations
 * </p>
 */
@ParamContainer("invitation/dosend")
public class SendGroupInvitationAction extends LoggedAction {
    public final static String GROUP_JOIN_CODE = "bloatit_join_group";
    public final static String RECEIVER_CODE = "bloatit_join_receiver";

    @RequestParam(name = GROUP_JOIN_CODE, role = Role.POST)
    private final Group group;

    @RequestParam(name = RECEIVER_CODE, role = Role.POST)
    private final Member receiver;

    private final SendGroupInvitationActionUrl url;

    public SendGroupInvitationAction(final SendGroupInvitationActionUrl url) {
        super(url);
        this.url = url;
        this.group = url.getGroup();
        this.receiver = url.getReceiver();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        final Member me = session.getAuthToken().getMember();

        try {
            me.sendInvitation(receiver, group);
            session.notifyGood("Invitation sent to " + receiver.getDisplayName() + " for group " + group.getLogin());
        } catch (final UnauthorizedOperationException e) {
            e.printStackTrace();
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
        return Context.tr("You must be logged to send an invitation");
    }

    @Override
    protected void transmitParameters() {
        throw new NotImplementedException();
    }
}
