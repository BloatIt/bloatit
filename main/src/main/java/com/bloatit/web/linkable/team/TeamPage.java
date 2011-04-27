package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
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
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.model.visitor.Visitor;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.team.tabs.AccountTab;
import com.bloatit.web.linkable.team.tabs.ActivityTab;
import com.bloatit.web.linkable.team.tabs.MembersTab;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.SideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AccountPageUrl;
import com.bloatit.web.url.ModifyTeamPageUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * Home page for handling teams
 * </p>
 */
@ParamContainer("team")
public final class TeamPage extends MasterPage {
    private final static String TEAM_TAB_PANE = "tab";
    private final static String MEMBERS_TAB = "members";
    private final static String ACTIVITY_TAB = "activity";
    private final static String ACCOUNT_TAB = "account";

    private final TeamPageUrl url;
    @RequestParam(name = "id", conversionErrorMsg = @tr("I cannot find the team number: ''%value%''."))
    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a team number."))
    private final Team targetTeam;

    @RequestParam(name = TEAM_TAB_PANE)
    @Optional(MEMBERS_TAB)
    private String activeTabKey;

    public TeamPage(final TeamPageUrl url) {
        super(url);
        this.url = url;
        this.targetTeam = url.getTargetTeam();
        this.activeTabKey = url.getActiveTabKey();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(false, url);
        final Visitor me = session.getAuthToken().getVisitor();

        layout.addLeft(generateTeamIDCard(me));
        layout.addLeft(generateMain(me));

        layout.addRight(generateSideBar());
        layout.addRight(new SideBarDocumentationBlock("team_role"));
        layout.addRight(new SideBarTeamWithdrawMoneyBlock());

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

    private SideBarElementLayout generateSideBar() {
        final TitleSideBarElementLayout contacts = new TitleSideBarElementLayout();
        contacts.setTitle(Context.tr("How to contact {0}?", targetTeam.getDisplayName()));

        if (targetTeam.canAccessContact(Action.READ)) {
            contacts.add(new HtmlCachedMarkdownRenderer(targetTeam.getContact()));
        } else {
            contacts.add(new HtmlParagraph().addText("No public contact information available"));
        }

        return contacts;
    }

    private HtmlElement generateMain(final Visitor me) {
        final HtmlDiv master = new HtmlDiv("team_tabs");

        final TeamPageUrl url = new TeamPageUrl(targetTeam);
        final HtmlTabBlock tabPane = new HtmlTabBlock(TEAM_TAB_PANE, activeTabKey, url);
        master.add(tabPane);

        tabPane.addTab(new MembersTab(targetTeam, tr("Members"), MEMBERS_TAB));
        tabPane.addTab(new AccountTab(targetTeam, tr("Account"), ACCOUNT_TAB));
        tabPane.addTab(new ActivityTab(targetTeam, tr("Activity"), ACTIVITY_TAB));

        return master;
    }

    /**
     * Generates the team block displaying it's ID
     * 
     * @param me the connected member
     * @return the ID card
     */
    private HtmlElement generateTeamIDCard(final Visitor me) {
        final HtmlDiv master = new HtmlDiv("padding_box");
        if (me.hasModifyTeamRight(targetTeam)) {
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

        // display name
        informationsList.add(new HtmlDefineParagraph(Context.tr("Unique name: "), targetTeam.getLogin()));
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
        final int featuresCount = getActivityCount();
        informationsList.add(new HtmlDefineParagraph(Context.tr("Team's recent activity: "), new HtmlMixedText(Context.tr("{0} (<0::see details>)",
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
                master.add(bankInformations);
                {
                    final HtmlList bankInformationsList = new HtmlList();
                    bankInformations.add(bankInformationsList);

                    // Account balance
                    final MoneyDisplayComponent amount = new MoneyDisplayComponent(targetTeam.getInternalAccount().getAmount(), true, targetTeam);
                    final AccountPageUrl accountPageUrl = new AccountPageUrl();
                    accountPageUrl.setTeam(targetTeam);
                    final HtmlListItem accountBalanceItem = new HtmlListItem(new HtmlDefineParagraph(Context.tr("Account balance: "),
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

        return master;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return TeamPage.generateBreadcrumb(targetTeam);
    }

    public static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamsPage.generateBreadcrumb();
        breadcrumb.pushLink(new TeamPageUrl(team).getHtmlLink(team.getDisplayName()));
        return breadcrumb;
    }

    private int getActivityCount() {
        // TODO: real work
        return 3;
    }

    public static class SideBarTeamWithdrawMoneyBlock extends TitleSideBarElementLayout {

        SideBarTeamWithdrawMoneyBlock() {
            setTitle(tr("Team account"));

            add(new HtmlParagraph(tr("Like users, teams have an elveos account where they can store money.")));
            add(new HtmlParagraph(tr("People with the talk right can decide to make developments under the name of the team to let it earn money.")));
            add(new HtmlParagraph(tr("People with the bank right can withdraw money from the elveos account back to the team bank account.")));
            // TODO good URL
            add(new SideBarButton(tr("Withdraw money"), new PageNotFoundUrl(), WebConfiguration.getImgAccountWithdraw()).asElement());

        }
    }
}
