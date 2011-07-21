package com.bloatit.web.linkable.oauth2;

import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.common.error.OAuthError;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.model.Member;
import com.bloatit.web.url.OAuthDenyAuthorizationActionUrl;

@ParamContainer("oauth/dodenyauthorization")
public class OAuthDenyAuthorizationAction extends OAuthAuthorizationAction {

    private final OAuthDenyAuthorizationActionUrl url;

    public OAuthDenyAuthorizationAction(final OAuthDenyAuthorizationActionUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected Url processOAuthRequest(final Member member, //
                                      final String clientId, //
                                      final String redirectUri,//
                                      final String responseType, //
                                      final String state) throws OAuthSystemException {
        
        final OAuthResponse resp = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                                .setState(state)
                                                .setError(OAuthError.CodeResponse.ACCESS_DENIED)
                                                .setErrorDescription("The user refused to grant access to your service.")
                                                .location(redirectUri)
                                                .buildQueryMessage();
        
        return new UrlString(resp.getLocationUri());
    }
}
