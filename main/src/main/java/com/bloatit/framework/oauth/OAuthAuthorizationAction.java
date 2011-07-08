package com.bloatit.framework.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.url.UrlString;


public class OAuthAuthorizationAction {
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        try {
            // dynamically recognize an OAuth profile based on request
            // characteristic (params,
            // method, content type etc.), perform validation
            new OAuthAuthzRequest(request); // Throw an error if it is not an OAuthAuthzRequest

            // some code ....

            // TODO make a more secure Generator ?
            final OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new UUIDValueGenerator());
            
            // build OAuth response
            final OAuthResponse resp = OAuthASResponse.authorizationResponse(HttpServletResponse.SC_FOUND)
                                                      .setCode(oauthIssuerImpl.authorizationCode())
                                                      .location(redirectURI)
                                                      .buildQueryMessage();

            // response.sendRedirect(resp.getLocationUri());

            // if something goes wrong
        } catch (final OAuthProblemException ex) {
            final OAuthResponse resp = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                                      .error(ex)
                                                      .location(redirectUri)
                                                      .buildQueryMessage();

            throw new RedirectException(new UrlString(resp.getLocationUri()));
        }

    }
}
