package com.bloatit.web.linkable.oauth2;

import java.util.Map.Entry;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.oauthprocessor.OAuthProcessor;
import com.bloatit.framework.utils.parameters.HttpParameter;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ExternalService;
import com.bloatit.model.managers.ExternalServiceManager;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.OAuthAuthorizationPageUrl;

@ParamContainer(OAuthProcessor.OAUTH_GET_CREDENTIAL_PAGENAME)
public class OAuthAuthorizationPage extends ElveosPage {

    private final OAuthAuthorizationPageUrl url;

    /**
     * REQUIRED. Value MUST be set to "code".
     */
    @RequestParam(name = OAuth.OAUTH_RESPONSE_TYPE)
    @NonOptional(@tr("OAuth request need a %paramName% parameter."))
    private final String responseType;

    /**
     * REQUIRED. The client identifier as described in Section 2.3.
     */
    @RequestParam(name = OAuth.OAUTH_CLIENT_ID)
    @NonOptional(@tr("OAuth request need a %paramName% parameter."))
    private String clientId;

    /**
     * OPTIONAL, as described in Section 3.1.2.
     */
    // FIXME: I am non optional because I don't know what to do if there is no
    // redirectUri
    @RequestParam(name = OAuth.OAUTH_REDIRECT_URI)
    @NonOptional(@tr("OAuth request need a %paramName% parameter."))
    private String redirectUri;

    /**
     * OPTIONAL. The scope of the access request expressed as a list of
     * space-delimited, case sensitive strings. The value is defined by the
     * authorization server. If the value contains multiple space-delimited
     * strings, their order does not matter, and each string adds an additional
     * access range to the requested scope.
     */
    @Optional
    @RequestParam(name = OAuth.OAUTH_SCOPE)
    private final String scope;

    /**
     * OPTIONAL. An opaque value used by the client to maintain state between
     * the request and callback. The authorization server includes this value
     * when redirecting the user-agent back to the client.
     */
    @RequestParam(name = OAuth.OAUTH_STATE)
    @Optional
    private final String state;

    @RequestParam(name = "fail")
    @Optional
    private final Boolean fail;

    public OAuthAuthorizationPage(final OAuthAuthorizationPageUrl url) {
        super(url);
        this.url = url;
        this.clientId = url.getClientId();
        this.redirectUri = url.getRedirectUri();
        this.responseType = url.getResponseType();
        this.scope = url.getScope();
        this.state = url.getState();
        this.fail = url.getFail();
    }

    @Override
    public HtmlElement createBodyContent() throws RedirectException {

        if (fail != null && fail) {
            getSession().notifyError(Context.tr("Wrong login or password, please retry."));
        }

        ExternalService service = ExternalServiceManager.getByToken(clientId);
        if (service == null) {
            getSession().notifyError(Context.tr("Service not found!"));
            throw new RedirectException(new IndexPageUrl());
        }

        String yesUrl = createYesUrl();

        // TODO make this page pretty(er) !
        final HtmlDiv div = new HtmlDiv("oauth_question");
        {
            final HtmlTitleBlock loginTitle = new HtmlTitleBlock(Context.tr("Enter your loggin and password to grant ''{0}'' the right to access your Elveos account ?",
                                                                            service.getDescription().getDefaultTranslation().getTitle()),
                                                                 1);
            div.add(loginTitle);

            final HtmlForm loginForm = new HtmlForm(yesUrl);
            div.add(loginForm);

            // Login field
            final HtmlTextField loginInput = new HtmlTextField(OAuthProcessor.LOGIN_CODE, Context.trc("Login (noun)", "Login"));
            loginForm.add(loginInput);

            // passwordField
            final HtmlPasswordField passwordInput = new HtmlPasswordField(OAuthProcessor.PASSWORD_CODE, Context.tr("Password"));
            loginForm.add(passwordInput);

            // Submit
            final HtmlDiv yesOrNoDiv = new HtmlDiv("login_or_signup");
            loginForm.add(yesOrNoDiv);
            final HtmlSubmit submitButton = new HtmlSubmit(Context.tr("Yes, I trust {0}", service.getDescription().getDefaultTranslation().getTitle()));
            // submitButton.setCssClass("button_good");
            yesOrNoDiv.add(new HtmlLink("/oauth/" + OAuthProcessor.DENY_AUTHORIZATION_PAGE_NAME, Context.tr("No")).setCssClass("button_bad"));
            yesOrNoDiv.add(submitButton);

        }
        return div;
    }

    private String createYesUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("/oauth/");
        sb.append(OAuthProcessor.GET_AUTHORIZATION_PAGE_NAME);
        sb.append("?");
        final URLCodec urlCodec = new URLCodec();
        for (Entry<String, HttpParameter> param : url.getStringParameters().entrySet()) {
            for (String value : param.getValue()) {
                sb.append(param.getKey());
                sb.append("=");
                try {
                    sb.append(urlCodec.encode(value));
                } catch (EncoderException e) {
                    throw new ExternalErrorException(e);
                }
                sb.append("&");
            }
        }
        return sb.toString();
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return generateBreadcrumb(responseType, clientId, redirectUri);
    }

    public static Breadcrumb generateBreadcrumb(final String responseType, final String clientId, final String redirectUri) {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new OAuthAuthorizationPageUrl(responseType, clientId, redirectUri).getHtmlLink(Context.tr("oauth authorization")));
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
