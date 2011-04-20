package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.EnumSet;
import java.util.Iterator;

import com.bloatit.common.Log;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlListItem;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.components.renderer.HtmlCachedMarkdownRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.SideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AccountPageUrl;
import com.bloatit.web.url.GiveRightActionUrl;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.SendTeamInvitationPageUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("team")
public final class TeamPage extends MasterPage {

    private final TeamPageUrl url;
    @RequestParam(name = "id", conversionErrorMsg = @tr("I cannot find the team number: ''%value%''."))
    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a team number."))
    private final Team targetTeam;

    public TeamPage(final TeamPageUrl url) {
        super(url);
        this.url = url;
        this.targetTeam = url.getTargetTeam();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateMain());
        layout.addRight(generateContactBox());
        layout.addRight(new SideBarDocumentationBlock("team_role"));
        return layout;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Consult team information");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private SideBarElementLayout generateContactBox() {
        final TitleSideBarElementLayout contacts = new TitleSideBarElementLayout();
        try {
            contacts.setTitle(Context.tr("How to contact {0}?", targetTeam.getLogin()));
        } catch (final UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("Oops, an error prevented us from showing you team name, please notify us."));
            throw new ShallNotPassException("Couldn't display team name", e);
        }

        if (targetTeam.canAccessEmail(Action.READ)) {
            try {
                contacts.add(new HtmlParagraph().addText(targetTeam.getEmail()));
            } catch (final UnauthorizedOperationException e) {
                session.notifyBad("An error prevented us from showing you team contact information. Please notify us.");
                throw new ShallNotPassException("User can't see team contact information while he should", e);
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

        // Title and team type
        HtmlTitleBlock titleBlock;
        try {
            titleBlock = new HtmlTitleBlock(targetTeam.getLogin(), 1);
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Not allowed to see team name in team page, should not happen", e);
        }
        master.add(titleBlock);

        // Avatar
        titleBlock.add(new HtmlDiv("float_left").add(TeamTools.getTeamAvatar(targetTeam)));

        // Group informations

        HtmlList informationsList = new HtmlList();

        // Visibility

        informationsList.add(new HtmlDefineParagraph(Context.tr("Visibility: "), (targetTeam.isPublic() ? Context.tr("Public")
                                                             : Context.tr("Private"))));

        // Creation date
        try {
            informationsList.add(new HtmlDefineParagraph(Context.tr("Creation date: "),
                                                                                     Context.getLocalizator()
                                                                                            .getDate(targetTeam.getDateCreation())
                                                                                            .toString(FormatStyle.LONG)));
        } catch (final UnauthorizedOperationException e) {
            // Should never happen
            Log.web().error("Not allowed to see team creation date in team page, should not happen", e);
        }

        // Member count
        informationsList.add(new HtmlDefineParagraph(Context.tr("Number of members: "),
                                                                                    String.valueOf(targetTeam.getMembers().size())));

        // Features count
        int featuresCount = getFeatureCount();
        informationsList.add(new HtmlDefineParagraph(Context.tr("Involved in features: "),
                                                                                     new HtmlMixedText(Context.tr("{0} (<0::see details>)",
                                                                                                                  featuresCount),
                                                                                                       new PageNotFoundUrl().getHtmlLink())));

        titleBlock.add(informationsList);

        // Description
        final HtmlTitleBlock description = new HtmlTitleBlock(Context.tr("Description"), 2);
        titleBlock.add(description);
        final HtmlCachedMarkdownRenderer hcmr = new HtmlCachedMarkdownRenderer(targetTeam.getDescription());
        description.add(hcmr);

        // Bank informations
        if (targetTeam.canGetInternalAccount() && targetTeam.canGetExternalAccount()) {
            try {
                final HtmlTitleBlock bankInformations = new HtmlTitleBlock(Context.tr("Bank informations"), 2);
                titleBlock.add(bankInformations);
                {
                    HtmlList bankInformationsList = new HtmlList();
                    bankInformations.add(bankInformationsList);

                    // Account balance
                    MoneyDisplayComponent amount = new MoneyDisplayComponent(targetTeam.getInternalAccount().getAmount());
                    AccountPageUrl accountPageUrl = new AccountPageUrl();
                    accountPageUrl.setTeam(targetTeam);
                    HtmlListItem accountBalanceItem = new HtmlListItem(new HtmlDefineParagraph(Context.tr("Account balance: "),
                                                                                               new HtmlMixedText(Context.tr("<0:amount (1000€):> (<1::view details>)"),
                                                                                                                 amount,
                                                                                                                 accountPageUrl.getHtmlLink())));
                    bankInformationsList.add(accountBalanceItem);

                }
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Cannot access to bank informations, should not happen", e);
            }
        }

        // Members
        final HtmlTitleBlock memberTitle = new HtmlTitleBlock(Context.tr("Members ({0})", targetTeam.getMembers().size()), 2);
        titleBlock.add(memberTitle);

        if (me != null && me.isInTeam(targetTeam) && me.canInvite(targetTeam)) {
            final SendTeamInvitationPageUrl sendInvitePage = new SendTeamInvitationPageUrl(targetTeam);
            final HtmlLink inviteMember = new HtmlLink(sendInvitePage.urlString(), Context.tr("Invite a member to this team"));
            memberTitle.add(new HtmlParagraph().add(inviteMember));
        }

        if (targetTeam.isPublic() && me != null && !me.isInTeam(targetTeam)) {
            final HtmlLink joinLink = new HtmlLink(new JoinTeamActionUrl(targetTeam).urlString(), Context.tr("Join this team"));
            memberTitle.add(joinLink);
        }

        final PageIterable<Member> members = targetTeam.getMembers();
        final HtmlTable membersTable = new HtmlTable(new MyTableModel(members));
        membersTable.setCssClass("members_table");
        memberTitle.add(membersTable);

        return master;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return TeamPage.generateBreadcrumb(targetTeam);
    }

    public static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamsPage.generateBreadcrumb();

        try {
            breadcrumb.pushLink(new TeamPageUrl(team).getHtmlLink(team.getLogin()));
        } catch (final UnauthorizedOperationException e) {
            breadcrumb.pushLink(new TeamPageUrl(team).getHtmlLink(tr("Unknown team")));
        }

        return breadcrumb;
    }

    private int getFeatureCount() {
        // TODO: real work
        return 3;
    }

    /**
     * Model used to display information about each team members.
     */
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
            return UserTeamRight.values().length + 1;
        }

        @Override
        public XmlNode getHeader(final int column) {
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
                case PROMOTE:
                    return new HtmlText(Context.tr("Promote"));
                case BANK:
                    return new HtmlText(Context.tr("Bank"));
                default:
                    return new HtmlText("");
            }
        }

        @Override
        public XmlNode getBody(final int column) {
            switch (column) {
                case 0: // Name
                    try {
                        return new HtmlLink(new MemberPageUrl(member).urlString(), member.getDisplayName());
                    } catch (final UnauthorizedOperationException e) {
                        session.notifyError("An error prevented us from showing you team name. Please notify us.");
                        throw new ShallNotPassException("Cannot display a team name", e);
                    }
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

        private XmlNode getUserRightStatus(final UserTeamRight right) {
            if (member.canInTeam(targetTeam, right)) {
                if (connectedMember != null && (connectedMember.canPromote(targetTeam) || connectedMember.equals(member))) {
                    final PlaceHolderElement ph = new PlaceHolderElement();
                    if (right == UserTeamRight.CONSULT) {
                        if (member.equals(connectedMember)) {
                            ph.add(new GiveRightActionUrl(targetTeam, member, right, false).getHtmlLink(Context.tr("Leave")));
                        } else {
                            ph.add(new GiveRightActionUrl(targetTeam, member, right, false).getHtmlLink(Context.tr("Kick")));
                        }

                    } else {
                        ph.add(new GiveRightActionUrl(targetTeam, member, right, false).getHtmlLink(Context.tr("Remove")));
                    }
                    return ph;
                }
            } else if (connectedMember != null && connectedMember.canPromote(targetTeam)) {
                return new GiveRightActionUrl(targetTeam, member, right, true).getHtmlLink(Context.tr("Grant"));
            }
            return new HtmlText("");
        }

        @Override
        public String getColumnCss(int column) {
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
            if (member.canInTeam(targetTeam, right)) {
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
