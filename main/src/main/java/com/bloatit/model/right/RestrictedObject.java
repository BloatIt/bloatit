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

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException.SpecialCode;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.Session;
import com.bloatit.model.Member;

/**
 * A restricted object is an object that contains some properties which accesses
 * are restricted to some users.
 */
public abstract class RestrictedObject implements RestrictedInterface {

    /**
     * The Enum OwningState is the state of a user regarding to this
     * {@link RestrictedObject}.
     */
    private enum OwningState {

        /** NOBODY means the user is not authenticated. */
        NOBODY,
        /**
         * AUTHENTICATED means the user is authenticated but he is not the
         * author of this content
         */
        AUTHENTICATED,
        /**
         * OWNER means the user is authenticated and is the author of this
         * content
         */
        OWNER
    }

    /** The token. */
    private AuthToken token = null;

    /** The owning state of the current user authenticated on this content. */
    private OwningState owningState;

    /** The team rights of the current user authenticated on this content. */
    private EnumSet<UserTeamRight> teamRights;

    /**
     * Instantiates a new restricted object.
     */
    public RestrictedObject() {
        owningState = OwningState.NOBODY;
        teamRights = EnumSet.noneOf(UserTeamRight.class);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.right.RestrictedInterface#authenticate(com.bloatit.
     * model.right .AuthToken)
     */
    @Override
    public void authenticate(final AuthToken token) {
        this.token = token;
        updateRights();
    }

    private void automaticAuthentication() {
        final Session session = Context.getSession();
        if (token == null && session != null && session.getAuthToken() != null) {
            authenticate(session.getAuthToken());
        }
    }

    private void updateRights() {
        if (token == null) {
            Log.model().debug("Try to authanticate with a null token.");
            owningState = OwningState.NOBODY;
            return;
        }
        if (token.getMember() == null) {
            Log.model().fatal("Null member on an AuthToken");
            return;
        }

        final Member member = token.getMember();
        if (isMine(member)) {
            owningState = OwningState.OWNER;
        } else {
            owningState = OwningState.AUTHENTICATED;
        }

        teamRights = calculateMyTeamRights(member);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.right.RestrictedInterface#hasTeamPrivilege(com.bloatit
     * .data. DaoTeamRight.UserTeamRight)
     */
    @Override
    public final boolean hasTeamPrivilege(final UserTeamRight right) {
        automaticAuthentication();
        return teamRights.contains(right);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.right.RestrictedInterface#hasUserPrivilege(com.bloatit
     * .data.DaoMember .Role)
     */
    @Override
    public final boolean hasUserPrivilege(final Role role) {
        automaticAuthentication();
        // We have to test if we are authenticated
        // to make sure the token is != null
        return isAuthenticated() && token.getMember().getRole() == role;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.right.RestrictedInterface#isAuthenticated()
     */
    @Override
    public final boolean isAuthenticated() {
        automaticAuthentication();
        return owningState != OwningState.NOBODY;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.right.RestrictedInterface#isOwner()
     */
    @Override
    public final boolean isOwner() {
        automaticAuthentication();
        return owningState == OwningState.OWNER;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.right.RestrictedInterface#isNobody()
     */
    @Override
    public final boolean isNobody() {
        automaticAuthentication();
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

    /**
     * Calculate my team rights.
     *
     * @param member the member
     * @return the enum set
     */
    protected EnumSet<UserTeamRight> calculateMyTeamRights(final Member member) {
        return EnumSet.noneOf(UserTeamRight.class);
    }

    /**
     * Gets the auth token.
     *
     * @return the auth token
     * @throws UnauthorizedOperationException the unauthorized operation
     *             exception
     */
    protected final AuthToken getAuthToken() throws UnauthorizedOperationException {
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
    protected final AuthToken getAuthTokenUnprotected() {
        automaticAuthentication();
        return token;
    }

    /**
     * Can access.
     *
     * @param accessor the accessor
     * @param action the action
     * @return true, if successful
     */
    protected final boolean canAccess(final Accessor accessor, final Action action) {
        return accessor.canAccess(this, action);
    }

    /**
     * Try access.
     *
     * @param accessor the accessor
     * @param action the action
     * @throws UnauthorizedOperationException the unauthorized operation
     *             exception
     */
    protected final void tryAccess(final Accessor accessor, final Action action) throws UnauthorizedOperationException {
        accessor.tryAccess(this, action);
    }
}
