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
package com.bloatit.web.linkable.team.tabs;

import java.util.EnumSet;
import java.util.Iterator;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.url.GiveRightActionUrl;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.SendTeamInvitationPageUrl;

public class MembersTab extends HtmlTab {
    private final Team team;
    private final Member visitor;

    public MembersTab(final Team team, final String title, final String tabKey, final Member member) {
        super(title, tabKey);
        this.team = team;
        this.visitor = member;
    }

    @Override
    public HtmlNode generateBody() {
        final HtmlDiv master = new HtmlDiv("tab_pane");

        // Members
        final HtmlTitleBlock memberTitle = new HtmlTitleBlock(Context.tr("Members ({0})", team.getMembers().size()), 1);
        master.add(memberTitle);

        if (visitor != null) {
            if (visitor.hasInviteTeamRight(team)) {
                final SendTeamInvitationPageUrl sendInvitePage = new SendTeamInvitationPageUrl(team);
                final HtmlLink inviteMember = new HtmlLink(sendInvitePage.urlString(), Context.tr("Invite a member to this team"));
                memberTitle.add(new HtmlParagraph().add(inviteMember));
            }

            if (team.isPublic() && !visitor.isInTeam(team)) {
                final HtmlLink joinLink = new HtmlLink(new JoinTeamActionUrl(Context.getSession().getShortKey(), team).urlString(),
                                                       Context.tr("Join this team"));
                memberTitle.add(joinLink);
            }
        }

        final PageIterable<Member> members = team.getMembers();
        final HtmlTable membersTable = new HtmlTable(new MyTableModel(members, visitor));
        membersTable.setCssClass("members_table");
        memberTitle.add(membersTable);

        return master;
    }

    private class MyTableModel extends HtmlTableModel {
        private final PageIterable<Member> members;
        private Member member;
        private Iterator<Member> iterator;
        private Member visitor;
        private static final int CONSULT = 1;
        private static final int TALK = 2;
        private static final int MODIFY = 4;
        private static final int INVITE = 3;
        private static final int BANK = 5;
        private static final int PROMOTE = 6;

        public MyTableModel(final PageIterable<Member> members, final Member visitor) {
            this.members = members;
            this.visitor = visitor;
            iterator = members.iterator();
        }

        @Override
        public int getColumnCount() {
            return UserTeamRight.values().length + 1;
        }

        @Override
        public String getTitle(final int column) {
            switch (column) {
                case CONSULT:
                    return Context.tr("The user can see informations relative to this team. Every member of a team can do this");
                case TALK:
                    return Context.tr("The user is allowed to express the team opinion");
                case MODIFY:
                    return Context.tr("The user can modify the team informations (contact, description …)");
                case INVITE:
                    return Context.tr("The user can invite other people to join the team (private teams only)");
                case PROMOTE:
                    return Context.tr("The user can change the rights of the members of the team");
                case BANK:
                    return Context.tr("The user can access and modify the team bank informations (he can also withdraw money from the team account)");
                default:
                    return null;
            }
        }
        
        @Override
        public HtmlNode getHeader(final int column) {
            if (column == 0) {
                return new HtmlText(Context.tr("Member name"));
            }
            final EnumSet<UserTeamRight> e = EnumSet.allOf(UserTeamRight.class);
            final UserTeamRight ugr = (UserTeamRight) e.toArray()[column - 1];
            switch (ugr) {
                case CONSULT:
                    return new HtmlText(Context.tr("Consult"));
                case TALK:
                    return new HtmlText(Context.tr("Talk"));
                case MODIFY:
                    return new HtmlText(Context.tr("Modify"));
                case INVITE:
                    return new HtmlText(Context.tr("Invite"));
                case BANK:
                    return new HtmlText(Context.tr("Bank"));
                case PROMOTE:
                    return new HtmlText(Context.tr("Promote"));
                default:
                    return new HtmlText("");
            }
        }

        @Override
        public HtmlNode getBody(final int column) {
            switch (column) {
                case 0: // Name
                    return new HtmlLink(new MemberPageUrl(member).urlString(), member.getDisplayName());
                case CONSULT:
                    return getUserRightStatus(UserTeamRight.CONSULT);
                case TALK:
                    return getUserRightStatus(UserTeamRight.TALK);
                case MODIFY:
                    return getUserRightStatus(UserTeamRight.MODIFY);
                case INVITE:
                    return getUserRightStatus(UserTeamRight.INVITE);
                case PROMOTE:
                    return getUserRightStatus(UserTeamRight.PROMOTE);
                case BANK:
                    return getUserRightStatus(UserTeamRight.BANK);
                default:
                    return new HtmlText("");
            }
        }

        private HtmlNode getUserRightStatus(final UserTeamRight right) {

            final PlaceHolderElement ph = new PlaceHolderElement();

            if (right == UserTeamRight.CONSULT) {
                if (member.canBeKickFromTeam(team, visitor)) {
                    if (member.equals(visitor)) {
                        ph.add(new GiveRightActionUrl(Context.getSession().getShortKey(), false, right, member, team).getHtmlLink(Context.tr("Leave")));
                    } else {
                        ph.add(new GiveRightActionUrl(Context.getSession().getShortKey(), false, right, member, team).getHtmlLink(Context.tr("Kick")));
                    }
                }
            } else {
                if (team.canChangeRight(visitor, member, right, true)) {
                    ph.add(new GiveRightActionUrl(Context.getSession().getShortKey(), true, right, member, team).getHtmlLink(Context.tr("Grant")));
                } else if (team.canChangeRight(visitor, member, right, false)) {
                    ph.add(new GiveRightActionUrl(Context.getSession().getShortKey(), false, right, member, team).getHtmlLink(Context.tr("Remove")));
                }
            }

            return ph;
        }

        @Override
        public String getColumnCss(final int column) {
            switch (column) {
                case 0: // Name
                    return "name";
                case CONSULT:
                    return getUserRightStyle(UserTeamRight.CONSULT);
                case TALK:
                    return getUserRightStyle(UserTeamRight.TALK);
                case MODIFY:
                    return getUserRightStyle(UserTeamRight.MODIFY);
                case INVITE:
                    return getUserRightStyle(UserTeamRight.INVITE);
                case PROMOTE:
                    return getUserRightStyle(UserTeamRight.PROMOTE);
                case BANK:
                    return getUserRightStyle(UserTeamRight.BANK);
                default:
                    return "";
            }
        }

        private String getUserRightStyle(final UserTeamRight right) {
            if (member.hasTeamRight(team, right)) {
                return "can";
            }
            return "";
        }

        @Override
        public boolean next() {
            if (iterator == null) {
                iterator = members.iterator();
            }

            if (iterator.hasNext()) {
                member = iterator.next();
                return true;
            }
            return false;
        }
    }

}
