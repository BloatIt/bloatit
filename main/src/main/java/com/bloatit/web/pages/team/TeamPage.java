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
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.framework.webserver.components.renderer.HtmlMarkdownRenderer;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.Group;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.Member;
import com.bloatit.model.right.Action;
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

        Member me = null;
        if (session.getAuthToken() != null) {
            me = session.getAuthToken().getMember();
            if (me != null) {
                me.authenticate(session.getAuthToken());
            }
        }

        // Title and group type
        HtmlTitleBlock title;
        try {
            title = new HtmlTitleBlock(Context.tr("Team: ") + targetTeam.getLogin(), 1);
        } catch (UnauthorizedOperationException e) {
            throw new FatalErrorException("Not allowed to see group name in group page, should not happen", e);
        }
        master.add(title);
        title.add(new HtmlParagraph().addText(Context.tr("({0} group)", targetTeam.isPublic() ? "Public" : "Private")));

        // Link to join if needed
        if (me != null && !me.isInGroup(targetTeam)) {
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
                Log.web().error("Cannot access to team email, I checked just before tho", e);
                contacts.add(new HtmlParagraph().addText("No public contact information available"));
            }
        } else {
            contacts.add(new HtmlParagraph().addText("No public contact information available"));
        }

        HtmlBranch financial;
        if (targetTeam.canGetInternalAccount() && targetTeam.canGetInternalAccount()) {
            financial = new HtmlTitleBlock(Context.tr("Group financial information"), 2);
        } else {
            financial = new PlaceHolderElement();
        }
        title.add(financial);

        // External account
        if (targetTeam.canGetExternalAccount()) {
            try {
                ExternalAccount exAccount = targetTeam.getExternalAccount();
                exAccount.authenticate(session.getAuthToken());
                HtmlTitleBlock external = new HtmlTitleBlock(Context.tr("Team's external account"), 3);
                financial.add(external);
                HtmlList exAccountInfo = new HtmlList();
                external.add(exAccountInfo);

                if (exAccount.canAccessAmount())
                    exAccountInfo.add(Context.tr("Money available: {0} ", Context.getLocalizator().getCurrency(exAccount.getAmount())
                            .getDefaultString()));
                if (exAccount.canAccessBankCode())
                    exAccountInfo.add(Context.tr("Bank code: {0}", exAccount.getBankCode()));
            } catch (UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Cannot access to bank external accound, I checked just before tho", e);
            }
        }

        // Internal account
        if (targetTeam.canGetInternalAccount()) {
            try {
                InternalAccount inAccount = targetTeam.getInternalAccount();
                inAccount.authenticate(session.getAuthToken());
                HtmlTitleBlock internal = new HtmlTitleBlock(Context.tr("Team's internal account"), 3);
                financial.add(internal);
                HtmlList inAccountInfo = new HtmlList();
                internal.add(inAccountInfo);

                if (inAccount.canAccessAmount())
                    inAccountInfo.add(Context.tr("Money available: {0} ", Context.getLocalizator().getCurrency(inAccount.getAmount())
                            .getDefaultString()));
            } catch (UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Cannot access to bank internal accound, I checked just before tho", e);
            }
        }

        // Members
        HtmlTitleBlock memberTitle = new HtmlTitleBlock(Context.tr("Members"), 2);
        title.add(memberTitle);

        if (me != null && me.isInGroup(targetTeam) && me.canSendInvitation(targetTeam, Action.WRITE)) {
            SendGroupInvitationPageUrl sendInvitePage = new SendGroupInvitationPageUrl();
            sendInvitePage.setGroup(targetTeam);
            HtmlLink inviteMember = new HtmlLink(sendInvitePage.urlString(), Context.tr("Invite a member to this team"));
            memberTitle.add(new HtmlParagraph().add(inviteMember));
        }

        PageIterable<Member> members = targetTeam.getMembers();
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
        public XmlNode getHeader(int column) {
            if (column == 0) {
                return new HtmlText(Context.tr("Member name"));
            }
            EnumSet<UserGroupRight> e = EnumSet.allOf(UserGroupRight.class);
            UserGroupRight ugr = (UserGroupRight) e.toArray()[column - 1];
            switch (ugr) {
            case CONSULT:
                return new HtmlText(Context.tr("Consult"));
            case TALK:
                return new HtmlText(Context.tr("Talk"));
            case MODIFY:
                return new HtmlText(Context.tr("Modify"));
            case INVITE:
                return new HtmlText(Context.tr("Invite"));
            case PROMOTE:
                return new HtmlText(Context.tr("Promote"));
            case BANK:
                return new HtmlText(Context.tr("Bank"));
            default:
                return new HtmlText("");
            }
        }

        @Override
        public XmlNode getBody(int column) {
            if (column == 0) {
                try {
                    return new HtmlLink(new MemberPageUrl(member).urlString(), member.getDisplayName());
                } catch (UnauthorizedOperationException e) {
                    Log.web().warn("Not allowed to see a display name", e);
                    return new HtmlText("");
                }
            }
            return new HtmlText("");
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
