/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.managers;

import com.bloatit.common.CryptoTools;
import com.bloatit.model.Member;
import com.bloatit.model.exceptions.ElementNotFoundException;
import com.bloatit.common.FatalErrorException;
import com.bloatit.framework.AuthToken;

import java.util.HashMap;

public class LoginManager {

    private static final HashMap<String, AuthToken> authTokenList = new HashMap<String, AuthToken>();
    private static final HashMap<String, String> accounts = new HashMap<String, String>();

    static {
        accounts.put("fred", "lapin");
        accounts.put("tom", "savoie");
        accounts.put("yoann", "babar");
    }

    public static AuthToken loginByPassword(String login, String password) {
        if (accounts.containsKey(login) && accounts.get(login).equals(password)) {
            return newAuthToken(login);
        } else {
            return null;
        }
    }

    public static AuthToken newAuthToken(String login) {

        Member member;
        try {
            member = MemberManager.getMemberByLogin(login);
        } catch (ElementNotFoundException ex) {
            throw new FatalErrorException("login invalid but auth sucess", ex);
        }

        String key = CryptoTools.generateKey();


        AuthToken newToken = new AuthToken(member, key);

        authTokenList.put(key, newToken);

        return newToken;
    }

    public static AuthToken getByKey(String key) {
        if (authTokenList.containsKey(key)) {
            return authTokenList.get(key);
        } else {
            return null;
        }

    }
}
