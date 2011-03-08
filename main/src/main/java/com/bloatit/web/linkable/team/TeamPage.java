package com.bloatit.web.linkable.team;

import java.util.EnumSet;
import java.util.Iterator;

import com.bloatit.common.Log;
import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.Image.ImageType;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlList;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.framework.webserver.components.renderer.HtmlMarkdownRenderer;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.Group;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.Member;
import com.bloatit.model.right.Action;
import com.bloatit.web.pages.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.SideBarElementLayout;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.GiveRightActionUrl;
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
    private final TeamPageUrl url; // we keep it for consistency

    @RequestParam()
    private final Group targetTeam;

    public TeamPage(final TeamPageUrl url) {
        super(url);
        this.url = url;
        this.targetTeam = url.getTargetTeam();
    }

    @Override
    protected void doCreate() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true);
        layout.addLeft(generateMain());
        layout.addRight(generateContactBox());
        layout.addRight(new SideBarDocumentationBlock("team_role"));

        add(layout);
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Consult team information");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private SideBarElementLayout generateContactBox() {
        SideBarElementLayout contacts = new SideBarElementLayout();
        try {
            contacts.setTitle(Context.tr("How to contact {0}", targetTeam.getLogin()));
        } catch (UnauthorizedOperationException e) {
            Log.web().warn("Can't access a team display name", e);
            contacts.setTitle(Context.tr("How to contact us"));
        }

        if (targetTeam.canAccessEmail(Action.READ)) {
            try {
                contacts.add(new HtmlParagraph().addText(targetTeam.getEmail()));
            } catch (final UnauthorizedOperationException e) {
                // Should not happen
                Log.web().error("Cannot access to team email, I checked just before tho", e);
                contacts.add(new HtmlParagraph().addText("No public contact information available"));
            }
        } else {
            contacts.add(new HtmlParagraph().addText("No public contact information available"));
        }

        return contacts;
    }

    private HtmlElement generateMain() {
        final HtmlDiv master = new HtmlDiv();
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
        } catch (final UnauthorizedOperationException e) {
            throw new FatalErrorException("Not allowed to see group name in group page, should not happen", e);
        }
        master.add(title);
        title.add(new HtmlParagraph().addText(Context.tr("({0} group)", targetTeam.isPublic() ? "Public" : "Private")));

        // Link to join if needed
        if (me != null && !me.isInGroup(targetTeam)) {
            if (targetTeam.isPublic()) {
                final HtmlLink joinLink = new HtmlLink(new JoinTeamActionUrl(targetTeam).urlString(), Context.tr("Join this group"));
                title.add(joinLink);
            } else {
                title.add(new HtmlParagraph().addText("Send a request to join group"));
            }
        }

        // Description
        // TODO add cache
        final HtmlTitleBlock description = new HtmlTitleBlock(Context.tr("Description"), 2);
        title.add(description);
        final HtmlMarkdownRenderer hmr = new HtmlMarkdownRenderer(targetTeam.getDescription());
        description.add(hmr);

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
                final ExternalAccount exAccount = targetTeam.getExternalAccount();
                exAccount.authenticate(session.getAuthToken());
                final HtmlTitleBlock external = new HtmlTitleBlock(Context.tr("Team's external account"), 3);
                financial.add(external);
                final HtmlList exAccountInfo = new HtmlList();
                external.add(exAccountInfo);

                if (exAccount.canAccessAmount()) {
                    exAccountInfo.add(Context.tr("Money available: {0} ", Context.getLocalizator()
                                                                                 .getCurrency(exAccount.getAmount())
                                                                                 .getDefaultString()));
                }
                if (exAccount.canAccessBankCode()) {
                    exAccountInfo.add(Context.tr("Bank code: {0}", exAccount.getBankCode()));
                }
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Cannot access to bank external accound, I checked just before tho", e);
            }
        }

        // Internal account
        if (targetTeam.canGetInternalAccount()) {
            try {
                final InternalAccount inAccount = targetTeam.getInternalAccount();
                inAccount.authenticate(session.getAuthToken());
                final HtmlTitleBlock internal = new HtmlTitleBlock(Context.tr("Team's internal account"), 3);
                financial.add(internal);
                final HtmlList inAccountInfo = new HtmlList();
                internal.add(inAccountInfo);

                if (inAccount.canAccessAmount()) {
                    inAccountInfo.add(Context.tr("Money available: {0} ", Context.getLocalizator()
                                                                                 .getCurrency(inAccount.getAmount())
                                                                                 .getDefaultString()));
                }
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Cannot access to bank internal accound, I checked just before tho", e);
            }
        }

        // Members
        final HtmlTitleBlock memberTitle = new HtmlTitleBlock(Context.tr("Members"), 2);
        title.add(memberTitle);

        if (me != null && me.isInGroup(targetTeam) && me.canSendInvitation(targetTeam, Action.WRITE)) {
            final SendGroupInvitationPageUrl sendInvitePage = new SendGroupInvitationPageUrl(targetTeam);
            final HtmlLink inviteMember = new HtmlLink(sendInvitePage.urlString(), Context.tr("Invite a member to this team"));
            memberTitle.add(new HtmlParagraph().add(inviteMember));
        }

        final PageIterable<Member> members = targetTeam.getMembers();
        final HtmlTable membersTable = new HtmlTable(new MyTableModel(members));
        memberTitle.add(membersTable);

        return master;
    }

    private class MyTableModel extends HtmlTableModel {
        private final PageIterable<Member> members;
        private Member member;
        private Iterator<Member> iterator;
        private Member connectedMember;

        private static final int CONSULT = 1;
        private static final int TALK = 2;
        private static final int MODIFY = 3;
        private static final int INVITE = 4;
        private static final int PROMOTE = 5;
        private static final int BANK = 6;

        public MyTableModel(final PageIterable<Member> members) {
            this.members = members;
            if (session.getAuthToken() != null) {
                this.connectedMember = session.getAuthToken().getMember();
            }
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
            final EnumSet<UserGroupRight> e = EnumSet.allOf(UserGroupRight.class);
            final UserGroupRight ugr = (UserGroupRight) e.toArray()[column - 1];
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
            switch (column) {
                case 0: // Name
                    try {
                        return new HtmlLink(new MemberPageUrl(member).urlString(), member.getDisplayName());
                    } catch (final UnauthorizedOperationException e) {
                        Log.web().warn("Not allowed to see a display name", e);
                        return new HtmlText("");
                    }
                case CONSULT:
                    return getUserRightStatus(UserGroupRight.CONSULT);
                case TALK:
                    return getUserRightStatus(UserGroupRight.TALK);
                case MODIFY:
                    return getUserRightStatus(UserGroupRight.MODIFY);
                case INVITE:
                    return getUserRightStatus(UserGroupRight.INVITE);
                case PROMOTE:
                    return getUserRightStatus(UserGroupRight.PROMOTE);
                case BANK:
                    return getUserRightStatus(UserGroupRight.BANK);
                default:
                    return new HtmlText("");
            }
        }

        private XmlNode getUserRightStatus(UserGroupRight right) {
            if (member.canInGroup(targetTeam, right)) {
                if (connectedMember != null && (connectedMember.canPromote(targetTeam) || connectedMember.equals(member))) {
                    PlaceHolderElement ph = new PlaceHolderElement();
                    ph.add(new HtmlImage(new Image("valid.svg", ImageType.LOCAL), Context.tr("OK"), "group_can"));
                    ph.add(new GiveRightActionUrl(targetTeam, member, right, false).getHtmlLink(Context.tr("Remove")));
                    return ph;
                }
                return new HtmlImage(new Image("valid.svg", ImageType.LOCAL), Context.tr("OK"), "group_can");
            } else if (connectedMember != null && connectedMember.canPromote(targetTeam)) {
                return new GiveRightActionUrl(targetTeam, member, right, true).getHtmlLink(Context.tr("Promote"));
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
