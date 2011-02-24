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

import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.data.DaoMember.Role;

/**
 * The Interface RestrictedInterface represent a Restricted by some right
 * management class. Each class that implements this interface can be integrated
 * with the static right system.
 */
public interface RestrictedInterface {

    /**
     * Authenticate this content using an {@link AuthToken}.
     *
     * @param token the token representing the user wanting to be authenticated
     *            on this content.
     */
    public abstract void authenticate(AuthToken token);

    /**
     * Checks for group privilege.
     *
     * @param right the right
     * @return true, if successful
     */
    public abstract boolean hasGroupPrivilege(UserGroupRight right);

    /**
     * Checks for user privilege.
     *
     * @param role the role
     * @return true, if successful
     */
    public abstract boolean hasUserPrivilege(Role role);

    /**
     * Checks if is authenticated.
     *
     * @return true, if is authenticated
     */
    public abstract boolean isAuthenticated();

    /**
     * Checks if is owner.
     *
     * @return true, if is owner
     */
    public abstract boolean isOwner();

    /**
     * Checks if is nobody.
     *
     * @return true, if is nobody
     */
    public abstract boolean isNobody();

}
