package com.bloatit.web.actions;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.web.url.MessageListPageUrl;
import com.bloatit.web.url.SendGroupInvitationActionUrl;

@ParamContainer("invitation/dosend")
public class SendGroupInvitationAction extends LoggedAction {
    public final static String GROUP_JOIN_CODE = "bloatit_join_group";
    public final static String RECEIVER_CODE = "bloatit_join_receiver";

    @RequestParam(level = Level.ERROR, name = GROUP_JOIN_CODE, role = Role.POST)
    private Group group;

    @RequestParam(level = Level.ERROR, name = RECEIVER_CODE, role = Role.POST)
    private Member receiver;

    private SendGroupInvitationActionUrl url;

    public SendGroupInvitationAction(SendGroupInvitationActionUrl url) {
        super(url);
        this.url = url;
        this.group = url.getGroup();
        this.receiver = url.getReceiver();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        Member me = session.getAuthToken().getMember();
        me.authenticate(session.getAuthToken());

        try {
            me.invite(receiver, group);
            session.notifyGood("Invitation sent to " + receiver.getDisplayName() + " for group " + group.getLogin());
        } catch (UnauthorizedOperationException e) {
            e.printStackTrace();
        }

        return new MessageListPageUrl();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        throw new NotImplementedException();
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
