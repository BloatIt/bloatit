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

import javassist.NotFoundException;

import com.bloatit.framework.webprocessor.context.UserToken;
import com.bloatit.model.right.AuthenticatedUserToken;

/**
 * The Class LoginManager is an utility class containing static methods.
 */
public final class LoginManager {

    /**
     * Desactivated constructor on utility class.
     */
    private LoginManager() {
        // Desactivate default ctor
    }

    /**
     * Create an {@link AuthenticatedUserToken} using a login and a password.
     * 
     * @param login the login
     * @param password the password
     * @return the new {@link AuthenticatedUserToken} or <code>null</code> if the
     *         login/password does not match any user.
     */
    public static UserToken loginByPassword(final String login, final String password) {
        try {
            final AuthenticatedUserToken token = new AuthenticatedUserToken(login, password);
            return token;
        } catch (final NotFoundException e) {
            return null;
        }
    }
}
