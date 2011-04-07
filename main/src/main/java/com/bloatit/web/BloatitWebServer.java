package com.bloatit.web;

import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.SessionManager;
import com.bloatit.framework.webserver.WebServer;
import com.bloatit.framework.webserver.masters.Linkable;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.web.actions.AddAttachementAction;
import com.bloatit.web.actions.AddAttachementPage;
import com.bloatit.web.actions.CommentCommentAction;
import com.bloatit.web.actions.CreateCommentAction;
import com.bloatit.web.actions.MemberActivationAction;
import com.bloatit.web.actions.OfferAction;
import com.bloatit.web.actions.PopularityVoteAction;
import com.bloatit.web.actions.UploadFileAction;
import com.bloatit.web.linkable.admin.AdminHomePage;
import com.bloatit.web.linkable.admin.AdministrationAction;
import com.bloatit.web.linkable.admin.ConfigurationAdminAction;
import com.bloatit.web.linkable.admin.ConfigurationAdminPage;
import com.bloatit.web.linkable.admin.FeatureAdminPage;
import com.bloatit.web.linkable.admin.KudosableAdminPageImplementation;
import com.bloatit.web.linkable.admin.MilestoneAdminPage;
import com.bloatit.web.linkable.admin.UserContentAdminPageImplementation;
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
import com.bloatit.web.linkable.features.CreateFeatureAction;
import com.bloatit.web.linkable.features.CreateFeaturePage;
import com.bloatit.web.linkable.features.FeatureListPage;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.language.ChangeLanguageAction;
import com.bloatit.web.linkable.language.ChangeLanguagePage;
import com.bloatit.web.linkable.login.LoginAction;
import com.bloatit.web.linkable.login.LoginPage;
import com.bloatit.web.linkable.login.LogoutAction;
import com.bloatit.web.linkable.login.SignUpAction;
import com.bloatit.web.linkable.login.SignUpPage;
import com.bloatit.web.linkable.members.ChangeAvatarAction;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.members.MembersListPage;
import com.bloatit.web.linkable.messages.MessageListPage;
import com.bloatit.web.linkable.metabugreport.MetaBugDeleteAction;
import com.bloatit.web.linkable.metabugreport.MetaBugEditPage;
import com.bloatit.web.linkable.metabugreport.MetaBugsListPage;
import com.bloatit.web.linkable.metabugreport.MetaEditBugAction;
import com.bloatit.web.linkable.metabugreport.MetaReportBugAction;
import com.bloatit.web.linkable.money.AccountChargingPage;
import com.bloatit.web.linkable.money.AccountChargingProcess;
import com.bloatit.web.linkable.money.PaylineAction;
import com.bloatit.web.linkable.money.PaylineNotifyAction;
import com.bloatit.web.linkable.money.PaylinePage;
import com.bloatit.web.linkable.money.PaylineProcess;
import com.bloatit.web.linkable.money.PaylineReturnAction;
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
import com.bloatit.web.linkable.team.JoinTeamPage;
import com.bloatit.web.linkable.team.SendTeamInvitationAction;
import com.bloatit.web.linkable.team.SendTeamInvitationPage;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.linkable.team.TeamsPage;
import com.bloatit.web.pages.CommentReplyPage;
import com.bloatit.web.pages.DocumentationPage;
import com.bloatit.web.pages.FileUploadPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.MakeOfferPage;
import com.bloatit.web.pages.PageNotFound;
import com.bloatit.web.pages.SpecialsPage;
import com.bloatit.web.pages.TestPage;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.AccountChargingProcessUrl;
import com.bloatit.web.url.AddAttachementActionUrl;
import com.bloatit.web.url.AddAttachementPageUrl;
import com.bloatit.web.url.AddReleaseActionUrl;
import com.bloatit.web.url.AddReleasePageUrl;
import com.bloatit.web.url.AddSoftwareActionUrl;
import com.bloatit.web.url.AddSoftwarePageUrl;
import com.bloatit.web.url.AdminHomePageUrl;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.ChangeAvatarActionUrl;
import com.bloatit.web.url.ChangeLanguageActionUrl;
import com.bloatit.web.url.ChangeLanguagePageUrl;
import com.bloatit.web.url.CheckContributionActionUrl;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.CommentCommentActionUrl;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.ConfigurationAdminActionUrl;
import com.bloatit.web.url.ConfigurationAdminPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.ContributionProcessUrl;
import com.bloatit.web.url.CreateCommentActionUrl;
import com.bloatit.web.url.CreateFeatureActionUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.CreateTeamActionUrl;
import com.bloatit.web.url.CreateTeamPageUrl;
import com.bloatit.web.url.DocumentationPageUrl;
import com.bloatit.web.url.FeatureAdminPageUrl;
import com.bloatit.web.url.FeatureListPageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.FileUploadPageUrl;
import com.bloatit.web.url.GiveRightActionUrl;
import com.bloatit.web.url.HandleJoinTeamInvitationActionUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.JoinTeamPageUrl;
import com.bloatit.web.url.KudosableAdminPageUrl;
import com.bloatit.web.url.LoginActionUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LogoutActionUrl;
import com.bloatit.web.url.MakeOfferPageUrl;
import com.bloatit.web.url.MemberActivationActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.MessageListPageUrl;
import com.bloatit.web.url.MetaBugDeleteActionUrl;
import com.bloatit.web.url.MetaBugEditPageUrl;
import com.bloatit.web.url.MetaBugsListPageUrl;
import com.bloatit.web.url.MetaEditBugActionUrl;
import com.bloatit.web.url.MetaReportBugActionUrl;
import com.bloatit.web.url.MilestoneAdminPageUrl;
import com.bloatit.web.url.ModifyBugActionUrl;
import com.bloatit.web.url.ModifyBugPageUrl;
import com.bloatit.web.url.OfferActionUrl;
import com.bloatit.web.url.PaylineActionUrl;
import com.bloatit.web.url.PaylineNotifyActionUrl;
import com.bloatit.web.url.PaylinePageUrl;
import com.bloatit.web.url.PaylineProcessUrl;
import com.bloatit.web.url.PaylineReturnActionUrl;
import com.bloatit.web.url.PopularityVoteActionUrl;
import com.bloatit.web.url.ReleasePageUrl;
import com.bloatit.web.url.ReportBugActionUrl;
import com.bloatit.web.url.ReportBugPageUrl;
import com.bloatit.web.url.SendTeamInvitationActionUrl;
import com.bloatit.web.url.SendTeamInvitationPageUrl;
import com.bloatit.web.url.SignUpActionUrl;
import com.bloatit.web.url.SignUpPageUrl;
import com.bloatit.web.url.SoftwareListPageUrl;
import com.bloatit.web.url.SoftwarePageUrl;
import com.bloatit.web.url.SpecialsPageUrl;
import com.bloatit.web.url.TeamPageUrl;
import com.bloatit.web.url.TeamsPageUrl;
import com.bloatit.web.url.TestPageUrl;
import com.bloatit.web.url.UploadFileActionUrl;
import com.bloatit.web.url.UserContentAdminPageUrl;

