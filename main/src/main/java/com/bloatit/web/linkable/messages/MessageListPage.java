package com.bloatit.web.linkable.messages;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoJoinTeamInvitation.State;
import com.bloatit.framework.exceptions.specific.RedirectException;
import com.bloatit.framework.exceptions.specific.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.JoinTeamInvitation;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.HandleJoinTeamInvitationActionUrl;
import com.bloatit.web.url.MessageListPageUrl;
import com.bloatit.web.url.SendTeamInvitationPageUrl;

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
    public void processErrors() throws RedirectException {
        session.notifyList(url.getMessages());
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        final HtmlDiv master = new HtmlDiv("padding_box");

        final HtmlTitleBlock main = new HtmlTitleBlock(Context.tr("Bloatit private messages"), 1);
        master.add(main);

        // Generating links to team invites
        final HtmlTitleBlock teamInvites = new HtmlTitleBlock(Context.tr("Team invites"), 2);
        main.add(teamInvites);

        final HtmlLink inviteToTeam = new HtmlLink(new SendTeamInvitationPageUrl((Team) null).urlString(), "Invite people to your team");
        teamInvites.add(new HtmlParagraph().add(inviteToTeam));

        final Member me = session.getAuthToken().getMember();
        final PageIterable<JoinTeamInvitation> invitations = me.getReceivedInvitation(State.PENDING);
        for (final JoinTeamInvitation invitation : invitations) {
            final HtmlParagraph p = new HtmlParagraph();
            try {
                p.addText("Received an invitation to team '" + invitation.getTeam().getLogin() + "' from: '"
                        + invitation.getSender().getDisplayName() + "'");

                final HtmlLink accept = new HtmlLink(new HandleJoinTeamInvitationActionUrl(invitation, true).urlString(), Context.tr("accept"));
                final HtmlLink refuse = new HtmlLink(new HandleJoinTeamInvitationActionUrl(invitation, false).urlString(), Context.tr("refuse"));
                final HtmlGenericElement empty = new HtmlGenericElement();
                empty.addText(" (");
                empty.add(accept);
                empty.addText(") - (");
                empty.add(refuse);
                empty.addText(")");
                p.add(empty);

            } catch (final UnauthorizedOperationException e) {
                p.addText("You have been invited to a team, but you can't see its name");
            }
            teamInvites.add(p);
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

    @Override
    protected Breadcrumb getBreadcrumb() {
        return MessageListPage.generateBreadcrumb(session.getAuthToken().getMember());
    }

    public static Breadcrumb generateBreadcrumb(Member member) {
        Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(member);

        breadcrumb.pushLink(new MessageListPageUrl().getHtmlLink(tr("Message list")));

        return breadcrumb;
    }
}
