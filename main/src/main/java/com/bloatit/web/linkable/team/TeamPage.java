package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.EnumSet;
import java.util.Iterator;

import com.bloatit.common.Log;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlListItem;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.components.renderer.HtmlCachedMarkdownRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.PageNotFound;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.SideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.GiveRightActionUrl;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.SendTeamInvitationPageUrl;
import com.bloatit.web.url.TeamPageUrl;
import java.math.BigDecimal;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("team")
public final class TeamPage extends MasterPage {

    private final TeamPageUrl url;
    @RequestParam(name = "id", conversionErrorMsg =
    @tr("I cannot find the team number: ''%value%''."))
    @ParamConstraint(optionalErrorMsg =
    @tr("You have to specify a team number."))
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

        // Avatar
        master.add(new HtmlDiv("float_right").add(TeamTools.getTeamAvatar(targetTeam)));

        // Title and team type
        HtmlTitleBlock titleBlock;
        try {
            titleBlock = new HtmlTitleBlock(targetTeam.getLogin(), 1);
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Not allowed to see team name in team page, should not happen", e);
        }
        master.add(titleBlock);

        // Description
        final HtmlTitleBlock description = new HtmlTitleBlock(Context.tr("Description"), 2);
        titleBlock.add(description);
        final HtmlCachedMarkdownRenderer hcmr = new HtmlCachedMarkdownRenderer(targetTeam.getDescription());
        description.add(hcmr);

        // Group informations
        final HtmlTitleBlock groupInformations = new HtmlTitleBlock(Context.tr("Group informations"), 2);
        titleBlock.add(groupInformations);
        {
            HtmlList informationsList = new HtmlList();
            groupInformations.add(informationsList);

            // Visibility
            HtmlListItem visibitityItem = new HtmlListItem(new HtmlParagraph().addText((targetTeam.isPublic() ? Context.tr("Public team") : Context.tr("Private team"))));
            informationsList.add(visibitityItem);

            // Creation date
            try {
                HtmlListItem creationDateItem = new HtmlListItem(new HtmlDefineParagraph(Context.tr("Creation date: "), Context.getLocalizator().getDate(targetTeam.getDateCreation()).toString(FormatStyle.LONG)));
                informationsList.add(creationDateItem);
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Not allowed to see team creation date in team page, should not happen", e);
            }

            // Member count
            HtmlListItem numberOfMembersItem = new HtmlListItem(new HtmlDefineParagraph(Context.tr("Number of members: "), String.valueOf(targetTeam.getMembers().size())));
            informationsList.add(numberOfMembersItem);

            // Features count
            int featuresCount = getFeatureCount();
            HtmlListItem numberOfFeaturesItem = new HtmlListItem(new HtmlDefineParagraph(Context.tr("Involved in features: "), new HtmlMixedText(Context.tr("{0} (<0::see details>)",featuresCount), new PageNotFoundUrl().getHtmlLink())));
            informationsList.add(numberOfFeaturesItem);
        }
       
        // Group informations
        if(targetTeam.canGetInternalAccount() && targetTeam.canGetExternalAccount()) {
            try {
                final HtmlTitleBlock bankInformations = new HtmlTitleBlock(Context.tr("Bank informations"), 2);
                titleBlock.add(bankInformations);
                {
                    HtmlList informationsList = new HtmlList();
                    bankInformations.add(informationsList);

                    // Account balance
                    MoneyDisplayComponent amount = new MoneyDisplayComponent(targetTeam.getInternalAccount().getAmount());
                    HtmlListItem accountBalanceItem = new HtmlListItem(new HtmlDefineParagraph(Context.tr("Account balance: "), new HtmlMixedText(Context.tr("<0:amount (1000€):> (<1::view details>)"), amount ,new PageNotFoundUrl().getHtmlLink())));
                    informationsList.add(accountBalanceItem);

                }
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Cannot access to bank informations, should not happen", e);
            }
        }


