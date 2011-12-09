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
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.SubParamContainer;
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
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlFollowButton.HtmlFollowActorButton;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.HtmlDefineParagraph;
import com.bloatit.web.linkable.master.sidebar.SideBarElementLayout;
import com.bloatit.web.linkable.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.members.tabs.InvoicingContactTab;
import com.bloatit.web.linkable.money.SideBarLoadAccountBlock;
import com.bloatit.web.linkable.team.tabs.AccountTab;
import com.bloatit.web.linkable.team.tabs.HistoryTab;
import com.bloatit.web.linkable.team.tabs.MembersTab;
import com.bloatit.web.url.ModifyTeamPageUrl;
import com.bloatit.web.url.TeamPageUrl;
import com.bloatit.web.url.WithdrawMoneyPageUrl;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("teams/%team%")
public final class TeamPage extends ElveosPage {
    public final static String TEAM_TAB_PANE = "tab";
    public final static String MEMBERS_TAB = "members";
    public final static String ACTIVITY_TAB = "history";
    public final static String ACCOUNT_TAB = "account";
    public final static String INVOICING_TAB = "invoicing";

    private final TeamPageUrl url;

    @SubParamContainer
    private HistoryTab history;

    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the team number: ''%value%''."))
    @NonOptional(@tr("You have to specify a team number."))
    private final Team team;

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
        this.team = url.getTeam();
        this.activeTabKey = url.getActiveTabKey();
        this.login = url.getLogin();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(false, url);

        layout.addLeft(generateTeamIDCard());
        layout.addLeft(generateMain());

        layout.addRight(generateContactBox());
        layout.addRight(new SideBarDocumentationBlock("team_role"));
        if (AuthToken.isAuthenticated() && AuthToken.getMember().hasBankTeamRight(team)) {
            layout.addRight(new SideBarTeamWithdrawMoneyBlock(team));
            layout.addRight(new SideBarLoadAccountBlock(team));
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
        contacts.setTitle(Context.tr("How to contact \"{0}\"?", team.getDisplayName()));

        if (team.canAccessContact(Action.READ)) {
            contacts.add(new HtmlCachedMarkdownRenderer(team.getPublicContact()));
        } else {
            contacts.add(new HtmlParagraph().addText("No public contact information available"));
        }

        return contacts;
    }

    private HtmlElement generateMain() {
        final HtmlDiv master = new HtmlDiv("team_tabs");

        final TeamPageUrl secondUrl = new TeamPageUrl(team);

        String tabKey = activeTabKey;

        if (activeTabKey.equals(ACCOUNT_TAB) && !team.canAccessBankTransaction(Action.READ)) {
            tabKey = MEMBERS_TAB;
        }
        final HtmlTabBlock tabPane = new HtmlTabBlock(TEAM_TAB_PANE, tabKey, secondUrl);

        master.add(tabPane);

        tabPane.addTab(new MembersTab(team, tr("Members"), MEMBERS_TAB, AuthToken.getMember()));
        if (team.canAccessBankTransaction(Action.READ)) {
            tabPane.addTab(new AccountTab(team, tr("Account"), ACCOUNT_TAB));
            tabPane.addTab(new InvoicingContactTab(team, tr("Invoicing"), INVOICING_TAB));
        }
        history = new HistoryTab(team, tr("History"), ACTIVITY_TAB, url);
        tabPane.addTab(history);

        return master;
    }

    /**
     * Generates the team block displaying it's ID
     *
     * @param me the connected member
     * @return the ID card
     */
    private HtmlElement generateTeamIDCard() {
        final HtmlDiv master = new HtmlDiv("padding_box");
        if (AuthToken.isAuthenticated() && AuthToken.getMember().hasModifyTeamRight(team)) {
            // Link to change account settings
            final HtmlDiv modify = new HtmlDiv("float_right");
            master.add(modify);
            modify.add(new ModifyTeamPageUrl(team).getHtmlLink(Context.tr("Change team settings")));
            //modify.add(new HtmlFollowActorButton(team));
        }

        // Title and team type
        HtmlTitleBlock titleBlock;
        titleBlock = new HtmlTitleBlock(team.getDisplayName(), 1);
        master.add(titleBlock);

        // Avatar
        titleBlock.add(new HtmlDiv("float_left").add(TeamTools.getTeamAvatar(team)));

        // Team informations
        final HtmlList informationsList = new HtmlList();

        // Visibility
        informationsList.add(new HtmlDefineParagraph(Context.tr("Membership: "), (team.isPublic() ? Context.tr("Open to all") : Context.tr("By invitation"))));

        // Creation date
        try {
            informationsList.add(new HtmlDefineParagraph(Context.tr("Creation date: "), Context.getLocalizator()
                                                                                               .getDate(team.getDateCreation())
                                                                                               .toString(FormatStyle.LONG)));
        } catch (final UnauthorizedOperationException e) {
            // Should never happen
            Log.web().error("Not allowed to see team creation date in team page, should not happen", e);
        }

        // Member count
        informationsList.add(new HtmlDefineParagraph(Context.tr("Number of members: "), String.valueOf(team.getMembers().size())));

        // Features count
        final long featuresCount = getHistoryCount();
        final TeamPageUrl historyPage = new TeamPageUrl(team);
        historyPage.setActiveTabKey(ACTIVITY_TAB);
        final HtmlMixedText mixed = new HtmlMixedText(Context.tr("{0} (<0::see details>)", featuresCount), historyPage.getHtmlLink());
        informationsList.add(new HtmlDefineParagraph(Context.tr("Team's recent history: "), mixed));
        titleBlock.add(informationsList);

        // Description
        final HtmlTitleBlock description = new HtmlTitleBlock(Context.tr("Description"), 2);
        titleBlock.add(description);
        final HtmlCachedMarkdownRenderer hcmr = new HtmlCachedMarkdownRenderer(team.getDescription());
        description.add(hcmr);

        // Bank informations
        if (AuthToken.isAuthenticated()) {
            addBankInfos(master, AuthToken.getMember());
        }

        return master;
    }

    protected void addBankInfos(final HtmlDiv master, final Member member) {
        if (team.canGetInternalAccount() && team.canGetExternalAccount()) {
            try {
                final HtmlTitleBlock bankInformations = new HtmlTitleBlock(Context.tr("Bank informations"), 2);
                master.add(bankInformations);
                {
                    final HtmlList bankInformationsList = new HtmlList();
                    bankInformations.add(bankInformationsList);

                    // Account balance
                    final MoneyDisplayComponent amount = new MoneyDisplayComponent(team.getInternalAccount().getAmount(),
                                                                                   true,
                                                                                   team,
                                                                                   member,
                                                                                   Context.getLocalizator());
                    final HtmlListItem accountBalanceItem = new HtmlListItem(new HtmlDefineParagraph(Context.tr("Account balance: "),
                                                                                                     new HtmlMixedText(Context.tr("<0:amount (1000€):> (<1::view details>)"),
                                                                                                                       amount,
                                                                                                                       AccountUrl(team).getHtmlLink())));
                    bankInformationsList.add(accountBalanceItem);

                }
            } catch (final UnauthorizedOperationException e) {
                // Should never happen
                Log.web().error("Cannot access to bank informations, should not happen", e);
            }
        }
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return TeamPage.generateBreadcrumb(team);
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

    private long getHistoryCount() {
        return team.getRecentHistoryCount();
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
