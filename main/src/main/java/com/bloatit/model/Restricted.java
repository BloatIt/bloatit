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

package com.bloatit.model;

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;
import com.bloatit.model.right.Accessor;
import com.bloatit.model.right.RightManager.Action;
import com.bloatit.model.right.RightManager.OwningState;

/**
 * An Unlockable class is a class that you can unlock with an {@link AuthToken}.
 * You also can use the calculateRole to get the role of user. The role is
 * calculated using the {@link AuthToken}, and the argument of the calculateRole
 * method. To more understandable: <li>The {@link AuthToken} is represent the
 * user that try to access an attribute.</li> <li>The argument of the
 * calculateRole method represent the author of the attribute.</li>
 */
public abstract class Restricted implements RestrictedInterface {

    private AuthToken token = null;
    private OwningState owningState;
    private EnumSet<UserGroupRight> groupRights;

    public Restricted() {
        owningState = OwningState.NOBODY;
        groupRights = EnumSet.noneOf(UserGroupRight.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bloatit.model.UnlockableInterface#authenticate(com.bloatit.model.
     * AuthToken)
     */
    @Override
    public final void authenticate(final AuthToken authToken) {
        this.token = authToken;
        if (token == null) {
            Log.model().debug("Try to authanticate with a null token.");
            return;
        }
        if (token.getMember() == null) {
            Log.model().fatal("Null member on an AuthToken");
            return;
        }

        Member member = token.getMember();
        if (isMine(member)) {
            owningState = OwningState.OWNER;
        } else {
            owningState = OwningState.AUTHENTICATED;
        }

        groupRights = calculateMyGroupRights(member);
    }

    public final boolean hasGroupPrivilege(UserGroupRight right) {
        return groupRights.contains(right);
    }

    public final boolean hasUserPrivilege(Role role) {
        // We have to test if we are authenticated
        // to make sure the token is != null
        return isAuthenticated() && token.getMember().getRole() == role;
    }

    public final boolean isAuthenticated() {
        return owningState != OwningState.NOBODY;
    }

    public final boolean isOwner() {
        return owningState == OwningState.OWNER;
    }

    public final boolean isNobody() {
        return owningState == OwningState.NOBODY;
    }

    /**
     * Return true if this content is mine (I am the author).
     * 
     * @param member is the person that wish to know if the content is his.
     * @return true if <code>member</code> is the author of <code>this</code>,
     *         false otherwise.
     */
    protected abstract boolean isMine(Member member);

    protected EnumSet<UserGroupRight> calculateMyGroupRights(Member member) {
        return EnumSet.noneOf(UserGroupRight.class);
    }

    protected final AuthToken getAuthToken() throws UnauthorizedOperationException {
        if (token != null) {
            return token;
        }
        throw new UnauthorizedOperationException(SpecialCode.AUTHENTICATION_NEEDED);
    }

    protected final AuthToken getAuthTokenUnprotected() {
        return token;
    }

    protected final boolean canAccess(Accessor accessor, Action action) {
        return accessor.canAccess(this, action);
    }

    protected final void tryAccess(Accessor accessor, Action action) throws UnauthorizedOperationException {
        accessor.tryAccess(this, action);
    }

}
