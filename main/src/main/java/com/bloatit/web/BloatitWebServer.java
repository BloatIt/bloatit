package com.bloatit.web;

import com.bloatit.framework.webserver.WebServer;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.CommentCommentActionUrl;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.CreateIdeaActionUrl;
import com.bloatit.web.url.CreateDemandPageUrl;
import com.bloatit.web.url.DemandListUrl;
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
        addLinkable(IndexPageUrl.getName(), IndexPageUrl.class);
        addLinkable(LoginPageUrl.getName(), LoginPageUrl.class);
        addLinkable(DemandListUrl.getName(), DemandListUrl.class);
        addLinkable(CreateDemandPageUrl.getName(), CreateDemandPageUrl.class);
        addLinkable(DemandPageUrl.getName(), DemandPageUrl.class);
        addLinkable(MyAccountPageUrl.getName(), MyAccountPageUrl.class);
        addLinkable(SpecialsPageUrl.getName(), SpecialsPageUrl.class);
        addLinkable(MembersListPageUrl.getName(), MembersListPageUrl.class);
        addLinkable(MemberPageUrl.getName(), MemberPageUrl.class);
        addLinkable(ContributePageUrl.getName(), ContributePageUrl.class);
        addLinkable(OfferPageUrl.getName(), OfferPageUrl.class);
        addLinkable(TestPageUrl.getName(), TestPageUrl.class);
        addLinkable(AccountChargingPageUrl.getName(), AccountChargingPageUrl.class);
        addLinkable(RegisterPageUrl.getName(), RegisterPageUrl.class);
        addLinkable(PaylinePageUrl.getName(), PaylinePageUrl.class);
        addLinkable(CommentReplyPageUrl.getName(), CommentReplyPageUrl.class);
        addLinkable(FileUploadPageUrl.getName(), FileUploadPageUrl.class);

        addLinkable(LoginActionUrl.getName(), LoginActionUrl.class);
        addLinkable(LogoutActionUrl.getName(), LogoutActionUrl.class);
        addLinkable(ContributionActionUrl.getName(), ContributionActionUrl.class);
        addLinkable(OfferActionUrl.getName(), OfferActionUrl.class);
        addLinkable(CreateIdeaActionUrl.getName(), CreateIdeaActionUrl.class);
        addLinkable(RegisterActionUrl.getName(), RegisterActionUrl.class);
        addLinkable(KudoActionUrl.getName(), KudoActionUrl.class);
        addLinkable(IdeaCommentActionUrl.getName(), IdeaCommentActionUrl.class);
        addLinkable(PaylineActionUrl.getName(), PaylineActionUrl.class);
        addLinkable(PaylineNotifyActionUrl.getName(), PaylineNotifyActionUrl.class);
        addLinkable(IdeaCommentActionUrl.getName(), IdeaCommentActionUrl.class);
        addLinkable(CommentCommentActionUrl.getName(), CommentCommentActionUrl.class);
    }

}