        //title.add();

        // Link to join if needed
//        if (me != null && !me.isInTeam(targetTeam)) {
//            if (targetTeam.isPublic()) {
//                final HtmlLink joinLink = new HtmlLink(new JoinTeamActionUrl(targetTeam).urlString(), Context.tr("Join this team"));
//                title.add(joinLink);
//            } else {
//                title.add(new HtmlParagraph().addText("Send a request to join team"));
//            }
//        }



//
//
//        HtmlBranch financial;
//        if (targetTeam.canGetInternalAccount() && targetTeam.canGetInternalAccount()) {
//            financial = new HtmlTitleBlock(Context.tr("Team financial information"), 2);
//        } else {
//            financial = new PlaceHolderElement();
//        }
//        titleBlock.add(financial);
//
//        // External account
//        if (targetTeam.canGetExternalAccount()) {
//            try {
//                final ExternalAccount exAccount = targetTeam.getExternalAccount();
//                exAccount.authenticate(session.getAuthToken());
//                final HtmlTitleBlock external = new HtmlTitleBlock(Context.tr("Team's external account"), 3);
//                financial.add(external);
//                final HtmlList exAccountInfo = new HtmlList();
//                external.add(exAccountInfo);
//
//                if (exAccount.canAccessAmount()) {
//                    exAccountInfo.add(Context.tr("Money available: {0} ", Context.getLocalizator().getCurrency(exAccount.getAmount()).getDefaultString()));
//                }
//                if (exAccount.canAccessBankCode()) {
//                    exAccountInfo.add(Context.tr("Bank code: {0}", exAccount.getBankCode()));
//                }
//            } catch (final UnauthorizedOperationException e) {
//                // Should never happen
//                Log.web().error("Cannot access to bank external account, I checked just before tho", e);
//            }
//        }
//
//        // Internal account
//        if (targetTeam.canGetInternalAccount()) {
//            try {
//                final InternalAccount inAccount = targetTeam.getInternalAccount();
//                inAccount.authenticate(session.getAuthToken());
//                final HtmlTitleBlock internal = new HtmlTitleBlock(Context.tr("Team's internal account"), 3);
//                financial.add(internal);
//                final HtmlList inAccountInfo = new HtmlList();
//                internal.add(inAccountInfo);
//
//                if (inAccount.canAccessAmount()) {
//                    inAccountInfo.add(Context.tr("Money available: {0} ", Context.getLocalizator().getCurrency(inAccount.getAmount()).getDefaultString()));
//                }
//            } catch (final UnauthorizedOperationException e) {
//                // Should never happen
//                Log.web().error("Cannot access to bank internal account, I checked just before tho", e);
//            }
//        }

        // Members
        final HtmlTitleBlock memberTitle = new HtmlTitleBlock(Context.tr("Members"), 2);
        titleBlock.add(memberTitle);

        if (me != null && me.isInTeam(targetTeam) && me.canSendInvitation(targetTeam)) {
            final SendTeamInvitationPageUrl sendInvitePage = new SendTeamInvitationPageUrl(targetTeam);
            final HtmlLink inviteMember = new HtmlLink(sendInvitePage.urlString(), Context.tr("Invite a member to this team"));
            memberTitle.add(new HtmlParagraph().add(inviteMember));
        }

        final PageIterable<Member> members = targetTeam.getMembers();
        final HtmlTable membersTable = new HtmlTable(new MyTableModel(members));
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
        //TODO: real work
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
                    ph.add(new HtmlImage(new Image(WebConfiguration.getImgValidIcon()), Context.tr("OK"), "team_can"));
                    ph.add(new GiveRightActionUrl(targetTeam, member, right, false).getHtmlLink(Context.tr("Remove")));
                    return ph;
                }
                return new HtmlImage(new Image(WebConfiguration.getImgValidIcon()), Context.tr("OK"), "team_can");
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