public class BloatitWebServer extends WebServer {

    public BloatitWebServer() {
        super();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Linkable constructLinkable(final String pageCode, final Parameters params, final Session session) {

        // Pages
        if (pageCode.equals(IndexPageUrl.getName())) {
            return new IndexPage(new IndexPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(LoginPageUrl.getName())) {
            return new LoginPage(new LoginPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(FeatureListPageUrl.getName())) {
            return new FeatureListPage(new FeatureListPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CreateFeaturePageUrl.getName())) {
            return new CreateFeaturePage(new CreateFeaturePageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(FeaturePageUrl.getName())) {
            return new FeaturePage(new FeaturePageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(SpecialsPageUrl.getName())) {
            return new SpecialsPage(new SpecialsPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MembersListPageUrl.getName())) {
            return new MembersListPage(new MembersListPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MemberPageUrl.getName())) {
            return new MemberPage(new MemberPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ContributePageUrl.getName())) {
            return new ContributePage(new ContributePageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CheckContributionPageUrl.getName())) {
            return new CheckContributionPage(new CheckContributionPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MakeOfferPageUrl.getName())) {
            return new MakeOfferPage(new MakeOfferPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(TestPageUrl.getName())) {
            return new TestPage(new TestPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AccountChargingPageUrl.getName())) {
            return new AccountChargingPage(new AccountChargingPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(SignUpPageUrl.getName())) {
            return new SignUpPage(new SignUpPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(PaylinePageUrl.getName())) {
            return new PaylinePage(new PaylinePageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CommentReplyPageUrl.getName())) {
            return new CommentReplyPage(new CommentReplyPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(FileUploadPageUrl.getName())) {
            return new FileUploadPage(new FileUploadPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(SoftwarePageUrl.getName())) {
            return new SoftwarePage(new SoftwarePageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AddSoftwarePageUrl.getName())) {
            return new AddSoftwarePage(new AddSoftwarePageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(SoftwareListPageUrl.getName())) {
            return new SoftwareListPage(new SoftwareListPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(UserContentAdminPageUrl.getName())) {
            return new UserContentAdminPageImplementation(new UserContentAdminPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(DocumentationPageUrl.getName())) {
            return new DocumentationPage(new DocumentationPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(TeamsPageUrl.getName())) {
            return new TeamsPage(new TeamsPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(TeamPageUrl.getName())) {
            return new TeamPage(new TeamPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CreateTeamPageUrl.getName())) {
            return new CreateTeamPage(new CreateTeamPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(JoinTeamPageUrl.getName())) {
            return new JoinTeamPage(new JoinTeamPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MessageListPageUrl.getName())) {
            return new MessageListPage(new MessageListPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(SendTeamInvitationPageUrl.getName())) {
            return new SendTeamInvitationPage(new SendTeamInvitationPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(KudosableAdminPageUrl.getName())) {
            return new KudosableAdminPageImplementation(new KudosableAdminPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(FeatureAdminPageUrl.getName())) {
            return new FeatureAdminPage(new FeatureAdminPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(BugPageUrl.getName())) {
            return new BugPage(new BugPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ReportBugPageUrl.getName())) {
            return new ReportBugPage(new ReportBugPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AddReleasePageUrl.getName())) {
            return new AddReleasePage(new AddReleasePageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ModifyBugPageUrl.getName())) {
            return new ModifyBugPage(new ModifyBugPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ReleasePageUrl.getName())) {
            return new ReleasePage(new ReleasePageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MilestoneAdminPageUrl.getName())) {
            return new MilestoneAdminPage(new MilestoneAdminPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MetaBugsListPageUrl.getName())) {
            return new MetaBugsListPage(new MetaBugsListPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MetaBugEditPageUrl.getName())) {
            return new MetaBugEditPage(new MetaBugEditPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ConfigurationAdminPageUrl.getName())) {
            return new ConfigurationAdminPage(new ConfigurationAdminPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AdminHomePageUrl.getName())) {
            return new AdminHomePage(new AdminHomePageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AddAttachementPageUrl.getName())) {
            return new AddAttachementPage(new AddAttachementPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ChangeLanguagePageUrl.getName())) {
            return new ChangeLanguagePage(new ChangeLanguagePageUrl(params, session.getParameters()));
        }

        // Actions
        if (pageCode.equals(LoginActionUrl.getName())) {
            return new LoginAction(new LoginActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(LogoutActionUrl.getName())) {
            return new LogoutAction(new LogoutActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ContributionActionUrl.getName())) {
            return new ContributionAction(new ContributionActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CheckContributionActionUrl.getName())) {
            return new CheckContributionAction(new CheckContributionActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(OfferActionUrl.getName())) {
            return new OfferAction(new OfferActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CreateFeatureActionUrl.getName())) {
            return new CreateFeatureAction(new CreateFeatureActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(SignUpActionUrl.getName())) {
            return new SignUpAction(new SignUpActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(PopularityVoteActionUrl.getName())) {
            return new PopularityVoteAction(new PopularityVoteActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CreateCommentActionUrl.getName())) {
            return new CreateCommentAction(new CreateCommentActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(PaylineActionUrl.getName())) {
            return new PaylineAction(new PaylineActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(PaylineNotifyActionUrl.getName())) {
            if(params.containsKey(PaylineNotifyAction.TOKEN_CODE)) {
                String token = params.look(PaylineNotifyAction.TOKEN_CODE).getSimpleValue();
                Session fakeSession = SessionManager.pickTemporarySession(token);
                if(fakeSession != null) {
                    Context.reInitializeContext(Context.getHeader(), fakeSession);
                }
            }
            return new PaylineNotifyAction(new PaylineNotifyActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CommentCommentActionUrl.getName())) {
            return new CommentCommentAction(new CommentCommentActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AddSoftwareActionUrl.getName())) {
            return new AddSoftwareAction(new AddSoftwareActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(UploadFileActionUrl.getName())) {
            return new UploadFileAction(new UploadFileActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MemberActivationActionUrl.getName())) {
            return new MemberActivationAction(new MemberActivationActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(PaylineReturnActionUrl.getName())) {
            return new PaylineReturnAction(new PaylineReturnActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AdministrationActionUrl.getName())) {
            return new AdministrationAction(new AdministrationActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CreateTeamActionUrl.getName())) {
            return new CreateTeamAction(new CreateTeamActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(JoinTeamActionUrl.getName())) {
            return new JoinTeamAction(new JoinTeamActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(SendTeamInvitationActionUrl.getName())) {
            return new SendTeamInvitationAction(new SendTeamInvitationActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(HandleJoinTeamInvitationActionUrl.getName())) {
            return new HandleJoinTeamInvitationAction(new HandleJoinTeamInvitationActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ReportBugActionUrl.getName())) {
            return new ReportBugAction(new ReportBugActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AddAttachementActionUrl.getName())) {
            return new AddAttachementAction(new AddAttachementActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AddReleaseActionUrl.getName())) {
            return new AddReleaseAction(new AddReleaseActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ModifyBugActionUrl.getName())) {
            return new ModifyBugAction(new ModifyBugActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ChangeAvatarActionUrl.getName())) {
            return new ChangeAvatarAction(new ChangeAvatarActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(GiveRightActionUrl.getName())) {
            return new GiveRightAction(new GiveRightActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ConfigurationAdminActionUrl.getName())) {
            return new ConfigurationAdminAction(new ConfigurationAdminActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MetaReportBugActionUrl.getName())) {
            return new MetaReportBugAction(new MetaReportBugActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MetaEditBugActionUrl.getName())) {
            return new MetaEditBugAction(new MetaEditBugActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(MetaBugDeleteActionUrl.getName())) {
            return new MetaBugDeleteAction(new MetaBugDeleteActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ChangeLanguageActionUrl.getName())) {
            return new ChangeLanguageAction(new ChangeLanguageActionUrl(params, session.getParameters()));
        }

        // Process
        if (pageCode.equals(ContributionProcessUrl.getName())) {
            return new ContributionProcess(new ContributionProcessUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AccountChargingProcessUrl.getName())) {
            return new AccountChargingProcess(new AccountChargingProcessUrl(params, session.getParameters()));
        }
        if (pageCode.equals(PaylineProcessUrl.getName())) {
            return new PaylineProcess(new PaylineProcessUrl(params, session.getParameters()));
        }


        // Resource page
        if (pageCode.equals(FileResourceUrl.getName())) {
            return new FileResource(new FileResourceUrl(params, session.getParameters()));
        }

        return new PageNotFound(new PageNotFoundUrl(params, session.getParameters()));
    }

    @Override
    public boolean initialize() {
        WebConfiguration.load();
        return true;
    }
}
