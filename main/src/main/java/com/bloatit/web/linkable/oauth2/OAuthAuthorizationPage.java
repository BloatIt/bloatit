package com.bloatit.web.linkable.oauth2;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.OAuthAuthorizationActionUrl;
import com.bloatit.web.url.OAuthAuthorizationPageUrl;

@ParamContainer("oauth/authorization")
public class OAuthAuthorizationPage extends LoggedPage {

    private final OAuthAuthorizationPageUrl url;

    /**
     * REQUIRED. Value MUST be set to "code".
     */
    @RequestParam(name = "response_type")
    @NonOptional(@tr("OAuth request need a %param% parameter."))
    private String responseType;

    /**
     * REQUIRED. The client identifier as described in Section 2.3.
     */
    @RequestParam(name = "client_id")
    @NonOptional(@tr("OAuth request need a %param% parameter."))
    private String clientId;

    /**
     * OPTIONAL, as described in Section 3.1.2.
     */
    @RequestParam(name = "redirect_uri")
    @Optional
    private String redirectUri;

    /**
     * OPTIONAL. The scope of the access request expressed as a list of
     * space-delimited, case sensitive strings. The value is defined by the
     * authorization server. If the value contains multiple space-delimited
     * strings, their order does not matter, and each string adds an additional
     * access range to the requested scope.
     */
    @Optional
    @RequestParam(name = "scope")
    private String scope;

    /**
     * OPTIONAL. An opaque value used by the client to maintain state between
     * the request and callback. The authorization server includes this value
     * when redirecting the user-agent back to the client.
     */
    @RequestParam(name = "state")
    @Optional
    private String state;

    public OAuthAuthorizationPage(final OAuthAuthorizationPageUrl url) {
        super(url);
        this.url = url;
        this.clientId = url.getClientId();
        this.redirectUri = url.getRedirectUri();
        this.responseType = url.getResponseType();
        this.scope = url.getScope();
        this.state = url.getState();
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        // TODO make this page pretty(er) !
        final HtmlDiv div = new HtmlDiv("oauth_question");
        div.add(new HtmlParagraph(Context.tr("Dear {0}, are you sure you want to grant ''{1}'' the right to access your Elveos account ?",
                                             loggedUser.getDisplayName(),
                                             clientId)));
        
        div.add(new HtmlLink(redirectUri, Context.tr("No")));
        final OAuthAuthorizationActionUrl targetUrl = new OAuthAuthorizationActionUrl(getSession().getShortKey(), responseType, clientId);
        targetUrl.setRedirectUri(redirectUri);
        targetUrl.setScope(scope);
        targetUrl.setState(state);
        div.add(new HtmlLink(targetUrl.urlString(), Context.tr("Yes, I trust {0}", clientId)));
        return div;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You have to be authenticated to grant access to your account.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return generateBreadcrumb(member, responseType, clientId);
    }

    public static Breadcrumb generateBreadcrumb(final Member member, final String responseType, final String clientId) {
        final Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(member);
        breadcrumb.pushLink(new OAuthAuthorizationPageUrl(responseType, clientId).getHtmlLink(Context.tr("oauth authorization")));
        return breadcrumb;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Authorize other application to access your elveos account");
    }

    @Override
    public boolean isStable() {
        return false;
    }

}
