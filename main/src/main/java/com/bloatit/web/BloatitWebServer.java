package com.bloatit.web;

import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.WebServer;
import com.bloatit.framework.webserver.masters.Linkable;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.web.actions.CommentCommentAction;
import com.bloatit.web.actions.ContributionAction;
import com.bloatit.web.actions.CreateDemandAction;
import com.bloatit.web.actions.IdeaCommentAction;
import com.bloatit.web.actions.KudoAction;
import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.actions.OfferAction;
import com.bloatit.web.actions.PaylineAction;
import com.bloatit.web.actions.PaylineNotifyAction;
import com.bloatit.web.actions.RegisterAction;
import com.bloatit.web.pages.AccountChargingPage;
import com.bloatit.web.pages.CommentReplyPage;
import com.bloatit.web.pages.ContributePage;
import com.bloatit.web.pages.CreateDemandPage;
import com.bloatit.web.pages.DemandListPage;
import com.bloatit.web.pages.FileUploadPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.pages.MemberPage;
import com.bloatit.web.pages.MembersListPage;
import com.bloatit.web.pages.MyAccountPage;
import com.bloatit.web.pages.OfferPage;
import com.bloatit.web.pages.PageNotFound;
import com.bloatit.web.pages.PaylinePage;
import com.bloatit.web.pages.RegisterPage;
import com.bloatit.web.pages.SpecialsPage;
import com.bloatit.web.pages.TestPage;
import com.bloatit.web.pages.demand.DemandPage;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.CommentCommentActionUrl;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.CreateDemandActionUrl;
import com.bloatit.web.url.CreateDemandPageUrl;
import com.bloatit.web.url.DemandListPageUrl;
import com.bloatit.web.url.DemandPageUrl;
import com.bloatit.web.url.FileUploadPageUrl;
import com.bloatit.web.url.IdeaCommentActionUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.KudoActionUrl;
import com.bloatit.web.url.LoginActionUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LogoutActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.MyAccountPageUrl;
import com.bloatit.web.url.OfferActionUrl;
import com.bloatit.web.url.OfferPageUrl;
import com.bloatit.web.url.PaylineActionUrl;
import com.bloatit.web.url.PaylineNotifyActionUrl;
import com.bloatit.web.url.PaylinePageUrl;
import com.bloatit.web.url.RegisterActionUrl;
import com.bloatit.web.url.RegisterPageUrl;
import com.bloatit.web.url.SpecialsPageUrl;
import com.bloatit.web.url.TestPageUrl;

public class BloatitWebServer extends WebServer {

    public BloatitWebServer() {
        super();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Linkable constructLinkable(String pageCode, Parameters params, Session session) {

        //Pages
        if (pageCode.equals(IndexPageUrl.getName())) {
            return new IndexPage(new IndexPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(LoginPageUrl.getName())) {
            return new LoginPage(new LoginPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(DemandListPageUrl.getName())) {
            return new DemandListPage(new DemandListPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(CreateDemandPageUrl.getName())) {
            return new CreateDemandPage(new CreateDemandPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(DemandPageUrl.getName())) {
            return new DemandPage(new DemandPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(MyAccountPageUrl.getName())) {
            return new MyAccountPage(new MyAccountPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(SpecialsPageUrl.getName())) {
            return new SpecialsPage(new SpecialsPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(MembersListPageUrl.getName())) {
            return new MembersListPage(new MembersListPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(MemberPageUrl.getName())) {
            return new MemberPage(new MemberPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(ContributePageUrl.getName())) {
            return new ContributePage(new ContributePageUrl(params, session.getParams()));
        }
        if (pageCode.equals(OfferPageUrl.getName())) {
            return new OfferPage(new OfferPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(TestPageUrl.getName())) {
            return new TestPage(new TestPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(AccountChargingPageUrl.getName())) {
            return new AccountChargingPage(new AccountChargingPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(RegisterPageUrl.getName())) {
            return new RegisterPage(new RegisterPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(PaylinePageUrl.getName())) {
            return new PaylinePage(new PaylinePageUrl(params, session.getParams()));
        }
        if (pageCode.equals(CommentReplyPageUrl.getName())) {
            return new CommentReplyPage(new CommentReplyPageUrl(params, session.getParams()));
        }
        if (pageCode.equals(FileUploadPageUrl.getName())) {
            return new FileUploadPage(new FileUploadPageUrl(params, session.getParams()));
        }

        //Actions
        if (pageCode.equals(LoginActionUrl.getName())) {
            return new LoginAction(new LoginActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(LogoutActionUrl.getName())) {
            return new LogoutAction(new LogoutActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(ContributionActionUrl.getName())) {
            return new ContributionAction(new ContributionActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(OfferActionUrl.getName())) {
            return new OfferAction(new OfferActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(CreateDemandActionUrl.getName())) {
            return new CreateDemandAction(new CreateDemandActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(RegisterActionUrl.getName())) {
            return new RegisterAction(new RegisterActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(KudoActionUrl.getName())) {
            return new KudoAction(new KudoActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(IdeaCommentActionUrl.getName())) {
            return new IdeaCommentAction(new IdeaCommentActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(PaylineActionUrl.getName())) {
            return new PaylineAction(new PaylineActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(PaylineNotifyActionUrl.getName())) {
            return new PaylineNotifyAction(new PaylineNotifyActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(IdeaCommentActionUrl.getName())) {
            return new IdeaCommentAction(new IdeaCommentActionUrl(params, session.getParams()));
        }
        if (pageCode.equals(CommentCommentActionUrl.getName())) {
            return new CommentCommentAction(new CommentCommentActionUrl(params, session.getParams()));
        }


        return new PageNotFound(new PageNotFoundUrl(params, session.getParams()));
    }

}
