package com.bloatit.web.pages.team;

import java.util.EnumSet;
import java.util.Iterator;

import com.bloatit.common.Log;
import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlList;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webserver.components.renderer.HtmlMarkdownRenderer;
import com.bloatit.model.Contribution;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.model.TeamRole;
import com.bloatit.model.right.RightManager.Action;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.SendGroupInvitationPageUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("team")
public class TeamPage extends MasterPage {
    @SuppressWarnings("unused")
    private TeamPageUrl url; // we keep it for consistency

    @RequestParam(level = Level.ERROR)
    private Group targetTeam;

    public TeamPage(TeamPageUrl url) {
        super(url);
        this.url = url;
        this.targetTeam = url.getTargetTeam();
    }

    @Override
    protected void doCreate() throws RedirectException {
        HtmlDiv master = new HtmlDiv("padding_box");
        add(master);

        targetTeam.authenticate(session.getAuthToken());
        Member me = session.getAuthToken().getMember();
        me.authenticate(session.getAuthToken());

        // Title and group type
        HtmlTitleBlock title;
        try {
            title = new HtmlTitleBlock(Context.tr("Team :") + targetTeam.getLogin(), 1);
        } catch (UnauthorizedOperationException e) {
            throw new FatalErrorException("Not allowed to see group name in group page, should not happen", e);
        }
        master.add(title);
        title.add(new HtmlParagraph().addText(Context.tr("({0} group)", targetTeam.isPublic() ? "Public" : "Private")));

        // Link to join if needed
        if (!session.getAuthToken().getMember().isInGroup(targetTeam)) {
            if (targetTeam.isPublic()) {
                HtmlLink joinLink = new HtmlLink(new JoinTeamActionUrl(targetTeam).urlString(), Context.tr("Join this group"));
                title.add(joinLink);
            } else {
                title.add(new HtmlParagraph().addText("Send a request to join group"));
            }
        }

        // Description
        // TODO add cache
        HtmlTitleBlock description = new HtmlTitleBlock(Context.tr("Description"), 2);
        title.add(description);
        HtmlMarkdownRenderer hmr = new HtmlMarkdownRenderer(targetTeam.getDescription());
        description.add(hmr);

        // Contact
        HtmlTitleBlock contacts = new HtmlTitleBlock(Context.tr("How to contact us"), 2);
        title.add(contacts);

        if (targetTeam.canAccessEmail(Action.READ)) {
            try {
                contacts.add(new HtmlParagraph().addText(targetTeam.getEmail()));
            } catch (UnauthorizedOperationException e) {
                // Should not happen
                contacts.add(new HtmlParagraph().addText("No public contact information available"));
            }
        } else {
            contacts.add(new HtmlParagraph().addText("No public contact information available"));
        }

        // Members
        HtmlTitleBlock memberTitle = new HtmlTitleBlock(Context.tr("Members"), 2);
        title.add(memberTitle);

        HtmlList memberList = new HtmlList();
        memberTitle.add(memberList);

        if (me.canInvite(targetTeam, Action.WRITE)) {
            // First line is always "invite a member"
            HtmlLink inviteMember = new HtmlLink(new SendGroupInvitationPageUrl().urlString(), Context.tr("Invite a member to this group"));
            memberList.add(inviteMember);
        }

        PageIterable<Member> members = targetTeam.getMembers();
        for (Member m : members) {
            try {
                HtmlLink memberLink = new HtmlLink(new MemberPageUrl(m).urlString(), m.getDisplayName());
                memberList.add(memberLink);
            } catch (UnauthorizedOperationException e) {
                Log.web().warn("Not allowed to see a display name", e);
            }
        }

        HtmlTable membersTable = new HtmlTable(new MyTableModel(members));
        memberTitle.add(membersTable);

    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Consult team information");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private class MyTableModel extends HtmlTableModel {
        private PageIterable<Member> members;
        private Member member;
        private Iterator<Member> iterator;

        public MyTableModel(PageIterable<Member> members) {
            this.members = members;
            iterator = members.iterator();
        }

        @Override
        public int getColumnCount() {
            return UserGroupRight.values().length + 1;
        }

        @Override
        public String getHeader(int column) {
            if (column == 0) {
                return Context.tr("Member name");
            }
            EnumSet<UserGroupRight> e = EnumSet.allOf(UserGroupRight.class);
            UserGroupRight ugr = (UserGroupRight) e.toArray()[column - 1];
            switch (ugr) {
            case CONSULT:
                return Context.tr("Consult");
            case TALK:
                return Context.tr("Talk");
            case MODIFY:
                return Context.tr("Modify");
            case INVITE:
                return Context.tr("Invite");                
            case PROMOTE:
                return Context.tr("Promote");
            case BANK:
                return Context.tr("Bank");
            default:
                return "";
            }
        }

        @Override
        public String getBody(int column) {
            if (column == 0) {
                try {
                    return member.getDisplayName();
                } catch (UnauthorizedOperationException e) {
                    Log.web().warn("Not allowed to see a display name", e);
                    return "";
                }
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
