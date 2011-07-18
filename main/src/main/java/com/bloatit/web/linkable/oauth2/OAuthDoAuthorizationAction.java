package com.bloatit.web.linkable.oauth2;

import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.ResponseType;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.web.url.OAuthDoAuthorizationActionUrl;

@ParamContainer("oauth/doauthorization")
public final class OAuthDoAuthorizationAction extends OAuthAuthorizationAction {

    private final OAuthDoAuthorizationActionUrl url;

    public OAuthDoAuthorizationAction(final OAuthDoAuthorizationActionUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected Url processOAuthRequest(final String clientId, //
                                      final String redirectUri, //
                                      final String responseType, //
                                      final String state) throws OAuthSystemException {
        
        final OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(HttpServletResponse.SC_FOUND);

        // TODO make a more secure Generator ?
        final OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new UUIDValueGenerator());

        if (responseType.equals(ResponseType.CODE.toString())) {
            builder.setCode(oauthIssuerImpl.authorizationCode());
        } else if (responseType.equals(ResponseType.TOKEN.toString())) {
            builder.setAccessToken(oauthIssuerImpl.accessToken());
            builder.setExpiresIn(String.valueOf(3600));
        }

        final OAuthResponse response = builder.location(redirectUri).buildQueryMessage();
        builder.setState(state);

        return new UrlString(response.getLocationUri());
    }
}
