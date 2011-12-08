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
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.WebProcessor;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.webprocessor.context.SessionManager;
import com.bloatit.framework.webprocessor.masters.Linkable;
import com.bloatit.framework.webprocessor.url.PageForbiddenUrl;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.linkable.AddAttachementAction;
import com.bloatit.web.linkable.AddAttachementPage;
import com.bloatit.web.linkable.CommentReplyPage;
import com.bloatit.web.linkable.CreateCommentAction;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.PopularityVoteAction;
import com.bloatit.web.linkable.SiteMapPage;
import com.bloatit.web.linkable.TestPage;
import com.bloatit.web.linkable.admin.AdminHomePage;
import com.bloatit.web.linkable.admin.AdministrationAction;
import com.bloatit.web.linkable.admin.DeclareHightlightedFeatureAction;
import com.bloatit.web.linkable.admin.FeatureAdminPage;
import com.bloatit.web.linkable.admin.HightlightedFeatureAdminPage;
import com.bloatit.web.linkable.admin.KudosableAdminPageImplementation;
import com.bloatit.web.linkable.admin.MilestoneAdminPage;
import com.bloatit.web.linkable.admin.UserContentAdminPageImplementation;
import com.bloatit.web.linkable.admin.configuration.ConfigurationAdminAction;
import com.bloatit.web.linkable.admin.configuration.ConfigurationAdminPage;
import com.bloatit.web.linkable.admin.exception.ExceptionAdministrationAction;
import com.bloatit.web.linkable.admin.exception.ExceptionAdministrationPage;
import com.bloatit.web.linkable.admin.moderation.FeatureModerationAction;
import com.bloatit.web.linkable.admin.moderation.FeatureModerationPage;
import com.bloatit.web.linkable.admin.news.AdminNewsAction;
import com.bloatit.web.linkable.admin.news.AdminNewsDeleteAction;
import com.bloatit.web.linkable.admin.news.AdminNewsPage;
import com.bloatit.web.linkable.admin.news.AdminNewsRestoreAction;
import com.bloatit.web.linkable.admin.notify.AdminGlobalNotificationAction;
import com.bloatit.web.linkable.admin.notify.AdminGlobalNotificationPage;
import com.bloatit.web.linkable.admin.withdraw.MoneyWithdrawalAdminAction;
import com.bloatit.web.linkable.admin.withdraw.MoneyWithdrawalAdminPage;
import com.bloatit.web.linkable.aliases.FeaturePageAlias;
import com.bloatit.web.linkable.aliases.IndexPageAlias;
import com.bloatit.web.linkable.aliases.MembersPageAlias;
import com.bloatit.web.linkable.atom.FeatureAtomFeed;
import com.bloatit.web.linkable.atom.SoftwareAtomFeed;
import com.bloatit.web.linkable.atom.ActivityAtomFeed;
import com.bloatit.web.linkable.bugs.BugPage;
import com.bloatit.web.linkable.bugs.ModifyBugAction;
import com.bloatit.web.linkable.bugs.ModifyBugPage;
import com.bloatit.web.linkable.bugs.ReportBugAction;
import com.bloatit.web.linkable.bugs.ReportBugPage;
import com.bloatit.web.linkable.contribution.CancelContributionAction;
import com.bloatit.web.linkable.contribution.CancelContributionPage;
import com.bloatit.web.linkable.contribution.CheckContributeAction;
import com.bloatit.web.linkable.contribution.CheckContributePage;
import com.bloatit.web.linkable.contribution.ContributeAction;
import com.bloatit.web.linkable.contribution.ContributePage;
import com.bloatit.web.linkable.contribution.ContributionProcess;
import com.bloatit.web.linkable.contribution.StaticCheckContributionPage;
import com.bloatit.web.linkable.contribution.UnlockContributionProcessAction;
import com.bloatit.web.linkable.documentation.DocumentationPage;
import com.bloatit.web.linkable.documentation.DocumentationRootPage;
import com.bloatit.web.linkable.errors.PageForbidden;
import com.bloatit.web.linkable.errors.PageNotFound;
import com.bloatit.web.linkable.features.FeatureListPage;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.ModifyFeatureAction;
import com.bloatit.web.linkable.features.ModifyFeaturePage;
import com.bloatit.web.linkable.features.create.ChooseFeatureTypeAction;
import com.bloatit.web.linkable.features.create.ChooseFeatureTypePage;
import com.bloatit.web.linkable.features.create.CreateFeatureAction;
import com.bloatit.web.linkable.features.create.CreateFeatureAndOfferAction;
import com.bloatit.web.linkable.features.create.CreateFeatureAndOfferPage;
import com.bloatit.web.linkable.features.create.CreateFeaturePage;
import com.bloatit.web.linkable.features.create.CreateFeatureProcess;
import com.bloatit.web.linkable.invoice.ContributionInvoicePreviewData;
import com.bloatit.web.linkable.invoice.ContributionInvoiceResource;
import com.bloatit.web.linkable.invoice.ContributionInvoicesZipData;
import com.bloatit.web.linkable.invoice.ContributionInvoicingInformationsAction;
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
import com.bloatit.web.linkable.members.ModifyDetailAction;
import com.bloatit.web.linkable.members.ModifyMemberAction;
import com.bloatit.web.linkable.members.ModifyMemberPage;
import com.bloatit.web.linkable.members.ModifyNewsletterAction;
import com.bloatit.web.linkable.members.ModifyPasswordAction;
import com.bloatit.web.linkable.members.tabs.dashboard.StopFollowAction;
import com.bloatit.web.linkable.meta.feedback.MetaEditFeedbackAction;
import com.bloatit.web.linkable.meta.feedback.MetaFeedbackDeleteAction;
import com.bloatit.web.linkable.meta.feedback.MetaFeedbackEditPage;
import com.bloatit.web.linkable.meta.feedback.MetaFeedbackListPage;
import com.bloatit.web.linkable.meta.feedback.MetaReportFeedbackAction;
import com.bloatit.web.linkable.money.AccountChargingPage;
import com.bloatit.web.linkable.money.AccountChargingProcess;
import com.bloatit.web.linkable.money.CancelWithdrawMoneyAction;
import com.bloatit.web.linkable.money.ChangePrepaidAmountAction;
import com.bloatit.web.linkable.money.PaymentAction;
import com.bloatit.web.linkable.money.PaymentAutoresponseAction;
import com.bloatit.web.linkable.money.PaymentProcess;
import com.bloatit.web.linkable.money.PaymentResponseAction;
import com.bloatit.web.linkable.money.StaticAccountChargingPage;
import com.bloatit.web.linkable.money.UnlockAccountChargingProcessAction;
import com.bloatit.web.linkable.money.WithdrawMoneyAction;
import com.bloatit.web.linkable.money.WithdrawMoneyPage;
import com.bloatit.web.linkable.oauth2.CreateExternalServiceAction;
import com.bloatit.web.linkable.oauth2.CreateExternalServicePage;
import com.bloatit.web.linkable.oauth2.OAuthAuthorizationPage;
import com.bloatit.web.linkable.oauth2.OAuthPage;
import com.bloatit.web.linkable.offer.MakeOfferPage;
import com.bloatit.web.linkable.offer.OfferAction;
import com.bloatit.web.linkable.release.CreateReleaseAction;
import com.bloatit.web.linkable.release.CreateReleasePage;
import com.bloatit.web.linkable.release.ReleasePage;
import com.bloatit.web.linkable.sitemap.ElveosSiteMap;
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
import com.bloatit.web.linkable.activity.FollowActorAction;
import com.bloatit.web.linkable.activity.FollowAllAction;
import com.bloatit.web.linkable.activity.FollowFeatureAction;
import com.bloatit.web.linkable.activity.FollowSoftwareAction;
import com.bloatit.web.linkable.activity.ManageFollowAction;
import com.bloatit.web.linkable.activity.ManageFollowPage;
import com.bloatit.web.linkable.activity.ReadActivityAction;
import com.bloatit.web.linkable.activity.ActivityPage;
import com.bloatit.web.linkable.translation.TranslateAction;
import com.bloatit.web.linkable.translation.TranslatePage;
import com.bloatit.web.url.*;

