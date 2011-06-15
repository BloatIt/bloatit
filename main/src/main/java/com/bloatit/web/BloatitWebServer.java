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
package com.bloatit.web;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.WebProcessor;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.webprocessor.context.SessionManager;
import com.bloatit.framework.webprocessor.masters.Linkable;
import com.bloatit.framework.webprocessor.url.PageForbiddenUrl;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.web.actions.AddAttachementAction;
import com.bloatit.web.actions.AddAttachementPage;
import com.bloatit.web.actions.CreateCommentAction;
import com.bloatit.web.actions.PopularityVoteAction;
import com.bloatit.web.linkable.admin.AdminHomePage;
import com.bloatit.web.linkable.admin.AdministrationAction;
import com.bloatit.web.linkable.admin.ConfigurationAdminAction;
import com.bloatit.web.linkable.admin.ConfigurationAdminPage;
import com.bloatit.web.linkable.admin.DeclareHightlightedFeatureAction;
import com.bloatit.web.linkable.admin.FeatureAdminPage;
import com.bloatit.web.linkable.admin.HightlightedFeatureAdminPage;
import com.bloatit.web.linkable.admin.KudosableAdminPageImplementation;
import com.bloatit.web.linkable.admin.MilestoneAdminPage;
import com.bloatit.web.linkable.admin.UserContentAdminPageImplementation;
import com.bloatit.web.linkable.admin.exception.ExceptionAdministrationAction;
import com.bloatit.web.linkable.admin.exception.ExceptionAdministrationPage;
import com.bloatit.web.linkable.admin.notify.AdminGlobalNotificationAction;
import com.bloatit.web.linkable.admin.notify.AdminGlobalNotificationPage;
import com.bloatit.web.linkable.admin.withdraw.MoneyWithdrawalAdminAction;
import com.bloatit.web.linkable.admin.withdraw.MoneyWithdrawalAdminPage;
import com.bloatit.web.linkable.bugs.BugPage;
import com.bloatit.web.linkable.bugs.ModifyBugAction;
import com.bloatit.web.linkable.bugs.ModifyBugPage;
import com.bloatit.web.linkable.bugs.ReportBugAction;
import com.bloatit.web.linkable.bugs.ReportBugPage;
import com.bloatit.web.linkable.contribution.CheckContributionAction;
import com.bloatit.web.linkable.contribution.CheckContributionPage;
import com.bloatit.web.linkable.contribution.ContributePage;
import com.bloatit.web.linkable.contribution.ContributionAction;
import com.bloatit.web.linkable.contribution.ContributionProcess;
import com.bloatit.web.linkable.contribution.StaticCheckContributionPage;
import com.bloatit.web.linkable.contribution.UnlockContributionProcessAction;
import com.bloatit.web.linkable.errors.PageForbidden;
import com.bloatit.web.linkable.errors.PageNotFound;
import com.bloatit.web.linkable.features.CreateFeatureAction;
import com.bloatit.web.linkable.features.CreateFeaturePage;
import com.bloatit.web.linkable.features.FeatureListPage;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.invoice.ContributionInvoicingInformationsPage;
import com.bloatit.web.linkable.invoice.ContributionInvoicingProcess;
import com.bloatit.web.linkable.invoice.InvoiceResource;
import com.bloatit.web.linkable.invoice.ModifyContactPage;
import com.bloatit.web.linkable.invoice.ModifyInvoicingContactAction;
import com.bloatit.web.linkable.invoice.ModifyInvoicingContactProcess;
import com.bloatit.web.linkable.language.ChangeLanguageAction;
import com.bloatit.web.linkable.language.ChangeLanguagePage;
import com.bloatit.web.linkable.login.LoginAction;
import com.bloatit.web.linkable.login.LoginPage;
import com.bloatit.web.linkable.login.LogoutAction;
import com.bloatit.web.linkable.login.LostPasswordAction;
import com.bloatit.web.linkable.login.LostPasswordPage;
import com.bloatit.web.linkable.login.MemberActivationAction;
import com.bloatit.web.linkable.login.RecoverPasswordAction;
import com.bloatit.web.linkable.login.RecoverPasswordPage;
import com.bloatit.web.linkable.login.SignUpAction;
import com.bloatit.web.linkable.login.SignUpPage;
import com.bloatit.web.linkable.members.ChangeAvatarAction;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.members.MembersListPage;
import com.bloatit.web.linkable.members.ModifyMemberAction;
import com.bloatit.web.linkable.members.ModifyMemberPage;
import com.bloatit.web.linkable.meta.bugreport.MetaBugDeleteAction;
import com.bloatit.web.linkable.meta.bugreport.MetaBugEditPage;
import com.bloatit.web.linkable.meta.bugreport.MetaBugsListPage;
import com.bloatit.web.linkable.meta.bugreport.MetaEditBugAction;
import com.bloatit.web.linkable.meta.bugreport.MetaReportBugAction;
import com.bloatit.web.linkable.money.AccountChargingPage;
import com.bloatit.web.linkable.money.AccountChargingProcess;
import com.bloatit.web.linkable.money.CancelWithdrawMoneyAction;
import com.bloatit.web.linkable.money.PaylineAction;
import com.bloatit.web.linkable.money.PaylineNotifyAction;
import com.bloatit.web.linkable.money.PaylineProcess;
import com.bloatit.web.linkable.money.PaylineReturnAction;
import com.bloatit.web.linkable.money.StaticAccountChargingPage;
import com.bloatit.web.linkable.money.UnlockAccountChargingProcessAction;
import com.bloatit.web.linkable.money.WithdrawMoneyAction;
import com.bloatit.web.linkable.money.WithdrawMoneyPage;
import com.bloatit.web.linkable.offer.MakeOfferPage;
import com.bloatit.web.linkable.offer.OfferAction;
import com.bloatit.web.linkable.release.AddReleaseAction;
import com.bloatit.web.linkable.release.AddReleasePage;
import com.bloatit.web.linkable.release.ReleasePage;
import com.bloatit.web.linkable.softwares.AddSoftwareAction;
import com.bloatit.web.linkable.softwares.AddSoftwarePage;
import com.bloatit.web.linkable.softwares.SoftwareListPage;
import com.bloatit.web.linkable.softwares.SoftwarePage;
import com.bloatit.web.linkable.team.CreateTeamAction;
import com.bloatit.web.linkable.team.CreateTeamPage;
import com.bloatit.web.linkable.team.GiveRightAction;
import com.bloatit.web.linkable.team.HandleJoinTeamInvitationAction;
import com.bloatit.web.linkable.team.JoinTeamAction;
import com.bloatit.web.linkable.team.ModifyTeamAction;
import com.bloatit.web.linkable.team.ModifyTeamPage;
import com.bloatit.web.linkable.team.SendTeamInvitationAction;
import com.bloatit.web.linkable.team.SendTeamInvitationPage;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.linkable.team.TeamsPage;
import com.bloatit.web.pages.CommentReplyPage;
import com.bloatit.web.pages.DocumentationPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.SiteMapPage;
import com.bloatit.web.pages.TestPage;
import com.bloatit.web.url.*;

