package com.bloatit.web.pages.messages;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.team.SendGroupInvitationAction;
import com.bloatit.web.url.SendGroupInvitationActionUrl;
import com.bloatit.web.url.SendGroupInvitationPageUrl;

@ParamContainer("invitation/send")
public class SendGroupInvitationPage extends LoggedPage {
    private SendGroupInvitationPageUrl url;

    public SendGroupInvitationPage(SendGroupInvitationPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        HtmlDiv master = new HtmlDiv("padding_box");

        SendGroupInvitationActionUrl target = new SendGroupInvitationActionUrl();
        HtmlForm form = new HtmlForm(target.urlString());
        master.add(form);

        Member me = session.getAuthToken().getMember();
        me.authenticate(session.getAuthToken());

        HtmlDropDown groupInput = new HtmlDropDown(SendGroupInvitationAction.GROUP_JOIN_CODE, Context.tr("Select group"));
        form.add(groupInput);
        try {
            for (Group g : me.getGroups()) {
                try {
                    groupInput.addDropDownElement(g.getId().toString(), g.getLogin());
                } catch (UnauthorizedOperationException e) {
                    e.printStackTrace();
                }
            }

            HtmlDropDown receiverInput = new HtmlDropDown(SendGroupInvitationAction.RECEIVER_CODE, Context.tr("Select group"));
            form.add(receiverInput);
            for (Member m : MemberManager.getMembers()) {
                try {
                    if (!m.equals(me)) {
                        receiverInput.addDropDownElement(m.getId().toString(), m.getLogin());
                    }
                } catch (UnauthorizedOperationException e) {
                    throw new FatalErrorException(e);
                }
            }

            form.add(new HtmlSubmit(Context.tr("Submit")));

            return master;

        } catch (UnauthorizedOperationException e1) {
            throw new FatalErrorException(e1);
        }
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("Your must be logged to send group invitations");
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Send group invitations");
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
