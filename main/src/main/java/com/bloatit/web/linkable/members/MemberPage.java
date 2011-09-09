/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.members;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.SubParamContainer;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.renderer.HtmlMarkdownRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.components.InvoicingContactTab;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.members.tabs.AccountTab;
import com.bloatit.web.linkable.members.tabs.ActivityTab;
import com.bloatit.web.linkable.members.tabs.DashboardTab;
import com.bloatit.web.linkable.members.tabs.TasksTab;
import com.bloatit.web.linkable.money.SideBarLoadAccountBlock;
import com.bloatit.web.linkable.money.SideBarWithdrawMoneyBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.ModifyMemberPageUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * A page used to display member information.
 * </p>
 * <p>
 * If the consulted member is the same as the logged member, then this page will
 * propose to edit account parameters
 * </p>
 */
@ParamContainer("members/%member%")
public final class MemberPage extends ElveosPage {
    private final MemberPageUrl url;

    public final static String MEMBER_TAB_PANE = "tab";
    public final static String TASKS_TAB = "tasks";
    public final static String ACTIVITY_TAB = "activity";
    public final static String ACCOUNT_TAB = "account";
    public final static String INVOICING_TAB = "invoicing";
    public final static String DASHBOARD_TAB = "dashboard";

    @SubParamContainer
    private ActivityTab activity;

    @SubParamContainer
    private DashboardTab dashboard;

    @RequestParam(name = MEMBER_TAB_PANE)
    @Optional(DASHBOARD_TAB)
    private final String activeTabKey;

    @NonOptional(@tr("You have to specify a member number."))
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the member number: ''%value%''."))
    private final Member member;

    public MemberPage(final MemberPageUrl url) {
        super(url);
        this.url = url;
        this.member = url.getMember();
        this.activeTabKey = url.getActiveTabKey();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(false, url);

        boolean myPage;
        if (AuthToken.isAuthenticated() && member != null && member.equals(AuthToken.getMember())) {
            myPage = true;
        } else {
            myPage = false;
        }

        layout.addLeft(generateMemberPageMain(myPage));

        if (member.canGetInternalAccount()) {
            layout.addLeft(generateTabPane());
        }

        // Adding list of teams
        final TitleSideBarElementLayout teamBlock = new TitleSideBarElementLayout();
        try {
            if (myPage) {
                teamBlock.setTitle(Context.tr("My teams"));
            } else {
                teamBlock.setTitle(Context.tr("{0}''s teams", member.getDisplayName()));
            }

            final HtmlList teamList = new HtmlList();
            teamList.setCssClass("member_teams_list");
            teamBlock.add(teamList);

            final PageIterable<Team> teams = member.getTeams();

            if (teams.size() == 0) {
                teamBlock.add(new HtmlParagraph(Context.tr("No team.")));
            }

            for (final Team team : teams) {
                teamList.add(new TeamPageUrl(team).getHtmlLink(team.getDisplayName()));
            }
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Cannot access member team information", e);
        }
        layout.addRight(teamBlock);
        if (AuthToken.isAuthenticated() && member.canGetInternalAccount()) {
            layout.addRight(new SideBarDocumentationBlock("internal_account"));
            layout.addRight(new SideBarLoadAccountBlock(null));
            layout.addRight(new SideBarWithdrawMoneyBlock(AuthToken.getMember()));
        }

        return layout;
    }

