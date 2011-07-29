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
package com.bloatit.web.linkable.members.tabs;

import com.bloatit.data.DaoJoinTeamInvitation.State;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.JoinTeamInvitation;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.url.ContributionInvoicingProcessUrl;
import com.bloatit.web.url.HandleJoinTeamInvitationActionUrl;

public class InvitationsTab extends HtmlTab {
    private final Member member;

    public InvitationsTab(final Member member, final String title, final String tabKey) {
        super(title, tabKey);
        this.member = member;
    }

    @Override
    public XmlNode generateBody() {

        final HtmlDiv master = new HtmlDiv("tab_pane");

        // Generating links to team invites
        final HtmlTitleBlock teamInvites = new HtmlTitleBlock(Context.tr("Team invites"), 2);
        master.add(teamInvites);

        final PageIterable<JoinTeamInvitation> invitations = member.getReceivedInvitation(State.PENDING);
        for (final JoinTeamInvitation invitation : invitations) {
            final HtmlParagraph p = new HtmlParagraph();
            try {
                p.addText("Received an invitation to team '" + invitation.getTeam().getDisplayName() + "' from: '"
                        + invitation.getSender().getDisplayName() + "'");
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException(e);
            }
            final HtmlLink accept = new HtmlLink(new HandleJoinTeamInvitationActionUrl(Context.getSession().getShortKey(), true, invitation).urlString(),
                                                 Context.tr("accept"));
            final HtmlLink refuse = new HtmlLink(new HandleJoinTeamInvitationActionUrl(Context.getSession().getShortKey(), false, invitation).urlString(),
                                                 Context.tr("refuse"));

            final HtmlMixedText acceptOrRefuse = new HtmlMixedText(Context.tr(" (<0::>) - (<1::>)"), accept, refuse);
            p.add(acceptOrRefuse);
            teamInvites.add(p);
        }

        final HtmlTitleBlock incoiving = new HtmlTitleBlock(Context.tr("Milestones to invoice"), 2);
        master.add(incoiving);

        final PageIterable<Milestone> milestoneToInvoice = member.getMilestoneToInvoice();

        for (final Milestone milestone : milestoneToInvoice) {
            final HtmlParagraph p = new HtmlParagraph();
            p.addText("Invoicing " + milestone.getOffer().getFeature().getTitle() + " - Milestone " + milestone.getPosition());
            p.add(new ContributionInvoicingProcessUrl(milestone.getOffer().getAuthor(), milestone).getHtmlLink("Generate invoices"));
            incoiving.add(p);
        }

        return master;
    }

}
