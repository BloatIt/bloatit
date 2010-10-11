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
package com.bloatit.framework;

import com.bloatit.model.Member;
import com.bloatit.model.exceptions.ElementNotFoundException;
import java.util.HashMap;

public class LoginManager {

    private static HashMap<String, AuthToken> authTokenList = new HashMap<String, AuthToken>();
    private static final HashMap<String, String> accounts = new HashMap<String, String>();

    static {
        accounts.put("fred", "lapin");
        accounts.put("tom", "savoie");
        accounts.put("yoann", "babar");
    }

    public static AuthToken loginByPassword(String login, String password) throws ElementNotFoundException {
        if (accounts.containsKey(login) && accounts.get(login).equals(password)) {
            return newAuthToken(login);
        } else {
            return null;
        }
    }

    public static AuthToken newAuthToken(String login) throws ElementNotFoundException {

        Member member = MemberManager.getMemberByLogin(login);

        // TODO : Generate auth token

        return new AuthToken(member, "abcdefg");
        /*
        member = MemberManager.get_member_by_login(login)
        #TODO: throw exception if no member match
        d = "".join([random.choice(string.ascii_letters) for _ in range(100)])
        cls.sha.update(d.encode())
        token_key = cls.sha.hexdigest()
        new_token = AuthToken(member, token_key)
        return new_token*/
    }

    public static AuthToken getByKey(String key) {
        return new AuthToken(null, key);
        // TODO : tralala
        /*
        if key in cls.auth_token_list:
        return cls.auth_token_list[key]
        else:
        return None
         */
    }
}