public class BloatitWebServer extends WebProcessor {

    public BloatitWebServer() {
        super();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Linkable constructLinkable(final String pageCode, final Parameters params, final Session session) {

        // Pages
        if (PageForbiddenUrl.matches(pageCode)) {
            return new PageForbidden(new PageForbiddenUrl());
        }
        if (IndexPageUrl.matches(pageCode)) {
            return new IndexPage(new IndexPageUrl(params, session.getParameters()));
        }
        if (LoginPageUrl.matches(pageCode)) {
            return new LoginPage(new LoginPageUrl(params, session.getParameters()));
        }
        if (FeatureListPageUrl.matches(pageCode)) {
            return new FeatureListPage(new FeatureListPageUrl(params, session.getParameters()));
        }
        if (CreateFeaturePageUrl.matches(pageCode)) {
            return new CreateFeaturePage(new CreateFeaturePageUrl(params, session.getParameters()));
        }
        if (FeaturePageUrl.matches(pageCode)) {
            return new FeaturePage(new FeaturePageUrl(params, session.getParameters()));
        }
        if (SiteMapPageUrl.matches(pageCode)) {
            return new SiteMapPage(new SiteMapPageUrl(params, session.getParameters()));
        }
        if (MembersListPageUrl.matches(pageCode)) {
            return new MembersListPage(new MembersListPageUrl(params, session.getParameters()));
        }
        if (MemberPageUrl.matches(pageCode)) {
            return new MemberPage(new MemberPageUrl(params, session.getParameters()));
        }
        if (ContributePageUrl.matches(pageCode)) {
            return new ContributePage(new ContributePageUrl(params, session.getParameters()));
        }
        if (CheckContributionPageUrl.matches(pageCode)) {
            return new CheckContributionPage(new CheckContributionPageUrl(params, session.getParameters()));
        }
        if (StaticCheckContributionPageUrl.matches(pageCode)) {
            return new StaticCheckContributionPage(new StaticCheckContributionPageUrl(params, session.getParameters()));
        }
        if (MakeOfferPageUrl.matches(pageCode)) {
            return new MakeOfferPage(new MakeOfferPageUrl(params, session.getParameters()));
        }
        if (TestPageUrl.matches(pageCode)) {
            return new TestPage(new TestPageUrl(params, session.getParameters()));
        }
        if (AccountChargingPageUrl.matches(pageCode)) {
            return new AccountChargingPage(new AccountChargingPageUrl(params, session.getParameters()));
        }
        if (SignUpPageUrl.matches(pageCode)) {
            return new SignUpPage(new SignUpPageUrl(params, session.getParameters()));
        }
        if (CommentReplyPageUrl.matches(pageCode)) {
            return new CommentReplyPage(new CommentReplyPageUrl(params, session.getParameters()));
        }
        if (SoftwarePageUrl.matches(pageCode)) {
            return new SoftwarePage(new SoftwarePageUrl(params, session.getParameters()));
        }
        if (AddSoftwarePageUrl.matches(pageCode)) {
            return new AddSoftwarePage(new AddSoftwarePageUrl(params, session.getParameters()));
        }
        if (SoftwareListPageUrl.matches(pageCode)) {
            return new SoftwareListPage(new SoftwareListPageUrl(params, session.getParameters()));
        }
        if (UserContentAdminPageUrl.matches(pageCode)) {
            return new UserContentAdminPageImplementation(new UserContentAdminPageUrl(params, session.getParameters()));
        }
        if (DocumentationPageUrl.matches(pageCode)) {
            return new DocumentationPage(new DocumentationPageUrl(params, session.getParameters()));
        }
        if (TeamsPageUrl.matches(pageCode)) {
            return new TeamsPage(new TeamsPageUrl(params, session.getParameters()));
        }
        if (TeamPageUrl.matches(pageCode)) {
            return new TeamPage(new TeamPageUrl(params, session.getParameters()));
        }
        if (CreateTeamPageUrl.matches(pageCode)) {
            return new CreateTeamPage(new CreateTeamPageUrl(params, session.getParameters()));
        }
        if (SendTeamInvitationPageUrl.matches(pageCode)) {
            return new SendTeamInvitationPage(new SendTeamInvitationPageUrl(params, session.getParameters()));
        }
        if (KudosableAdminPageUrl.matches(pageCode)) {
            return new KudosableAdminPageImplementation(new KudosableAdminPageUrl(params, session.getParameters()));
        }
        if (FeatureAdminPageUrl.matches(pageCode)) {
            return new FeatureAdminPage(new FeatureAdminPageUrl(params, session.getParameters()));
        }
        if (BugPageUrl.matches(pageCode)) {
            return new BugPage(new BugPageUrl(params, session.getParameters()));
        }
        if (ReportBugPageUrl.matches(pageCode)) {
            return new ReportBugPage(new ReportBugPageUrl(params, session.getParameters()));
        }
        if (AddReleasePageUrl.matches(pageCode)) {
            return new AddReleasePage(new AddReleasePageUrl(params, session.getParameters()));
        }
        if (ModifyBugPageUrl.matches(pageCode)) {
            return new ModifyBugPage(new ModifyBugPageUrl(params, session.getParameters()));
        }
        if (ReleasePageUrl.matches(pageCode)) {
            return new ReleasePage(new ReleasePageUrl(params, session.getParameters()));
        }
        if (MilestoneAdminPageUrl.matches(pageCode)) {
            return new MilestoneAdminPage(new MilestoneAdminPageUrl(params, session.getParameters()));
        }
        if (MetaBugsListPageUrl.matches(pageCode)) {
            return new MetaBugsListPage(new MetaBugsListPageUrl(params, session.getParameters()));
        }
        if (MetaBugEditPageUrl.matches(pageCode)) {
            return new MetaBugEditPage(new MetaBugEditPageUrl(params, session.getParameters()));
        }
        if (ConfigurationAdminPageUrl.matches(pageCode)) {
            return new ConfigurationAdminPage(new ConfigurationAdminPageUrl(params, session.getParameters()));
        }
        if (AdminHomePageUrl.matches(pageCode)) {
            return new AdminHomePage(new AdminHomePageUrl(params, session.getParameters()));
        }
        if (HightlightedFeatureAdminPageUrl.matches(pageCode)) {
            return new HightlightedFeatureAdminPage(new HightlightedFeatureAdminPageUrl(params, session.getParameters()));
        }
        if (ChangeLanguagePageUrl.matches(pageCode)) {
            return new ChangeLanguagePage(new ChangeLanguagePageUrl(params, session.getParameters()));
        }
        if (ModifyMemberPageUrl.matches(pageCode)) {
            return new ModifyMemberPage(new ModifyMemberPageUrl(params, session.getParameters()));
        }
        if (LostPasswordPageUrl.matches(pageCode)) {
            return new LostPasswordPage(new LostPasswordPageUrl(params, session.getParameters()));
        }
        if (RecoverPasswordPageUrl.matches(pageCode)) {
            return new RecoverPasswordPage(new RecoverPasswordPageUrl(params, session.getParameters()));
        }
        if (ModifyTeamPageUrl.matches(pageCode)) {
            return new ModifyTeamPage(new ModifyTeamPageUrl(params, session.getParameters()));
        }
        if (WithdrawMoneyPageUrl.matches(pageCode)) {
            return new WithdrawMoneyPage(new WithdrawMoneyPageUrl(params, session.getParameters()));
        }
        if (MoneyWithdrawalAdminPageUrl.matches(pageCode)) {
            return new MoneyWithdrawalAdminPage(new MoneyWithdrawalAdminPageUrl(params, session.getParameters()));
        }
        if (ModifyContactPageUrl.matches(pageCode)) {
            return new ModifyContactPage(new ModifyContactPageUrl(params, session.getParameters()));
        }
        if (ExceptionAdministrationPageUrl.matches(pageCode)) {
            return new ExceptionAdministrationPage(new ExceptionAdministrationPageUrl(params, session.getParameters()));
        }
        if (ContributionInvoicingInformationsPageUrl.matches(pageCode)) {
            return new ContributionInvoicingInformationsPage(new ContributionInvoicingInformationsPageUrl(params, session.getParameters()));
        }
        if (AdminGlobalNotificationPageUrl.matches(pageCode)) {
            return new AdminGlobalNotificationPage(new AdminGlobalNotificationPageUrl(params, session.getParameters()));
        }

        // ////////
        // Actions
        // ////////
        if (LoginActionUrl.matches(pageCode)) {
            return new LoginAction(new LoginActionUrl(params, session.getParameters()));
        }
        if (LogoutActionUrl.matches(pageCode)) {
            return new LogoutAction(new LogoutActionUrl(params, session.getParameters()));
        }
        if (ContributionActionUrl.matches(pageCode)) {
            return new ContributionAction(new ContributionActionUrl(params, session.getParameters()));
        }
        if (CheckContributionActionUrl.matches(pageCode)) {
            return new CheckContributionAction(new CheckContributionActionUrl(params, session.getParameters()));
        }
        if (OfferActionUrl.matches(pageCode)) {
            return new OfferAction(new OfferActionUrl(params, session.getParameters()));
        }
        if (CreateFeatureActionUrl.matches(pageCode)) {
            return new CreateFeatureAction(new CreateFeatureActionUrl(params, session.getParameters()));
        }
        if (SignUpActionUrl.matches(pageCode)) {
            return new SignUpAction(new SignUpActionUrl(params, session.getParameters()));
        }
        if (PopularityVoteActionUrl.matches(pageCode)) {
            return new PopularityVoteAction(new PopularityVoteActionUrl(params, session.getParameters()));
        }
        if (CreateCommentActionUrl.matches(pageCode)) {
            return new CreateCommentAction(new CreateCommentActionUrl(params, session.getParameters()));
        }
        if (PaylineActionUrl.matches(pageCode)) {
            return new PaylineAction(new PaylineActionUrl(params, session.getParameters()));
        }
        if (PaylineNotifyActionUrl.matches(pageCode)) {
            if (params.containsKey(PaylineNotifyAction.TOKEN_CODE)) {
                final String token = params.look(PaylineNotifyAction.TOKEN_CODE).getSimpleValue();
                final Session fakeSession = SessionManager.pickTemporarySession(token);
                if (fakeSession != null) {
                    Context.reInitializeContext(Context.getHeader(), fakeSession);
                }
            }
            return new PaylineNotifyAction(new PaylineNotifyActionUrl(params, session.getParameters()));
        }
        if (AddSoftwareActionUrl.matches(pageCode)) {
            return new AddSoftwareAction(new AddSoftwareActionUrl(params, session.getParameters()));
        }
        if (MemberActivationActionUrl.matches(pageCode)) {
            return new MemberActivationAction(new MemberActivationActionUrl(params, session.getParameters()));
        }
        if (PaylineReturnActionUrl.matches(pageCode)) {
            return new PaylineReturnAction(new PaylineReturnActionUrl(params, session.getParameters()));
        }
        if (AdministrationActionUrl.matches(pageCode)) {
            return new AdministrationAction(new AdministrationActionUrl(params, session.getParameters()));
        }
        if (CreateTeamActionUrl.matches(pageCode)) {
            return new CreateTeamAction(new CreateTeamActionUrl(params, session.getParameters()));
        }
        if (JoinTeamActionUrl.matches(pageCode)) {
            return new JoinTeamAction(new JoinTeamActionUrl(params, session.getParameters()));
        }
        if (SendTeamInvitationActionUrl.matches(pageCode)) {
            return new SendTeamInvitationAction(new SendTeamInvitationActionUrl(params, session.getParameters()));
        }
        if (AddAttachementActionUrl.matches(pageCode)) {
            return new AddAttachementAction(new AddAttachementActionUrl(params, session.getParameters()));
        }
        if (AddAttachementPageUrl.matches(pageCode)) {
            return new AddAttachementPage(new AddAttachementPageUrl(params, session.getParameters()));
        }
        if (HandleJoinTeamInvitationActionUrl.matches(pageCode)) {
            return new HandleJoinTeamInvitationAction(new HandleJoinTeamInvitationActionUrl(params, session.getParameters()));
        }
        if (ReportBugActionUrl.matches(pageCode)) {
            return new ReportBugAction(new ReportBugActionUrl(params, session.getParameters()));
        }
        if (AddReleaseActionUrl.matches(pageCode)) {
            return new AddReleaseAction(new AddReleaseActionUrl(params, session.getParameters()));
        }
        if (ModifyBugActionUrl.matches(pageCode)) {
            return new ModifyBugAction(new ModifyBugActionUrl(params, session.getParameters()));
        }
        if (ChangeAvatarActionUrl.matches(pageCode)) {
            return new ChangeAvatarAction(new ChangeAvatarActionUrl(params, session.getParameters()));
        }
        if (GiveRightActionUrl.matches(pageCode)) {
            return new GiveRightAction(new GiveRightActionUrl(params, session.getParameters()));
        }
        if (ConfigurationAdminActionUrl.matches(pageCode)) {
            return new ConfigurationAdminAction(new ConfigurationAdminActionUrl(params, session.getParameters()));
        }
        if (MetaReportBugActionUrl.matches(pageCode)) {
            return new MetaReportBugAction(new MetaReportBugActionUrl(params, session.getParameters()));
        }
        if (MetaEditBugActionUrl.matches(pageCode)) {
            return new MetaEditBugAction(new MetaEditBugActionUrl(params, session.getParameters()));
        }
        if (MetaBugDeleteActionUrl.matches(pageCode)) {
            return new MetaBugDeleteAction(new MetaBugDeleteActionUrl(params, session.getParameters()));
        }
        if (ChangeLanguageActionUrl.matches(pageCode)) {
            return new ChangeLanguageAction(new ChangeLanguageActionUrl(params, session.getParameters()));
        }
        if (UnlockAccountChargingProcessActionUrl.matches(pageCode)) {
            return new UnlockAccountChargingProcessAction(new UnlockAccountChargingProcessActionUrl(params, session.getParameters()));
        }
        if (UnlockContributionProcessActionUrl.matches(pageCode)) {
            return new UnlockContributionProcessAction(new UnlockContributionProcessActionUrl(params, session.getParameters()));
        }
        if (StaticAccountChargingPageUrl.matches(pageCode)) {
            return new StaticAccountChargingPage(new StaticAccountChargingPageUrl(params, session.getParameters()));
        }
        if (ModifyMemberActionUrl.matches(pageCode)) {
            return new ModifyMemberAction(new ModifyMemberActionUrl(params, session.getParameters()));
        }
        if (LostPasswordActionUrl.matches(pageCode)) {
            return new LostPasswordAction(new LostPasswordActionUrl(params, session.getParameters()));
        }
        if (RecoverPasswordActionUrl.matches(pageCode)) {
            return new RecoverPasswordAction(new RecoverPasswordActionUrl(params, session.getParameters()));
        }
        if (ModifyTeamActionUrl.matches(pageCode)) {
            return new ModifyTeamAction(new ModifyTeamActionUrl(params, session.getParameters()));
        }
        if (DeclareHightlightedFeatureActionUrl.matches(pageCode)) {
            return new DeclareHightlightedFeatureAction(new DeclareHightlightedFeatureActionUrl(params, session.getParameters()));
        }
        if (WithdrawMoneyActionUrl.matches(pageCode)) {
            return new WithdrawMoneyAction(new WithdrawMoneyActionUrl(params, session.getParameters()));
        }
        if (MoneyWithdrawalAdminActionUrl.matches(pageCode)) {
            return new MoneyWithdrawalAdminAction(new MoneyWithdrawalAdminActionUrl(params, session.getParameters()));
        }
        if (CancelWithdrawMoneyActionUrl.matches(pageCode)) {
            return new CancelWithdrawMoneyAction(new CancelWithdrawMoneyActionUrl(params, session.getParameters()));
        }
        if (ModifyInvoicingContactActionUrl.matches(pageCode)) {
            return new ModifyInvoicingContactAction(new ModifyInvoicingContactActionUrl(params, session.getParameters()));
        }
        if (ExceptionAdministrationActionUrl.matches(pageCode)) {
            return new ExceptionAdministrationAction(new ExceptionAdministrationActionUrl(params, session.getParameters()));
        }
        if (AdminGlobalNotificationActionUrl.matches(pageCode)) {
            return new AdminGlobalNotificationAction(new AdminGlobalNotificationActionUrl(params, session.getParameters()));
        }

        // ////////
        // Process
        // ////////
        if (ContributionProcessUrl.matches(pageCode)) {
            return new ContributionProcess(new ContributionProcessUrl(params, session.getParameters()));
        }
        if (AccountChargingProcessUrl.matches(pageCode)) {
            return new AccountChargingProcess(new AccountChargingProcessUrl(params, session.getParameters()));
        }
        if (PaylineProcessUrl.matches(pageCode)) {
            return new PaylineProcess(new PaylineProcessUrl(params, session.getParameters()));
        }
        if (ModifyInvoicingContactProcessUrl.matches(pageCode)) {
            return new ModifyInvoicingContactProcess(new ModifyInvoicingContactProcessUrl(params, session.getParameters()));
        }
        if (ContributionInvoicingProcessUrl.matches(pageCode)) {
            return new ContributionInvoicingProcess(new ContributionInvoicingProcessUrl(params, session.getParameters()));
        }

        // Resource page
        if (FileResourceUrl.matches(pageCode)) {
            return new FileResource(new FileResourceUrl(params, session.getParameters()));
        }
        if (InvoiceResourceUrl.matches(pageCode)) {
            return new InvoiceResource(new InvoiceResourceUrl(params, session.getParameters()));
        }
        Log.web().warn("Failed to find the page code '" + pageCode + "' in the linkable list. Maybe you forgot to declare it in BloatitWebServer ?");
        return new PageNotFound(new PageNotFoundUrl());
    }

    @Override
    public boolean initialize() {
        WebConfiguration.load();
        return true;
    }

}
