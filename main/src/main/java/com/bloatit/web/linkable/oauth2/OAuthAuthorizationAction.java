package com.bloatit.web.linkable.oauth2;

import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.framework.xcgiserver.HttpBloatitRequest;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.OAuthAuthorizationActionUrl;

@ParamContainer("oauth/doauthorization")
public class OAuthAuthorizationAction extends LoggedAction {

    /**
     * REQUIRED. Value MUST be set to "code".
     */
    @RequestParam(name = "response_type")
    @NonOptional(@tr("OAuth request need a %param% parameter."))
    private final String responseType;

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
    private final String scope;

    /**
     * OPTIONAL. An opaque value used by the client to maintain state between
     * the request and callback. The authorization server includes this value
     * when redirecting the user-agent back to the client.
     */
    @RequestParam(name = "state")
    @Optional
    private final String state;

    private final OAuthAuthorizationActionUrl url;

    public OAuthAuthorizationAction(final OAuthAuthorizationActionUrl url) {
        super(url);
        this.url = url;
        this.clientId = url.getClientId();
        this.redirectUri = url.getRedirectUri();
        this.responseType = url.getResponseType();
        this.scope = url.getScope();
        this.state = url.getState();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        final HttpBloatitRequest request = new HttpBloatitRequest(Context.getHeader(), url.getStringParameters());
        try {
            final OAuthAuthzRequest oAuthAuthzRequest = new OAuthAuthzRequest(request);
            clientId = oAuthAuthzRequest.getClientId();
            redirectUri = oAuthAuthzRequest.getRedirectURI();

            // TODO make a more secure Generator ?
            final OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new UUIDValueGenerator());

            // build OAuth response
            final OAuthResponse resp = OAuthASResponse.authorizationResponse(HttpServletResponse.SC_FOUND)
                                                      .setCode(oauthIssuerImpl.authorizationCode())
                                                      .location(redirectUri)
                                                      .buildQueryMessage();
            redirectUri = resp.getLocationUri();

            // if something goes wrong
        } catch (final OAuthProblemException ex) {
            try {
                final OAuthResponse resp = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                                        .error(ex)
                                                        .location(redirectUri)
                                                        .buildQueryMessage();
                redirectUri = resp.getLocationUri();
            } catch (final OAuthSystemException e) {
                throw new BadProgrammerException(ex);
            }
        } catch (final OAuthSystemException e) {
            throw new BadProgrammerException(e);
        }
        return new UrlString(redirectUri);
    }

    @Override
    protected Url doProcessErrors() {
        return new UrlString(redirectUri);
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You have to be authenticated to perform a right delegation");
    }

    @Override
    protected void transmitParameters() {
        // Nothing ... If it fails then it's a bad programmer who gets us here
    }

}
