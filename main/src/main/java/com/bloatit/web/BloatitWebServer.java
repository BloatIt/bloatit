package com.bloatit.web;

import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.WebServer;
import com.bloatit.framework.webserver.masters.Linkable;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.web.actions.AddAttachementAction;
import com.bloatit.web.actions.AddProjectAction;
import com.bloatit.web.actions.AdministrationAction;
import com.bloatit.web.actions.CommentCommentAction;
import com.bloatit.web.actions.ContributionAction;
import com.bloatit.web.actions.CreateCommentAction;
import com.bloatit.web.actions.CreateDemandAction;
import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.actions.MemberActivationAction;
import com.bloatit.web.actions.OfferAction;
import com.bloatit.web.actions.PaylineAction;
import com.bloatit.web.actions.PaylineNotifyAction;
import com.bloatit.web.actions.PaylineReturnAction;
import com.bloatit.web.actions.PopularityVoteAction;
import com.bloatit.web.actions.RegisterAction;
import com.bloatit.web.actions.ReportBugAction;
import com.bloatit.web.actions.UploadFileAction;
import com.bloatit.web.pages.AccountChargingPage;
import com.bloatit.web.pages.AddProjectPage;
import com.bloatit.web.pages.BugPage;
import com.bloatit.web.pages.CommentReplyPage;
import com.bloatit.web.pages.ContributePage;
import com.bloatit.web.pages.CreateDemandPage;
import com.bloatit.web.pages.DemandListPage;
import com.bloatit.web.pages.Documentation;
import com.bloatit.web.pages.FileUploadPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.pages.MemberPage;
import com.bloatit.web.pages.MembersListPage;
import com.bloatit.web.pages.OfferPage;
import com.bloatit.web.pages.PageNotFound;
import com.bloatit.web.pages.PaylinePage;
import com.bloatit.web.pages.ProjectListPage;
import com.bloatit.web.pages.ProjectPage;
import com.bloatit.web.pages.RegisterPage;
import com.bloatit.web.pages.ReportBugPage;
import com.bloatit.web.pages.SpecialsPage;
import com.bloatit.web.pages.TestPage;
import com.bloatit.web.pages.admin.DemandAdminPage;
import com.bloatit.web.pages.admin.KudosableAdminPageImplementation;
import com.bloatit.web.pages.admin.UserContentAdminPageImplementation;
import com.bloatit.web.pages.demand.DemandPage;
import com.bloatit.web.pages.messages.MessageListPage;
import com.bloatit.web.pages.team.CreateTeamAction;
import com.bloatit.web.pages.team.CreateTeamPage;
import com.bloatit.web.pages.team.HandleJoinGroupInvitationAction;
import com.bloatit.web.pages.team.JoinTeamAction;
import com.bloatit.web.pages.team.JoinTeamPage;
import com.bloatit.web.pages.team.SendGroupInvitationAction;
import com.bloatit.web.pages.team.SendGroupInvitationPage;
import com.bloatit.web.pages.team.TeamPage;
import com.bloatit.web.pages.team.TeamsPage;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.AddAttachementActionUrl;
import com.bloatit.web.url.AddProjectActionUrl;
import com.bloatit.web.url.AddProjectPageUrl;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.CommentCommentActionUrl;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.CreateCommentActionUrl;
import com.bloatit.web.url.CreateDemandActionUrl;
import com.bloatit.web.url.CreateDemandPageUrl;
import com.bloatit.web.url.CreateTeamActionUrl;
import com.bloatit.web.url.CreateTeamPageUrl;
import com.bloatit.web.url.DemandAdminPageUrl;
import com.bloatit.web.url.DemandListPageUrl;
import com.bloatit.web.url.DemandPageUrl;
import com.bloatit.web.url.DocumentationUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.FileUploadPageUrl;
import com.bloatit.web.url.HandleJoinGroupInvitationActionUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.JoinTeamPageUrl;
import com.bloatit.web.url.KudosableAdminPageUrl;
import com.bloatit.web.url.LoginActionUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LogoutActionUrl;
import com.bloatit.web.url.MemberActivationActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.MessageListPageUrl;
import com.bloatit.web.url.OfferActionUrl;
import com.bloatit.web.url.OfferPageUrl;
import com.bloatit.web.url.PaylineActionUrl;
import com.bloatit.web.url.PaylineNotifyActionUrl;
import com.bloatit.web.url.PaylinePageUrl;
import com.bloatit.web.url.PaylineReturnActionUrl;
import com.bloatit.web.url.PopularityVoteActionUrl;
import com.bloatit.web.url.ProjectListPageUrl;
import com.bloatit.web.url.ProjectPageUrl;
import com.bloatit.web.url.RegisterActionUrl;
import com.bloatit.web.url.RegisterPageUrl;
import com.bloatit.web.url.ReportBugActionUrl;
import com.bloatit.web.url.ReportBugPageUrl;
import com.bloatit.web.url.SendGroupInvitationActionUrl;
import com.bloatit.web.url.SendGroupInvitationPageUrl;
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
    protected Linkable constructLinkable(final String pageCode, final Parameters params, final Session session) {

        // Pages
        if (pageCode.equals(IndexPageUrl.getName())) {
            return new IndexPage(new IndexPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(LoginPageUrl.getName())) {
            return new LoginPage(new LoginPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(DemandListPageUrl.getName())) {
            return new DemandListPage(new DemandListPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CreateDemandPageUrl.getName())) {
            return new CreateDemandPage(new CreateDemandPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(DemandPageUrl.getName())) {
            return new DemandPage(new DemandPageUrl(params, session.getParameters()));
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
        if (pageCode.equals(OfferPageUrl.getName())) {
            return new OfferPage(new OfferPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(TestPageUrl.getName())) {
            return new TestPage(new TestPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AccountChargingPageUrl.getName())) {
            return new AccountChargingPage(new AccountChargingPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(RegisterPageUrl.getName())) {
            return new RegisterPage(new RegisterPageUrl(params, session.getParameters()));
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
        if (pageCode.equals(ProjectPageUrl.getName())) {
            return new ProjectPage(new ProjectPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AddProjectPageUrl.getName())) {
            return new AddProjectPage(new AddProjectPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ProjectListPageUrl.getName())) {
            return new ProjectListPage(new ProjectListPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(UserContentAdminPageUrl.getName())) {
            return new UserContentAdminPageImplementation(new UserContentAdminPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(DocumentationUrl.getName())) {
            return new Documentation(new DocumentationUrl(params, session.getParameters()));
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
        if (pageCode.equals(SendGroupInvitationPageUrl.getName())) {
            return new SendGroupInvitationPage(new SendGroupInvitationPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(KudosableAdminPageUrl.getName())) {
            return new KudosableAdminPageImplementation(new KudosableAdminPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(DemandAdminPageUrl.getName())) {
            return new DemandAdminPage(new DemandAdminPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(BugPageUrl.getName())) {
            return new BugPage(new BugPageUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ReportBugPageUrl.getName())) {
            return new ReportBugPage(new ReportBugPageUrl(params, session.getParameters()));
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
        if (pageCode.equals(OfferActionUrl.getName())) {
            return new OfferAction(new OfferActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CreateDemandActionUrl.getName())) {
            return new CreateDemandAction(new CreateDemandActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(RegisterActionUrl.getName())) {
            return new RegisterAction(new RegisterActionUrl(params, session.getParameters()));
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
            return new PaylineNotifyAction(new PaylineNotifyActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(CommentCommentActionUrl.getName())) {
            return new CommentCommentAction(new CommentCommentActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AddProjectActionUrl.getName())) {
            return new AddProjectAction(new AddProjectActionUrl(params, session.getParameters()));
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
        if (pageCode.equals(SendGroupInvitationActionUrl.getName())) {
            return new SendGroupInvitationAction(new SendGroupInvitationActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(HandleJoinGroupInvitationActionUrl.getName())) {
            return new HandleJoinGroupInvitationAction(new HandleJoinGroupInvitationActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(ReportBugActionUrl.getName())) {
            return new ReportBugAction(new ReportBugActionUrl(params, session.getParameters()));
        }
        if (pageCode.equals(AddAttachementActionUrl.getName())) {
            return new AddAttachementAction(new AddAttachementActionUrl(params, session.getParameters()));
        }




        // Resource page
        if (pageCode.equals(FileResourceUrl.getName())) {
            return new FileResource(new FileResourceUrl(params, session.getParameters()));
        }

        return new PageNotFound(new PageNotFoundUrl(params, session.getParameters()));
    }
}
