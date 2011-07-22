package com.bloatit.framework.oauthprocessor;

import java.io.IOException;

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
import com.bloatit.framework.xcgiserver.HttpReponseField.StatusCode;
import com.bloatit.framework.xcgiserver.HttpResponse;

public abstract class OAuthGetToken extends OAuthBloatitReponse {

    @Override
    public void process(HttpServletRequest request, HttpResponse response) throws IOException {

        OAuthTokenRequest oauthRequest = null;
        OAuthResponse oauthResponse;

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        try {
            oauthRequest = new OAuthTokenRequest(request);
            String authzCode = oauthRequest.getCode();

            String accessToken = oauthIssuerImpl.accessToken();
            String refreshToken = oauthIssuerImpl.refreshToken();
            int expiresIn = 3600;

            try {
                authorizeService(authzCode, accessToken, refreshToken, expiresIn);

                oauthResponse = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                                               .setAccessToken(accessToken)
                                               .setExpiresIn(String.valueOf(3600))
                                               .setRefreshToken(refreshToken)
                                               .buildJSONMessage();
            } catch (AuthorizationException e) {
                oauthResponse = OAuthASResponse.errorResponse(401)
                                               .setError(OAuthError.TokenResponse.INVALID_GRANT)
                                               .setErrorDescription("Your authorization token is invalid.")
                                               .buildJSONMessage();
            }

            // if something goes wrong
        } catch (OAuthProblemException ex) {
            try {
                oauthResponse = OAuthResponse.errorResponse(400).error(ex).buildJSONMessage();
            } catch (OAuthSystemException e) {
                throw new BadProgrammerException(e);
            }

        } catch (final OAuthSystemException e) {
            throw new BadProgrammerException(e);
        }

        // write the response
        response.writeOAuth(StatusCode.REDIRECTION_302_FOUND, oauthResponse.getHeaders(), oauthResponse.getBody());
    }

    protected abstract void authorizeService(String authzCode, String accessToken, String refreshToken, int expiresIn) throws AuthorizationException;

}
