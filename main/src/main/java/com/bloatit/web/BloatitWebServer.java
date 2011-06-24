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
import com.bloatit.web.linkable.admin.news.AdminNewsAction;
import com.bloatit.web.linkable.admin.news.AdminNewsDeleteAction;
import com.bloatit.web.linkable.admin.news.AdminNewsPage;
import com.bloatit.web.linkable.admin.news.AdminNewsRestoreAction;
import com.bloatit.web.linkable.admin.notify.AdminGlobalNotificationAction;
import com.bloatit.web.linkable.admin.notify.AdminGlobalNotificationPage;
import com.bloatit.web.linkable.admin.withdraw.MoneyWithdrawalAdminAction;
import com.bloatit.web.linkable.admin.withdraw.MoneyWithdrawalAdminPage;
import com.bloatit.web.linkable.bugs.BugPage;
import com.bloatit.web.linkable.bugs.ModifyBugAction;
import com.bloatit.web.linkable.bugs.ModifyBugPage;
import com.bloatit.web.linkable.bugs.ReportBugAction;
import com.bloatit.web.linkable.bugs.ReportBugPage;
import com.bloatit.web.linkable.contribution.CheckContributeAction;
import com.bloatit.web.linkable.contribution.CheckContributePage;
import com.bloatit.web.linkable.contribution.ContributePage;
import com.bloatit.web.linkable.contribution.ContributeAction;
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
import com.bloatit.web.linkable.release.CreateReleaseAction;
import com.bloatit.web.linkable.release.CreateReleasePage;
import com.bloatit.web.linkable.release.ReleasePage;
import com.bloatit.web.linkable.softwares.CreateSoftwareAction;
import com.bloatit.web.linkable.softwares.CreateSoftwarePage;
import com.bloatit.web.linkable.softwares.ModifySoftwareAction;
import com.bloatit.web.linkable.softwares.ModifySoftwarePage;
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
    public Linkable constructLinkable(final String pageCode, final Parameters postGetParameters, final Session session) {

        // Pages
        if (PageForbiddenUrl.matches(pageCode)) {
            return new PageForbidden(new PageForbiddenUrl());
        }
        if (IndexPageUrl.matches(pageCode)) {
            return new IndexPage(new IndexPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (LoginPageUrl.matches(pageCode)) {
            return new LoginPage(new LoginPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (FeatureListPageUrl.matches(pageCode)) {
            return new FeatureListPage(new FeatureListPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateFeaturePageUrl.matches(pageCode)) {
            return new CreateFeaturePage(new CreateFeaturePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (FeaturePageUrl.matches(pageCode)) {
            return new FeaturePage(new FeaturePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (SiteMapPageUrl.matches(pageCode)) {
            return new SiteMapPage(new SiteMapPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MembersListPageUrl.matches(pageCode)) {
            return new MembersListPage(new MembersListPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MemberPageUrl.matches(pageCode)) {
            return new MemberPage(new MemberPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ContributePageUrl.matches(pageCode)) {
            return new ContributePage(new ContributePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CheckContributePageUrl.matches(pageCode)) {
            return new CheckContributePage(new CheckContributePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (StaticCheckContributionPageUrl.matches(pageCode)) {
            return new StaticCheckContributionPage(new StaticCheckContributionPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MakeOfferPageUrl.matches(pageCode)) {
            return new MakeOfferPage(new MakeOfferPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (TestPageUrl.matches(pageCode)) {
            return new TestPage(new TestPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AccountChargingPageUrl.matches(pageCode)) {
            return new AccountChargingPage(new AccountChargingPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (SignUpPageUrl.matches(pageCode)) {
            return new SignUpPage(new SignUpPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CommentReplyPageUrl.matches(pageCode)) {
            return new CommentReplyPage(new CommentReplyPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (SoftwarePageUrl.matches(pageCode)) {
            return new SoftwarePage(new SoftwarePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateSoftwarePageUrl.matches(pageCode)) {
            return new CreateSoftwarePage(new CreateSoftwarePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (SoftwareListPageUrl.matches(pageCode)) {
            return new SoftwareListPage(new SoftwareListPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (UserContentAdminPageUrl.matches(pageCode)) {
            return new UserContentAdminPageImplementation(new UserContentAdminPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (DocumentationPageUrl.matches(pageCode)) {
            return new DocumentationPage(new DocumentationPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (TeamsPageUrl.matches(pageCode)) {
            return new TeamsPage(new TeamsPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (TeamPageUrl.matches(pageCode)) {
            return new TeamPage(new TeamPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateTeamPageUrl.matches(pageCode)) {
            return new CreateTeamPage(new CreateTeamPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (SendTeamInvitationPageUrl.matches(pageCode)) {
            return new SendTeamInvitationPage(new SendTeamInvitationPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (KudosableAdminPageUrl.matches(pageCode)) {
            return new KudosableAdminPageImplementation(new KudosableAdminPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (FeatureAdminPageUrl.matches(pageCode)) {
            return new FeatureAdminPage(new FeatureAdminPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (BugPageUrl.matches(pageCode)) {
            return new BugPage(new BugPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ReportBugPageUrl.matches(pageCode)) {
            return new ReportBugPage(new ReportBugPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateReleasePageUrl.matches(pageCode)) {
            return new CreateReleasePage(new CreateReleasePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyBugPageUrl.matches(pageCode)) {
            return new ModifyBugPage(new ModifyBugPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ReleasePageUrl.matches(pageCode)) {
            return new ReleasePage(new ReleasePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MilestoneAdminPageUrl.matches(pageCode)) {
            return new MilestoneAdminPage(new MilestoneAdminPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MetaBugsListPageUrl.matches(pageCode)) {
            return new MetaBugsListPage(new MetaBugsListPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MetaBugEditPageUrl.matches(pageCode)) {
            return new MetaBugEditPage(new MetaBugEditPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ConfigurationAdminPageUrl.matches(pageCode)) {
            return new ConfigurationAdminPage(new ConfigurationAdminPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AdminHomePageUrl.matches(pageCode)) {
            return new AdminHomePage(new AdminHomePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (HightlightedFeatureAdminPageUrl.matches(pageCode)) {
            return new HightlightedFeatureAdminPage(new HightlightedFeatureAdminPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ChangeLanguagePageUrl.matches(pageCode)) {
            return new ChangeLanguagePage(new ChangeLanguagePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyMemberPageUrl.matches(pageCode)) {
            return new ModifyMemberPage(new ModifyMemberPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (LostPasswordPageUrl.matches(pageCode)) {
            return new LostPasswordPage(new LostPasswordPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (RecoverPasswordPageUrl.matches(pageCode)) {
            return new RecoverPasswordPage(new RecoverPasswordPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyTeamPageUrl.matches(pageCode)) {
            return new ModifyTeamPage(new ModifyTeamPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (WithdrawMoneyPageUrl.matches(pageCode)) {
            return new WithdrawMoneyPage(new WithdrawMoneyPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MoneyWithdrawalAdminPageUrl.matches(pageCode)) {
            return new MoneyWithdrawalAdminPage(new MoneyWithdrawalAdminPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyContactPageUrl.matches(pageCode)) {
            return new ModifyContactPage(new ModifyContactPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifySoftwarePageUrl.matches(pageCode)) {
            return new ModifySoftwarePage(new ModifySoftwarePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ExceptionAdministrationPageUrl.matches(pageCode)) {
            return new ExceptionAdministrationPage(new ExceptionAdministrationPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ContributionInvoicingInformationsPageUrl.matches(pageCode)) {
            return new ContributionInvoicingInformationsPage(new ContributionInvoicingInformationsPageUrl(pageCode,
                                                                                                          postGetParameters,
                                                                                                          session.getParameters()));
        }
        if (AdminGlobalNotificationPageUrl.matches(pageCode)) {
            return new AdminGlobalNotificationPage(new AdminGlobalNotificationPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AdminNewsPageUrl.matches(pageCode)) {
            return new AdminNewsPage(new AdminNewsPageUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // ////////
        // Actions
        // ////////
        if (LoginActionUrl.matches(pageCode)) {
            return new LoginAction(new LoginActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (LogoutActionUrl.matches(pageCode)) {
            return new LogoutAction(new LogoutActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ContributeActionUrl.matches(pageCode)) {
            return new ContributeAction(new ContributeActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CheckContributeActionUrl.matches(pageCode)) {
            return new CheckContributeAction(new CheckContributeActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (OfferActionUrl.matches(pageCode)) {
            return new OfferAction(new OfferActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateFeatureActionUrl.matches(pageCode)) {
            return new CreateFeatureAction(new CreateFeatureActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (SignUpActionUrl.matches(pageCode)) {
            return new SignUpAction(new SignUpActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PopularityVoteActionUrl.matches(pageCode)) {
            return new PopularityVoteAction(new PopularityVoteActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateCommentActionUrl.matches(pageCode)) {
            return new CreateCommentAction(new CreateCommentActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PaylineActionUrl.matches(pageCode)) {
            return new PaylineAction(new PaylineActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PaylineNotifyActionUrl.matches(pageCode)) {
            if (postGetParameters.containsKey(PaylineNotifyAction.TOKEN_CODE)) {
                final String token = postGetParameters.look(PaylineNotifyAction.TOKEN_CODE).getSimpleValue();
                final Session fakeSession = SessionManager.pickTemporarySession(token);
                if (fakeSession != null) {
                    Context.reInitializeContext(Context.getHeader(), fakeSession);
                }
            }
            return new PaylineNotifyAction(new PaylineNotifyActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateSoftwareActionUrl.matches(pageCode)) {
            return new CreateSoftwareAction(new CreateSoftwareActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MemberActivationActionUrl.matches(pageCode)) {
            return new MemberActivationAction(new MemberActivationActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PaylineReturnActionUrl.matches(pageCode)) {
            return new PaylineReturnAction(new PaylineReturnActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AdministrationActionUrl.matches(pageCode)) {
            return new AdministrationAction(new AdministrationActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateTeamActionUrl.matches(pageCode)) {
            return new CreateTeamAction(new CreateTeamActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (JoinTeamActionUrl.matches(pageCode)) {
            return new JoinTeamAction(new JoinTeamActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (SendTeamInvitationActionUrl.matches(pageCode)) {
            return new SendTeamInvitationAction(new SendTeamInvitationActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AddAttachementActionUrl.matches(pageCode)) {
            return new AddAttachementAction(new AddAttachementActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AddAttachementPageUrl.matches(pageCode)) {
            return new AddAttachementPage(new AddAttachementPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (HandleJoinTeamInvitationActionUrl.matches(pageCode)) {
            return new HandleJoinTeamInvitationAction(new HandleJoinTeamInvitationActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ReportBugActionUrl.matches(pageCode)) {
            return new ReportBugAction(new ReportBugActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateReleaseActionUrl.matches(pageCode)) {
            return new CreateReleaseAction(new CreateReleaseActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyBugActionUrl.matches(pageCode)) {
            return new ModifyBugAction(new ModifyBugActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ChangeAvatarActionUrl.matches(pageCode)) {
            return new ChangeAvatarAction(new ChangeAvatarActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (GiveRightActionUrl.matches(pageCode)) {
            return new GiveRightAction(new GiveRightActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ConfigurationAdminActionUrl.matches(pageCode)) {
            return new ConfigurationAdminAction(new ConfigurationAdminActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MetaReportBugActionUrl.matches(pageCode)) {
            return new MetaReportBugAction(new MetaReportBugActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MetaEditBugActionUrl.matches(pageCode)) {
            return new MetaEditBugAction(new MetaEditBugActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MetaBugDeleteActionUrl.matches(pageCode)) {
            return new MetaBugDeleteAction(new MetaBugDeleteActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ChangeLanguageActionUrl.matches(pageCode)) {
            return new ChangeLanguageAction(new ChangeLanguageActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (UnlockAccountChargingProcessActionUrl.matches(pageCode)) {
            return new UnlockAccountChargingProcessAction(new UnlockAccountChargingProcessActionUrl(pageCode,
                                                                                                    postGetParameters,
                                                                                                    session.getParameters()));
        }
        if (UnlockContributionProcessActionUrl.matches(pageCode)) {
            return new UnlockContributionProcessAction(new UnlockContributionProcessActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (StaticAccountChargingPageUrl.matches(pageCode)) {
            return new StaticAccountChargingPage(new StaticAccountChargingPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyMemberActionUrl.matches(pageCode)) {
            return new ModifyMemberAction(new ModifyMemberActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (LostPasswordActionUrl.matches(pageCode)) {
            return new LostPasswordAction(new LostPasswordActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (RecoverPasswordActionUrl.matches(pageCode)) {
            return new RecoverPasswordAction(new RecoverPasswordActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyTeamActionUrl.matches(pageCode)) {
            return new ModifyTeamAction(new ModifyTeamActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (DeclareHightlightedFeatureActionUrl.matches(pageCode)) {
            return new DeclareHightlightedFeatureAction(new DeclareHightlightedFeatureActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (WithdrawMoneyActionUrl.matches(pageCode)) {
            return new WithdrawMoneyAction(new WithdrawMoneyActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MoneyWithdrawalAdminActionUrl.matches(pageCode)) {
            return new MoneyWithdrawalAdminAction(new MoneyWithdrawalAdminActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CancelWithdrawMoneyActionUrl.matches(pageCode)) {
            return new CancelWithdrawMoneyAction(new CancelWithdrawMoneyActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyInvoicingContactActionUrl.matches(pageCode)) {
            return new ModifyInvoicingContactAction(new ModifyInvoicingContactActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifySoftwareActionUrl.matches(pageCode)) {
            return new ModifySoftwareAction(new ModifySoftwareActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ExceptionAdministrationActionUrl.matches(pageCode)) {
            return new ExceptionAdministrationAction(new ExceptionAdministrationActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AdminGlobalNotificationActionUrl.matches(pageCode)) {
            return new AdminGlobalNotificationAction(new AdminGlobalNotificationActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AdminNewsActionUrl.matches(pageCode)) {
            return new AdminNewsAction(new AdminNewsActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AdminNewsDeleteActionUrl.matches(pageCode)) {
            return new AdminNewsDeleteAction(new AdminNewsDeleteActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AdminNewsRestoreActionUrl.matches(pageCode)) {
            return new AdminNewsRestoreAction(new AdminNewsRestoreActionUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // ////////
        // Process
        // ////////
        if (ContributionProcessUrl.matches(pageCode)) {
            return new ContributionProcess(new ContributionProcessUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AccountChargingProcessUrl.matches(pageCode)) {
            return new AccountChargingProcess(new AccountChargingProcessUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PaylineProcessUrl.matches(pageCode)) {
            return new PaylineProcess(new PaylineProcessUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyInvoicingContactProcessUrl.matches(pageCode)) {
            return new ModifyInvoicingContactProcess(new ModifyInvoicingContactProcessUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ContributionInvoicingProcessUrl.matches(pageCode)) {
            return new ContributionInvoicingProcess(new ContributionInvoicingProcessUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // Resource page
        if (FileResourceUrl.matches(pageCode)) {
            return new FileResource(new FileResourceUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (InvoiceResourceUrl.matches(pageCode)) {
            return new InvoiceResource(new InvoiceResourceUrl(pageCode, postGetParameters, session.getParameters()));
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