public class BloatitWebServer extends WebProcessor {

    public BloatitWebServer() {
        super();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Linkable constructLinkable(final String pageCode, final Parameters postGetParameters, final Session session) {

        // Pages
        if (IndexPageUrl.matches(pageCode)) {
            return new IndexPage(new IndexPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PageNotFoundUrl.matches(pageCode)) {
            return new PageNotFound(new PageNotFoundUrl());
        }
        if (PageForbiddenUrl.matches(pageCode)) {
            return new PageForbidden(new PageForbiddenUrl());
        }
        if (LoginPageUrl.matches(pageCode)) {
            return new LoginPage(new LoginPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (FeatureListPageUrl.matches(pageCode)) {
            return new FeatureListPage(new FeatureListPageUrl(pageCode, postGetParameters, session.getParameters()));
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
        if (DocumentationRootPageUrl.matches(pageCode)) {
            return new DocumentationRootPage(new DocumentationRootPageUrl(pageCode, postGetParameters, session.getParameters()));
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
        if (MetaFeedbackListPageUrl.matches(pageCode)) {
            return new MetaFeedbackListPage(new MetaFeedbackListPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MetaFeedbackEditPageUrl.matches(pageCode)) {
            return new MetaFeedbackEditPage(new MetaFeedbackEditPageUrl(pageCode, postGetParameters, session.getParameters()));
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
        if (ContributionInvoicingInformationsActionUrl.matches(pageCode)) {
            return new ContributionInvoicingInformationsAction(new ContributionInvoicingInformationsActionUrl(pageCode,
                                                                                                              postGetParameters,
                                                                                                              session.getParameters()));
        }
        if (AdminGlobalNotificationPageUrl.matches(pageCode)) {
            return new AdminGlobalNotificationPage(new AdminGlobalNotificationPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (AdminNewsPageUrl.matches(pageCode)) {
            return new AdminNewsPage(new AdminNewsPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (FeatureModerationPageUrl.matches(pageCode)) {
            return new FeatureModerationPage(new FeatureModerationPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (OAuthAuthorizationPageUrl.matches(pageCode)) {
            return new OAuthAuthorizationPage(new OAuthAuthorizationPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (OAuthPageUrl.matches(pageCode)) {
            return new OAuthPage(new OAuthPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateExternalServicePageUrl.matches(pageCode)) {
            return new CreateExternalServicePage(new CreateExternalServicePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CancelContributionPageUrl.matches(pageCode)) {
            return new CancelContributionPage(new CancelContributionPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyFeaturePageUrl.matches(pageCode)) {
            return new ModifyFeaturePage(new ModifyFeaturePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ActivityPageUrl.matches(pageCode)) {
            return new ActivityPage(new ActivityPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ManageFollowPageUrl.matches(pageCode)) {
            return new ManageFollowPage(new ManageFollowPageUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // ////////
        // Actions
        // ////////
        if (CreateExternalServiceActionUrl.matches(pageCode)) {
            return new CreateExternalServiceAction(new CreateExternalServiceActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
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
        if (SignUpActionUrl.matches(pageCode)) {
            return new SignUpAction(new SignUpActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PopularityVoteActionUrl.matches(pageCode)) {
            return new PopularityVoteAction(new PopularityVoteActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateCommentActionUrl.matches(pageCode)) {
            return new CreateCommentAction(new CreateCommentActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PaymentActionUrl.matches(pageCode)) {
            return new PaymentAction(new PaymentActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PaymentAutoresponseActionUrl.matches(pageCode)) {
            if (postGetParameters.containsKey(PaymentAutoresponseAction.TOKEN_CODE)) {
                final String token = postGetParameters.look(PaymentAutoresponseAction.TOKEN_CODE).getSimpleValue();
                final Session fakeSession = SessionManager.pickTemporarySession(token);
                if (fakeSession != null) {
                    Context.reInitializeContext(Context.getHeader(), fakeSession);
                    AuthToken.authenticate(fakeSession);
                }
            }
            return new PaymentAutoresponseAction(new PaymentAutoresponseActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateSoftwareActionUrl.matches(pageCode)) {
            return new CreateSoftwareAction(new CreateSoftwareActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MemberActivationActionUrl.matches(pageCode)) {
            return new MemberActivationAction(new MemberActivationActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (PaymentResponseActionUrl.matches(pageCode)) {
            return new PaymentResponseAction(new PaymentResponseActionUrl(pageCode, postGetParameters, session.getParameters()));
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
        if (MetaReportFeedbackActionUrl.matches(pageCode)) {
            return new MetaReportFeedbackAction(new MetaReportFeedbackActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MetaEditFeedbackActionUrl.matches(pageCode)) {
            return new MetaEditFeedbackAction(new MetaEditFeedbackActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MetaFeedbackDeleteActionUrl.matches(pageCode)) {
            return new MetaFeedbackDeleteAction(new MetaFeedbackDeleteActionUrl(pageCode, postGetParameters, session.getParameters()));
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
        if (ModifyDetailActionUrl.matches(pageCode)) {
            return new ModifyDetailAction(new ModifyDetailActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyPasswordActionUrl.matches(pageCode)) {
            return new ModifyPasswordAction(new ModifyPasswordActionUrl(pageCode, postGetParameters, session.getParameters()));
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
        if (FeatureModerationActionUrl.matches(pageCode)) {
            return new FeatureModerationAction(new FeatureModerationActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CancelContributionActionUrl.matches(pageCode)) {
            return new CancelContributionAction(new CancelContributionActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyFeatureActionUrl.matches(pageCode)) {
            return new ModifyFeatureAction(new ModifyFeatureActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ChangePrepaidAmountActionUrl.matches(pageCode)) {
            return new ChangePrepaidAmountAction(new ChangePrepaidAmountActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (StopFollowActionUrl.matches(pageCode)) {
            return new StopFollowAction(new StopFollowActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyNewsletterActionUrl.matches(pageCode)) {
            return new ModifyNewsletterAction(new ModifyNewsletterActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ReadActivityActionUrl.matches(pageCode)) {
            return new ReadActivityAction(new ReadActivityActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (FollowFeatureActionUrl.matches(pageCode)) {
            return new FollowFeatureAction(new FollowFeatureActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (FollowSoftwareActionUrl.matches(pageCode)) {
            return new FollowSoftwareAction(new FollowSoftwareActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (FollowActorActionUrl.matches(pageCode)) {
            return new FollowActorAction(new FollowActorActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (FollowAllActionUrl.matches(pageCode)) {
            return new FollowAllAction(new FollowAllActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ManageFollowActionUrl.matches(pageCode)) {
            return new ManageFollowAction(new ManageFollowActionUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // ////////
        // Sitemap
        // ////////
        if (ElveosSiteMapUrl.matches(pageCode)) {
            return new ElveosSiteMap(new ElveosSiteMapUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // ////////
        // Atom
        // ////////
        if (FeatureAtomFeedUrl.matches(pageCode)) {
            return new FeatureAtomFeed(new FeatureAtomFeedUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (SoftwareAtomFeedUrl.matches(pageCode)) {
            return new SoftwareAtomFeed(new SoftwareAtomFeedUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ActivityAtomFeedUrl.matches(pageCode)) {
            return new ActivityAtomFeed(new ActivityAtomFeedUrl(pageCode, postGetParameters, session.getParameters()));
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
        if (PaymentProcessUrl.matches(pageCode)) {
            return new PaymentProcess(new PaymentProcessUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ModifyInvoicingContactProcessUrl.matches(pageCode)) {
            return new ModifyInvoicingContactProcess(new ModifyInvoicingContactProcessUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ContributionInvoicingProcessUrl.matches(pageCode)) {
            return new ContributionInvoicingProcess(new ContributionInvoicingProcessUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // ////////
        // ALIAS
        // ////////
        if (FeaturePageAliasUrl.matches(pageCode)) {
            return new FeaturePageAlias(new FeaturePageAliasUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (IndexPageAliasUrl.matches(pageCode)) {
            return new IndexPageAlias(new IndexPageAliasUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (MembersPageAliasUrl.matches(pageCode)) {
            return new MembersPageAlias(new MembersPageAliasUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // Create feature process
        if (CreateFeatureProcessUrl.matches(pageCode)) {
            return new CreateFeatureProcess(new CreateFeatureProcessUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ChooseFeatureTypeActionUrl.matches(pageCode)) {
            return new ChooseFeatureTypeAction(new ChooseFeatureTypeActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (ChooseFeatureTypePageUrl.matches(pageCode)) {
            return new ChooseFeatureTypePage(new ChooseFeatureTypePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateFeatureActionUrl.matches(pageCode)) {
            return new CreateFeatureAction(new CreateFeatureActionUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateFeaturePageUrl.matches(pageCode)) {
            return new CreateFeaturePage(new CreateFeaturePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateFeatureAndOfferPageUrl.matches(pageCode)) {
            return new CreateFeatureAndOfferPage(new CreateFeatureAndOfferPageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (CreateFeatureAndOfferActionUrl.matches(pageCode)) {
            return new CreateFeatureAndOfferAction(new CreateFeatureAndOfferActionUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // Translation
        if (TranslatePageUrl.matches(pageCode)) {
            return new TranslatePage(new TranslatePageUrl(pageCode, postGetParameters, session.getParameters()));
        }
        if (TranslateActionUrl.matches(pageCode)) {
            return new TranslateAction(new TranslateActionUrl(pageCode, postGetParameters, session.getParameters()));
        }

        // Resource page
        try {
            if (FileResourceUrl.matches(pageCode)) {
                return new FileResource(new FileResourceUrl(pageCode, postGetParameters, session.getParameters()));
            }
            if (InvoiceResourceUrl.matches(pageCode)) {
                return new InvoiceResource(new InvoiceResourceUrl(pageCode, postGetParameters, session.getParameters()));
            }
            if (ContributionInvoiceResourceUrl.matches(pageCode)) {
                return new ContributionInvoiceResource(new ContributionInvoiceResourceUrl(pageCode, postGetParameters, session.getParameters()));
            }
            if (ContributionInvoicePreviewDataUrl.matches(pageCode)) {
                return new ContributionInvoicePreviewData(new ContributionInvoicePreviewDataUrl(pageCode, postGetParameters, session.getParameters()));
            }
            if (ContributionInvoicesZipDataUrl.matches(pageCode)) {
                return new ContributionInvoicesZipData(new ContributionInvoicesZipDataUrl(pageCode, postGetParameters, session.getParameters()));
            }

        } catch (final PageNotFoundException e) {
            return new PageNotFound(new PageNotFoundUrl());
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
