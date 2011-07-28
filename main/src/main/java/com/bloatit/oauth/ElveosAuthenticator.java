package com.bloatit.oauth;

import java.util.EnumSet;
import java.util.Set;

import com.bloatit.data.DaoExternalServiceMembership.RightLevel;
import com.bloatit.data.exceptions.ElementNotFoundException;
import com.bloatit.framework.oauthprocessor.OAuthAuthenticator;
import com.bloatit.framework.oauthprocessor.OAuthBloatitReponse.AuthorizationException;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.model.ExternalServiceMembership;
import com.bloatit.model.Member;
import com.bloatit.model.managers.ExternalServiceManager;
import com.bloatit.model.right.AuthToken;

public class ElveosAuthenticator extends OAuthAuthenticator {

    // Do not forget the super scope SCOPE_PERMANENT_TOKEN = "permanent_token"
    private static final String SCOPE_TRUST_ME = "trust_me";
    private static final String SCOPE_KUDOS = "kudos";
    private static final String SCOPE_CREATE_OFFER = "create_offer";
    private static final String SCOPE_CONTRIBUTE = "contribute";
    private static final String SCOPE_COMMENT = "comment";

    @Override
    protected void addExternalService(String clientId, String login, String password, String token, Set<String> scope)
            throws ElementNotFoundException {
        Member member = authenticate(login, password);
        EnumSet<RightLevel> rights = EnumSet.noneOf(RightLevel.class);
        if (scope.contains(SCOPE_COMMENT)) {
            rights.add(RightLevel.COMMENT);
        }
        if (scope.contains(SCOPE_CONTRIBUTE)) {
            rights.add(RightLevel.CONTRIBUTE);
        }
        if (scope.contains(SCOPE_CREATE_OFFER)) {
            rights.add(RightLevel.CREATE_OFFER);
        }
        if (scope.contains(SCOPE_KUDOS)) {
            rights.add(RightLevel.KUDOS);
        }
        if (scope.contains(SCOPE_TRUST_ME)) {
            rights.add(RightLevel.TRUST_ME);
        }
        member.addAuthorizedExternalService(clientId, token, rights);
    }

    @Override
    protected void authorize(String authzCode, String accessToken, String refreshToken, int expiresIn) throws AuthorizationException {
        ExternalServiceMembership serviceMembership = ExternalServiceManager.getMembershipByToken(authzCode);
        if (serviceMembership == null || serviceMembership.isAuthorized()) {
            throw new AuthorizationException();
        }
        serviceMembership.authorize(accessToken, refreshToken, DateUtils.nowPlusSomeSeconds(expiresIn));
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
