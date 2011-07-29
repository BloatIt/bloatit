package com.bloatit.framework.oauthprocessor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.common.error.OAuthError;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;


import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.xcgiserver.HttpResponse;

class OAuthDenyAuthorization extends OAuthBloatitReponse {

    @Override
    public void process(HttpServletRequest request, HttpResponse response) throws IOException {

        OAuthResponse oauthResponse;
        try {
            // Build a request and fail if it is not a valid OAUTH request
            final OAuthAuthzRequest oAuthAuthzRequest = new OAuthAuthzRequest(request);

            // Get oauth parameters
            String redirectUri = oAuthAuthzRequest.getRedirectURI();
            String state = oAuthAuthzRequest.getState();

            oauthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                         .setState(state)
                                         .setError(OAuthError.CodeResponse.ACCESS_DENIED)
                                         .setErrorDescription("The user refused to grant access to your service.")
                                         .location(redirectUri)
                                         .buildQueryMessage();

        } catch (final OAuthProblemException ex) {
            try {
                oauthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND).error(ex).buildQueryMessage();
            } catch (final OAuthSystemException e) {
                throw new BadProgrammerException(ex);
            }
        } catch (final OAuthSystemException e) {
            throw new BadProgrammerException(e);
        }

        // write the response
        String locationUri = oauthResponse.getLocationUri();
        if (locationUri == null || locationUri.isEmpty()) {
            locationUri = "pagenotfound";
        }
        response.writeOAuthRedirect(oauthResponse.getResponseStatus(), locationUri);
    }
}
