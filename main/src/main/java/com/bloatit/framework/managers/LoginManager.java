/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javassist.NotFoundException;

import com.bloatit.framework.AuthToken;

public final class LoginManager {

    private static final Map<UUID, AuthToken> AUTH_TOKEN_LIST = new HashMap<UUID, AuthToken>();

    // Desactivate default ctor
    private LoginManager() {
    }

    public static AuthToken loginByPassword(final String login, final String password) {
        try {
            final AuthToken token = new AuthToken(login, password);
            AUTH_TOKEN_LIST.put(token.getKey(), token);
            return token;
        } catch (final NotFoundException e) {
            return null;
        }
    }

    public static AuthToken getByKey(final String key) {
        return AUTH_TOKEN_LIST.get(UUID.fromString(key));
    }
}