    private HtmlElement generateMemberPageMain(final boolean myPage) {
        final HtmlDiv master = new HtmlDiv("member_page");

        if (member.canAccessUserInformations(Action.WRITE)) {
            // Link to change account settings
            final HtmlDiv modify = new HtmlDiv("float_right");
            master.add(modify);
            modify.add(new ModifyMemberPageUrl().getHtmlLink(Context.tr("Change member settings")));
        }

        // Title
        final String title = (myPage) ? Context.tr("My page") : Context.tr("{0}''s page", member.getDisplayName());
        final HtmlTitleBlock tBlock = new HtmlTitleBlock(title, 1);
        master.add(tBlock);

        final HtmlDiv main = new HtmlDiv("member");
        master.add(main);

        // Member ID card
        final HtmlDiv memberId = new HtmlDiv("member_id");

        // Avatar
        final HtmlDiv avatarDiv = new HtmlDiv("float_left");
        avatarDiv.add(MembersTools.getMemberAvatar(member));
        memberId.add(avatarDiv);
        main.add(memberId);

        try {
            final HtmlList memberIdList = new HtmlList();
            memberId.add(memberIdList);

            if (member.canAccessUserInformations(Action.READ)) {
                // Login
                final HtmlSpan login = new HtmlSpan("id_category");
                login.addText(Context.trc("login (noun)", "Login: "));
                memberIdList.add(new PlaceHolderElement().add(login).addText(member.getLogin()));

                // Fullname
                final HtmlSpan fullname = new HtmlSpan("id_category");
                fullname.addText(Context.tr("Fullname: "));
                if (member.getFullname() != null) {
                    memberIdList.add(new PlaceHolderElement().add(fullname).addText(member.getFullname()));
                } else {
                    memberIdList.add(new PlaceHolderElement().add(fullname));
                }

                // Email
                final HtmlSpan email = new HtmlSpan("id_category");
                email.addText(Context.tr("Email: "));
                memberIdList.add(new PlaceHolderElement().add(email).addText(member.getEmail()));

            } else {
                final HtmlSpan name = new HtmlSpan("id_category");
                name.addText(Context.tr("Name: "));
                memberIdList.add(new PlaceHolderElement().add(name).addText(member.getDisplayName()));
            }

            final Locale userLocale = Context.getLocalizator().getLocale();
            // Country
            final HtmlSpan country = new HtmlSpan("id_category");
            country.addText(Context.tr("Country: "));
            memberIdList.add(new PlaceHolderElement().add(country).addText(member.getLocale().getDisplayCountry(userLocale)));

            // Language
            final HtmlSpan language = new HtmlSpan("id_category");
            language.addText(Context.tr("Language: "));
            memberIdList.add(new PlaceHolderElement().add(language).addText(member.getLocale().getDisplayLanguage(userLocale)));

            // Karma
            final HtmlSpan karma = new HtmlSpan("id_category");
            karma.addText(Context.tr("Karma: "));
            memberIdList.add(new PlaceHolderElement().add(karma).addText("" + member.getKarma()));

            if (member.getDescription() != null) {
                HtmlBranch memberDescription = new HtmlDiv("member_description").add(new HtmlMarkdownRenderer(member.getDescription()));
                memberId.add(memberDescription);
            }

        } catch (final UnauthorizedOperationException e) {
            getSession().notifyError("An error prevented us from displaying user information. Please notify us.");
            throw new ShallNotPassException("Error while gathering user information", e);
        }

        if (!member.canGetInternalAccount()) {
            // Displaying list of user recent activity
            final HtmlTitleBlock recent = new HtmlTitleBlock(Context.tr("Recent activity"), 2);
            main.add(recent);
            recent.add(ActivityTab.generateActivities(member, url));
        }

        return master;
    }

    private HtmlElement generateTabPane() {
        final HtmlDiv master = new HtmlDiv("member_tabs");

        final MemberPageUrl secondUrl = new MemberPageUrl(member);
        final HtmlTabBlock tabPane = new HtmlTabBlock(MEMBER_TAB_PANE, activeTabKey, secondUrl);
        master.add(tabPane);

        // Dashboard tab
        dashboard = new DashboardTab(member, tr("Dashboard"), DASHBOARD_TAB, url);
        tabPane.addTab(dashboard);

        // Activity tab
        activity = new ActivityTab(member, tr("Activity"), ACTIVITY_TAB, url);
        tabPane.addTab(activity);
        
        // Account tab
        tabPane.addTab(new AccountTab(member, tr("Account"), ACCOUNT_TAB));
        long nb;
        if ((nb = (member.getInvitationCount() + member.getMilestoneToInvoice().size())) > 0) {
            tabPane.addTab(new TasksTab(member, tr("Tasks&nbsp;({0})", nb), TASKS_TAB));
        }

        // Invoicing contact tab
        tabPane.addTab(new InvoicingContactTab(member, tr("Invoicing"), INVOICING_TAB));

        return master;
    }

    @Override
    protected String createPageTitle() {
        return tr("Member - ") + member.getDisplayName();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return MemberPage.generateBreadcrumb(member);
    }

    public static Breadcrumb generateBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = MembersListPage.generateBreadcrumb();
        breadcrumb.pushLink(new MemberPageUrl(member).getHtmlLink(member.getDisplayName()));
        return breadcrumb;
    }

    public static Breadcrumb generateAccountBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(member);
        final MemberPageUrl memberPageUrl = new MemberPageUrl(member);
        memberPageUrl.setActiveTabKey(ACCOUNT_TAB);
        breadcrumb.pushLink(memberPageUrl.getHtmlLink(Context.tr("Account")));
        return breadcrumb;
    }

    public static MemberPageUrl accountUrl(final Member member) {
        final MemberPageUrl memberPageUrl = new MemberPageUrl(member);
        memberPageUrl.setActiveTabKey(ACCOUNT_TAB);
        // memberPageUrl.setAnchor(MEMBER_TAB_PANE);
        return memberPageUrl;
    }

    public static MemberPageUrl myAccountUrl(final Member member) {
        return accountUrl(member);
    }

    public static MemberPageUrl myMessagesUrl(final Member member) {
        final MemberPageUrl memberPageUrl = new MemberPageUrl(member);
        memberPageUrl.setActiveTabKey(TASKS_TAB);
        // memberPageUrl.setAnchor(MEMBER_TAB_PANE);
        return memberPageUrl;
    }
}
