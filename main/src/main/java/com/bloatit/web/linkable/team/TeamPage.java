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
package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlListItem;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.renderer.HtmlCachedMarkdownRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.money.SideBarLoadAccountBlock;
import com.bloatit.web.linkable.team.tabs.AccountTab;
import com.bloatit.web.linkable.team.tabs.ActivityTab;
import com.bloatit.web.linkable.team.tabs.MembersTab;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.sidebar.SideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ModifyTeamPageUrl;
import com.bloatit.web.url.TeamPageUrl;
import com.bloatit.web.url.WithdrawMoneyPageUrl;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("team")
public final class TeamPage extends ElveosPage {
    public final static String TEAM_TAB_PANE = "tab";
    public final static String MEMBERS_TAB = "members";
    public final static String ACTIVITY_TAB = "activity";
    public final static String ACCOUNT_TAB = "account";

    private final TeamPageUrl url;

    private ActivityTab activity;

    @RequestParam(name = "id", conversionErrorMsg = @tr("I cannot find the team number: ''%value%''."))
    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a team number."))
    private final Team targetTeam;

    @RequestParam(name = TEAM_TAB_PANE)
    @Optional(MEMBERS_TAB)
    private final String activeTabKey;

    @SuppressWarnings("unused")
    @RequestParam(name = "name", role = Role.PRETTY, generatedFrom = "targetTeam")
    @Optional("john-do")
    private final String login;

    public TeamPage(final TeamPageUrl url) {
        super(url);
        this.url = url;
        this.targetTeam = url.getTargetTeam();
        this.activeTabKey = url.getActiveTabKey();
        this.login = url.getLogin();
    }

    @Override
    protected HtmlElement createBodyContent(final ElveosUserToken userToken) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(false, url);

        layout.addLeft(generateTeamIDCard(userToken));
        layout.addLeft(generateMain(userToken));
        
        layout.addRight(generateContactBox());
        layout.addRight(new SideBarDocumentationBlock("team_role"));
        if (userToken.isAuthenticated() && userToken.getMember().hasBankTeamRight(targetTeam)) {
            layout.addRight(new SideBarTeamWithdrawMoneyBlock(targetTeam));
            layout.addRight(new SideBarLoadAccountBlock(targetTeam));
        }
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
        contacts.setTitle(Context.tr("How to contact \"{0}\"?", targetTeam.getDisplayName()));

        if (targetTeam.canAccessContact(Action.READ)) {
            contacts.add(new HtmlCachedMarkdownRenderer(targetTeam.getContact()));
        } else {
            contacts.add(new HtmlParagraph().addText("No public contact information available"));
        }

