package com.bloatit.framework.oauthprocessor;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.error.OAuthError;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.xcgiserver.HttpResponse;

public class OAuthGetToken extends OAuthBloatitReponse {

    private static final String SCOPE_PERMANENT_TOKEN = "permanent_token";

    private static final int DEFAULT_EXPIRE_TIME = 3600;

    private final OAuthAuthenticator authenticator;

    public OAuthGetToken(final OAuthAuthenticator authenticator) {
        super();
        this.authenticator = authenticator;
    }

    @Override
    public void process(final HttpServletRequest request, final HttpResponse response) throws IOException {

        OAuthTokenRequest oauthRequest = null;
        OAuthResponse oauthResponse;

        final OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        try {
            oauthRequest = new OAuthTokenRequest(request);
            final String authzCode = oauthRequest.getCode();
            final Set<String> scopes = oauthRequest.getScopes();
            int expiresIn = DEFAULT_EXPIRE_TIME;
            if (scopes.contains(SCOPE_PERMANENT_TOKEN)) {
                expiresIn = DateUtils.SECOND_PER_DAY * 3650;
            }

            final String accessToken = oauthIssuerImpl.accessToken();
            final String refreshToken = oauthIssuerImpl.refreshToken();
            try {
                authenticator.authorize(authzCode, accessToken, refreshToken, expiresIn);

                oauthResponse = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                                               .setAccessToken(accessToken)
                                               .setExpiresIn(String.valueOf(expiresIn))
                                               .setRefreshToken(refreshToken)
                                               .buildJSONMessage();
            } catch (final AuthorizationException e) {
                oauthResponse = OAuthResponse.errorResponse(401)
                                             .setError(OAuthError.TokenResponse.INVALID_GRANT)
                                             .setErrorDescription("Your authorization token is invalid.")
                                             .buildJSONMessage();
            }

            // if something goes wrong
        } catch (final OAuthProblemException ex) {
            try {
                oauthResponse = OAuthResponse.errorResponse(400).error(ex).buildJSONMessage();
            } catch (final OAuthSystemException e) {
                throw new BadProgrammerException(e);
            }

        } catch (final OAuthSystemException e) {
            throw new BadProgrammerException(e);
        }

        // write the response
        response.writeOAuth(oauthResponse.getResponseStatus(), oauthResponse.getHeaders(), oauthResponse.getBody());
    }
}
