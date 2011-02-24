package com.bloatit.web.pages.messages;

import com.bloatit.data.DaoJoinGroupInvitation.State;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Group;
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.Member;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.url.HandleJoinGroupInvitationActionUrl;
import com.bloatit.web.url.MessageListPageUrl;
import com.bloatit.web.url.SendGroupInvitationPageUrl;

/**
 * A plain list of messages received by the user
 */
@ParamContainer("messages/list")
public class MessageListPage extends LoggedPage {
    @SuppressWarnings("unused")
    private final MessageListPageUrl url; // kept for the sake of consistency

    public MessageListPage(final MessageListPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        final HtmlDiv master = new HtmlDiv("padding_box");

        final HtmlTitleBlock main = new HtmlTitleBlock(Context.tr("Bloatit private messages"), 1);
        master.add(main);

        // Generating links to group invites
        final HtmlTitleBlock groupInvites = new HtmlTitleBlock(Context.tr("Group invites"), 2);
        main.add(groupInvites);

        final HtmlLink inviteToGroup = new HtmlLink(new SendGroupInvitationPageUrl((Group) null).urlString(), "Invite people to your group");
        groupInvites.add(new HtmlParagraph().add(inviteToGroup));

        final Member me = session.getAuthToken().getMember();
        final PageIterable<JoinGroupInvitation> invitations = me.getReceivedInvitation(State.PENDING);
        for (final JoinGroupInvitation invitation : invitations) {
            final HtmlParagraph p = new HtmlParagraph();
            try {
                p.addText("Received an invitation to group '" + invitation.getGroup().getLogin() + "' from: '"
                        + invitation.getSender().getDisplayName() + "'");

                final HtmlLink accept = new HtmlLink(new HandleJoinGroupInvitationActionUrl(invitation, true).urlString(), Context.tr("accept"));
                final HtmlLink refuse = new HtmlLink(new HandleJoinGroupInvitationActionUrl(invitation, false).urlString(), Context.tr("refuse"));
                final HtmlGenericElement empty = new HtmlGenericElement();
                empty.addText(" (");
                empty.add(accept);
                empty.addText(") - (");
                empty.add(refuse);
                empty.addText(")");
                p.add(empty);

            } catch (final UnauthorizedOperationException e) {
                p.addText("You have been invited to a group, but you can't see its name");
            }
            groupInvites.add(p);
        }
        return master;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to check your messages.");
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Bloatit private messages.");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
