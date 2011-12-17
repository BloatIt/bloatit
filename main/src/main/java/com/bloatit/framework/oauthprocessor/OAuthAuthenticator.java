package com.bloatit.framework.oauthprocessor;

import java.util.Set;

import com.bloatit.data.exceptions.ElementNotFoundException;
import com.bloatit.framework.oauthprocessor.OAuthBloatitReponse.AuthorizationException;

public abstract class OAuthAuthenticator {

    protected abstract void addExternalService(String clientId, String login, String password, final String token, Set<String> scopes)
            throws ElementNotFoundException;

    protected abstract void authorize(String authzCode, String accessToken, String refreshToken, int expiresIn) throws AuthorizationException;

}
