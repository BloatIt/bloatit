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

package com.bloatit.model.right;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException.SpecialCode;
import com.bloatit.framework.webprocessor.context.UserToken;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;

/**
 * A restricted object is an object that contains some properties which accesses
 * are restricted to some users.
 */
public abstract class RestrictedObject implements RestrictedInterface {

    /** The token. */
    private AuthenticatedUserToken token = null;

    /**
     * Instantiates a new restricted object.
     */
    public RestrictedObject() {
        super();
    }

    @Override
    public void authenticate(final AuthenticatedUserToken token) {
        this.token = token;
    }

    private void automaticAuthentication() {
        final Session session = Context.getSession();
        if (token == null && session != null && session.getUserToken().isAuthenticated()) {
            authenticate((AuthenticatedUserToken) session.getUserToken());
        }
    }

    /**
     * Gets the auth token.
     * 
     * @return the auth token
     * @throws UnauthorizedOperationException the unauthorized operation
     *             exception
     */
    protected final AuthenticatedUserToken getAuthToken() throws UnauthorizedOperationException {
        automaticAuthentication();
        if (token != null) {
            return token;
        }
        throw new UnauthorizedOperationException(SpecialCode.AUTHENTICATION_NEEDED);
    }

    /**
     * Gets the auth token unprotected.
     * 
     * @return the auth token unprotected
     */
    protected final AuthenticatedUserToken getAuthTokenUnprotected() {
        automaticAuthentication();
        return token;
    }
}
