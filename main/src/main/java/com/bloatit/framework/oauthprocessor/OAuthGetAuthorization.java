package com.bloatit.framework.oauthprocessor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.ResponseType;

import com.bloatit.common.Log;
import com.bloatit.data.exceptions.ElementNotFoundException;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.xcgiserver.HttpResponse;

public class OAuthGetAuthorization extends OAuthBloatitReponse {

    private static final int DEFAULT_EXPIRE_TIME = 3600;

    private OAuthAuthenticator authenticator;

    public OAuthGetAuthorization(OAuthAuthenticator authenticator) {
        super();
        this.authenticator = authenticator;
    }

    @Override
    public void process(HttpServletRequest request, HttpResponse response) throws IOException {
        try {
            doProcess(request, response);
        } catch (OAuthSystemException e) {
            throw new ExternalErrorException(e);
        }
    }

    private void doProcess(HttpServletRequest request, HttpResponse response) throws OAuthSystemException, IOException {
        OAuthResponse oauthResponse;
        String state = request.getParameter(OAuth.OAUTH_STATE);
        String redirectUri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
        try {
            // Build a request and fail if it is not a valid OAUTH request
            final OAuthAuthzRequest oAuthAuthzRequest = new OAuthAuthzRequest(request);

            // Get oauth parameters
            String clientId = oAuthAuthzRequest.getClientId();
            String responseType = oAuthAuthzRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            redirectUri = oAuthAuthzRequest.getRedirectURI();
            state = oAuthAuthzRequest.getState();
            if (redirectUri == null || redirectUri.isEmpty()) {
                redirectUri = "pagenotfound";
            }

            String login = request.getParameter("login");
            String password = request.getParameter("password");

            final OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(HttpServletResponse.SC_FOUND);

            // TODO make a more secure Generator ?
            final OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new UUIDValueGenerator());

            if (responseType.equals(ResponseType.CODE.toString())) {
                // Create the token
                final String token = oauthIssuerImpl.authorizationCode();
                // build response
                builder.setCode(token);
                // Add the external service
                authenticator.addExternalService(clientId, login, password, token);

            } else if (responseType.equals(ResponseType.TOKEN.toString())) {
                // Create tokens
                final String token = oauthIssuerImpl.accessToken();
                final String refresh = oauthIssuerImpl.refreshToken();
                // build response
                builder.setAccessToken(token);
                builder.setParam(OAuth.OAUTH_REFRESH_TOKEN, refresh);
                builder.setExpiresIn(String.valueOf(DEFAULT_EXPIRE_TIME));
                // Add in external services
                authenticator.addExternalService(clientId, login, password, token);
                authenticator.authorize(token, token, refresh, DEFAULT_EXPIRE_TIME);
            }

            // Finish the construction
            oauthResponse = builder.location(redirectUri).buildQueryMessage();

        } catch (final OAuthProblemException ex) {
            oauthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                         .error(ex)
                                         .setState(state)
                                         .location(redirectUri)
                                         .buildQueryMessage();

        } catch (AuthorizationException e) {
            oauthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                         .setError("server_error")
                                         .setErrorDescription("Internal error. Please report.")
                                         .setState(state)
                                         .location(redirectUri)
                                         .buildQueryMessage();

            Log.framework().error("Cannot found a just added service ...", e);
        } catch (ElementNotFoundException e) {
            response.writeOAuthRedirect(302, "/" + OAuthProcessor.OAUTH_GET_CREDENTIAL_PAGENAME + "?fail=true&" + request.getQueryString());
            return;
        } catch (final OAuthSystemException e) {
            throw new BadProgrammerException(e);
        }

        // write the response
        response.writeOAuthRedirect(oauthResponse.getResponseStatus(), oauthResponse.getLocationUri());
    }

}
