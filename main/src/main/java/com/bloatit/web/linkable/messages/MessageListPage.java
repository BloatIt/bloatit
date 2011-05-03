//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.messages;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoJoinTeamInvitation.State;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateReadOnlyAccessException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPublicAccessException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.JoinTeamInvitation;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.HandleJoinTeamInvitationActionUrl;
import com.bloatit.web.url.MessageListPageUrl;

/**
 * A plain list of messages received by the user
 */
@ParamContainer("messages/list")
public final class MessageListPage extends LoggedPage {
    private final MessageListPageUrl url;

    public MessageListPage(final MessageListPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member me) throws RedirectException {
        final TwoColumnLayout master = new TwoColumnLayout(true, url);

        final HtmlTitleBlock main = new HtmlTitleBlock(Context.tr("Elveos private messages"), 1);
        master.addLeft(main);

        // Generating links to team invites
        final HtmlTitleBlock teamInvites = new HtmlTitleBlock(Context.tr("Team invites"), 2);
        main.add(teamInvites);

        final PageIterable<JoinTeamInvitation> invitations = me.getReceivedInvitation(State.PENDING);
        for (final JoinTeamInvitation invitation : invitations) {
            final HtmlParagraph p = new HtmlParagraph();
            try {
                p.addText("Received an invitation to team '" + invitation.getTeam().getDisplayName() + "' from: '"
                        + invitation.getSender().getDisplayName() + "'");
            } catch (final UnauthorizedPrivateReadOnlyAccessException e) {
                throw new ShallNotPassException(e);
            } catch (UnauthorizedPublicAccessException e) {
                throw new ShallNotPassException(e);
            }

            final HtmlLink accept = new HtmlLink(new HandleJoinTeamInvitationActionUrl(invitation, true).urlString(), Context.tr("accept"));
            final HtmlLink refuse = new HtmlLink(new HandleJoinTeamInvitationActionUrl(invitation, false).urlString(), Context.tr("refuse"));

            HtmlMixedText acceptOrRefuse = new HtmlMixedText(Context.tr(" (<0::>) - (<1::>)"), accept, refuse);
            p.add(acceptOrRefuse);
            teamInvites.add(p);
        }
        return master;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to check your messages.");
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Elveos private messages.");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return MessageListPage.generateBreadcrumb(session.getAuthToken().getMember());
    }

    private static Breadcrumb generateBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(member);

        breadcrumb.pushLink(new MessageListPageUrl().getHtmlLink(tr("Message list")));

        return breadcrumb;
    }
}
