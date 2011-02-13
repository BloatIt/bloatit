//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model.managers;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javassist.NotFoundException;

import com.bloatit.model.AuthToken;

public final class LoginManager {

    //TODO: clear
    private static final ConcurrentMap<UUID, AuthToken> AUTH_TOKEN_LIST = new ConcurrentHashMap<UUID, AuthToken>();

    private LoginManager() {
        // Desactivate default ctor
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
