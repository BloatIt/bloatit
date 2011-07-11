package com.bloatit.framework.oauth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;

public class OAuthAuthorizationAction {
    public static String doGet(final HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {

        final String redirectURI = "http://elveos.org";
        try {
            // dynamically recognize an OAuth profile based on request
            // characteristic (params,
            // method, content type etc.), perform validation
            new OAuthAuthzRequest(request); // Throw an error if it is not an
                                            // OAuthAuthzRequest

            // some code ....

            // TODO make a more secure Generator ?
            final OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new UUIDValueGenerator());

            // build OAuth response
            final OAuthResponse resp = OAuthASResponse.authorizationResponse(HttpServletResponse.SC_FOUND)
                                                      .setCode(oauthIssuerImpl.authorizationCode())
                                                      .location(redirectURI)
                                                      .buildQueryMessage();

            System.out.println(resp.getBody());
            System.out.println(resp.getResponseStatus());
            return resp.getLocationUri();

            // if something goes wrong
        } catch (final OAuthProblemException ex) {
            final OAuthResponse resp = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND).error(ex).location(redirectURI).buildQueryMessage();

            throw ex;
            // throw new RedirectException(new
            // UrlString(resp.getLocationUri()));
        }
    }

    public static void doPost(final HttpServletRequest request) throws OAuthSystemException  {

        OAuthTokenRequest oauthRequest = null;

        final OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        try {
            oauthRequest = new OAuthTokenRequest(request);

            final String authzCode = oauthRequest.getCode();

            // some code

            final String accessToken = oauthIssuerImpl.accessToken();
            final String refreshToken = oauthIssuerImpl.refreshToken();

            // some code

            final OAuthResponse r = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                                             .setAccessToken(accessToken)
                                             .setExpiresIn("3600")
                                             .setRefreshToken(refreshToken)
                                             .buildJSONMessage();

            System.out.println(r.getBody());
//            response.setStatus(r.getResponseStatus());
//            final PrintWriter pw = response.getWriter();
//            pw.print(r.getBody());
//            pw.flush();
//            pw.close();

            // if something goes wrong
        } catch (final OAuthProblemException ex) {

            final OAuthResponse r = OAuthResponse.errorResponse(401).error(ex).buildJSONMessage();


            System.err.println(r.getBody());
//            response.setStatus(r.getResponseStatus());
//            final PrintWriter pw = response.getWriter();
//            pw.print(r.getBody());
//            pw.flush();
//            pw.close();
//
//            response.sendError(401);
        }

    }

}
