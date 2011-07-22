package com.bloatit.oauth;

import com.bloatit.data.DaoExternalService;
import com.bloatit.framework.oauthprocessor.OAuthGetToken;
import com.bloatit.framework.utils.datetime.DateUtils;

public class GetToken extends OAuthGetToken {

    protected void authorizeService(String authzCode, String accessToken, String refreshToken, int expiresIn) throws AuthorizationException {
        DaoExternalService serviceByKey = DaoExternalService.getServiceByKey(authzCode);
        if (serviceByKey != null && !serviceByKey.isValid()) {
            throw new AuthorizationException();
        }
        serviceByKey.authorize(accessToken, refreshToken, DateUtils.nowPlusSomeSeconds(expiresIn));
    }

}
