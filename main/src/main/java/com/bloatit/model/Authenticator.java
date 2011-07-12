package com.bloatit.model;

import java.util.HashMap;
import java.util.Map;

import javassist.NotFoundException;

import com.bloatit.model.right.AuthenticatedUserToken;

public class Authenticator {

    private static Map<String, Integer> usersByKey = new HashMap<String, Integer>();
    private static String currentUserKey;
    private static Integer currentMember;

    public static ElveosUserToken getAuthToken() throws NotFoundException {
        if (currentMember != null) {
            return new AuthenticatedUserToken(currentMember);
        }
        return new AnonymousUserToken();
    }
    
    public static void authenticate(final String key, final Member member) {
        usersByKey.put(key, member.getId());
    }
    
    public static void setCurrentUserKey(final String key) {
        currentUserKey = key;
        currentMember = usersByKey.get(key);
    }

}
