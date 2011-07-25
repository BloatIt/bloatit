package com.bloatit.oauth;

import java.util.EnumSet;

import com.bloatit.data.DaoExternalServiceMembership.RightLevel;
import com.bloatit.data.exceptions.ElementNotFoundException;
import com.bloatit.framework.oauthprocessor.OAuthGetAuthorization;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.model.ExternalServiceMembership;
import com.bloatit.model.Member;
import com.bloatit.model.managers.ExternalServiceManager;
import com.bloatit.model.right.AuthToken;

public class GetAuthorization extends OAuthGetAuthorization {

    // FIXME DAO HERE !!

    @Override
    protected void addExternalService(String clientId, String login, String password, String token) throws ElementNotFoundException {
        Member member = authenticate(login, password);
        member.getDao().addAuthorizedExternalService(clientId, token, EnumSet.allOf(RightLevel.class));
    }

    @Override
    protected void authorize(String state, String token, String refresh, int expiresIn) throws AuthorizationException {
        ExternalServiceMembership serviceMembership = ExternalServiceManager.getMembershipByToken(token);
        if (serviceMembership != null && !serviceMembership.isValid()) {
            throw new AuthorizationException();
        }
        serviceMembership.authorize(token, refresh, DateUtils.nowPlusSomeSeconds(expiresIn));
    }

    private Member authenticate(String login, String password) throws ElementNotFoundException {
        // Authenticate the user
        if (login == null || password == null) {
            throw new ElementNotFoundException("login or password missing");
        }
        AuthToken.authenticate(login, password);
        Member member = AuthToken.getMember();
        if (member == null) {
            throw new ElementNotFoundException("Member not found");
        }
        return member;
    }

}
