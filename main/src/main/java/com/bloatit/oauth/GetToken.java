package com.bloatit.oauth;

import com.bloatit.framework.oauthprocessor.OAuthGetToken;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.model.ExternalServiceMembership;
import com.bloatit.model.managers.ExternalServiceManager;

public class GetToken extends OAuthGetToken {

    protected void authorizeService(String authzCode, String accessToken, String refreshToken, int expiresIn) throws AuthorizationException {
        ExternalServiceMembership serviceMembership = ExternalServiceManager.getMembershipByToken(authzCode);
        if (serviceMembership != null && !serviceMembership.isValid()) {
            throw new AuthorizationException();
        }
        serviceMembership.authorize(accessToken, refreshToken, DateUtils.nowPlusSomeSeconds(expiresIn));
    }

}