        return contacts;
    }

    private HtmlElement generateMain(final ElveosUserToken userToken) {
        final HtmlDiv master = new HtmlDiv("team_tabs");

        final TeamPageUrl secondUrl = new TeamPageUrl(targetTeam);

        String tabKey = activeTabKey;

        if (activeTabKey.equals(ACCOUNT_TAB) && !targetTeam.canAccessBankTransaction(Action.READ)) {
            tabKey = MEMBERS_TAB;
        }
        final HtmlTabBlock tabPane = new HtmlTabBlock(TEAM_TAB_PANE, tabKey, secondUrl);

        master.add(tabPane);

        if (userToken.isAuthenticated()) {
            tabPane.addTab(new MembersTab(targetTeam, tr("Members"), MEMBERS_TAB, userToken.getMember()));
        }
        if (targetTeam.canAccessBankTransaction(Action.READ)) {
            tabPane.addTab(new AccountTab(targetTeam, tr("Account"), ACCOUNT_TAB));
        }
        activity = new ActivityTab(targetTeam, tr("Activity"), ACTIVITY_TAB, url);
        tabPane.addTab(activity);

        return master;
    }

    /**
     * Generates the team block displaying it's ID
     * 
     * @param me the connected member
     * @return the ID card
     */
    private HtmlElement generateTeamIDCard(final ElveosUserToken token) {
        final HtmlDiv master = new HtmlDiv("padding_box");
        if (token.isAuthenticated() && token.getMember().hasModifyTeamRight(targetTeam)) {
            // Link to change account settings
            final HtmlDiv modify = new HtmlDiv("float_right");
            master.add(modify);
            modify.add(new ModifyTeamPageUrl(targetTeam).getHtmlLink(Context.tr("Change team settings")));
        }

        // Title and team type
        HtmlTitleBlock titleBlock;
        titleBlock = new HtmlTitleBlock(targetTeam.getDisplayName(), 1);
        master.add(titleBlock);

        // Avatar
        titleBlock.add(new HtmlDiv("float_left").add(TeamTools.getTeamAvatar(targetTeam)));

        // Team informations
        final HtmlList informationsList = new HtmlList();

        // Visibility
        informationsList.add(new HtmlDefineParagraph(Context.tr("Visibility: "), (targetTeam.isPublic() ? Context.tr("Public")
                : Context.tr("Private"))));

        // Creation date
        try {
            informationsList.add(new HtmlDefineParagraph(Context.tr("Creation date: "), Context.getLocalizator()
                                                                                               .getDate(targetTeam.getDateCreation())
                                                                                               .toString(FormatStyle.LONG)));
        } catch (final UnauthorizedOperationException e) {
            // Should never happen
            Log.web().error("Not allowed to see team creation date in team page, should not happen", e);
        }

        // Member count
        informationsList.add(new HtmlDefineParagraph(Context.tr("Number of members: "), String.valueOf(targetTeam.getMembers().size())));

        // Features count
        final long featuresCount = getActivityCount();
        final TeamPageUrl activityPage = new TeamPageUrl(targetTeam);
        activityPage.setActiveTabKey(ACTIVITY_TAB);
        final HtmlMixedText mixed = new HtmlMixedText(Context.tr("{0} (<0::see details>)", featuresCount), activityPage.getHtmlLink());
        informationsList.add(new HtmlDefineParagraph(Context.tr("Team's recent activity: "), mixed));
        titleBlock.add(informationsList);

        // Description
        final HtmlTitleBlock description = new HtmlTitleBlock(Context.tr("Description"), 2);
        titleBlock.add(description);
        final HtmlCachedMarkdownRenderer hcmr = new HtmlCachedMarkdownRenderer(targetTeam.getDescription());
        description.add(hcmr);

        // Bank informations
        if (token.isAuthenticated()) {
            addBankInfos(master, token.getMember());
        }

        return master;
    }

    protected void addBankInfos(final HtmlDiv master, final Member member) {
        if (targetTeam.canGetInternalAccount() && targetTeam.canGetExternalAccount()) {
            try {
                final HtmlTitleBlock bankInformations = new HtmlTitleBlock(Context.tr("Bank informations"), 2);
                master.add(bankInformations);
                {
                    final HtmlList bankInformationsList = new HtmlList();
                    bankInformations.add(bankInformationsList);

                    // Account balance
                    final MoneyDisplayComponent amount = new MoneyDisplayComponent(targetTeam.getInternalAccount().getAmount(),
                                                                                   true,
                                                                                   targetTeam,
                                                                                   member);
                    final HtmlListItem accountBalanceItem = new HtmlListItem(new HtmlDefineParagraph(Context.tr("Account balance: "),
                                                                                                     new HtmlMixedText(Context.tr("<0:amount (1000€):> (<1::view details>)"),
                                                                                                                       amount,
                                                                                                                       AccountUrl(targetTeam).getHtmlLink())));
                    bankInformationsList.add(accountBalanceItem);

                }
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Cannot access to bank informations, should not happen", e);
            }
        }
    }

    @Override
    protected Breadcrumb createBreadcrumb(final ElveosUserToken userToken) {
        return TeamPage.generateBreadcrumb(targetTeam);
    }

    public static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamsPage.generateBreadcrumb();
        breadcrumb.pushLink(new TeamPageUrl(team).getHtmlLink(team.getDisplayName()));
        return breadcrumb;
    }

    public static Breadcrumb generateAccountBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamPage.generateBreadcrumb(team);
        final TeamPageUrl teamPageUrl = new TeamPageUrl(team);
        teamPageUrl.setActiveTabKey(ACCOUNT_TAB);
        breadcrumb.pushLink(teamPageUrl.getHtmlLink(Context.tr("Account")));
        return breadcrumb;
    }

    private long getActivityCount() {
        return targetTeam.getRecentActivityCount();
    }

    private static class SideBarTeamWithdrawMoneyBlock extends TitleSideBarElementLayout {
        SideBarTeamWithdrawMoneyBlock(final Team team) {
            setTitle(tr("Team account"));

            add(new HtmlParagraph(tr("Like users, teams have an elveos account where they can store money.")));
            add(new HtmlParagraph(tr("People with the talk right can decide to make developments under the name of the team to let it earn money.")));
            add(new HtmlParagraph(tr("People with the bank right can withdraw money from the elveos account back to the team bank account.")));
            add(new SideBarButton(tr("Withdraw money"), new WithdrawMoneyPageUrl(team), WebConfiguration.getImgAccountWithdraw()).asElement());

        }
    }

    public static TeamPageUrl AccountUrl(final Team team) {
        final TeamPageUrl teamPageUrl = new TeamPageUrl(team);
        teamPageUrl.setActiveTabKey(ACCOUNT_TAB);
        teamPageUrl.setAnchor(TEAM_TAB_PANE);
        return teamPageUrl;
    }
}
