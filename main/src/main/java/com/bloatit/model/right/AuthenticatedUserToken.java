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

import java.util.UUID;

import javassist.NotFoundException;

import com.bloatit.common.Log;
import com.bloatit.data.DaoMember;
import com.bloatit.framework.utils.SecuredHash;
import com.bloatit.framework.webprocessor.context.User;
import com.bloatit.framework.webprocessor.context.User.ActivationState;
import com.bloatit.framework.webprocessor.context.UserToken;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;

/**
 * An AuthToken is a token representing an authenticated user. You can use it to
 * tell a {@link RestrictedObject} class which user is using it.
 */
public final class AuthenticatedUserToken implements ElveosUserToken {
    private Integer asTeamId;
    private final Integer memberId;
    private final UUID key;

    /**
     * Create an authoToken using the login and password of a person.
     * 
     * @throws NotFoundException if the login is not found or if the password is
     *             wrong.
     */
    public AuthenticatedUserToken(final String login, final String password) throws NotFoundException {
        final DaoMember tmp = DaoMember.getByLogin(login);
        if (tmp == null) {
            // Spend some time here.
            SecuredHash.calculateHash(password, "012345678901234567890");
            Log.model().info("Identification error with login " + login);
            throw new NotFoundException("Identification failed");
        }

        // Spend some time here. (Normal computation).
        final String digestedPassword = SecuredHash.calculateHash(password, tmp.getSalt());
        if (!tmp.passwordEquals(digestedPassword)) {
            Log.model().info("Authentication error with login " + login);
            throw new NotFoundException("Authentication failed");
        }

        if (tmp.getActivationState() != ActivationState.ACTIVE) {
            // TODO: display a different notification error
            Log.model().warn("Authentication with inactive or deleted account with login " + login);
            throw new NotFoundException("Authentication with inactive or deleted account.");
        }

        this.key = UUID.randomUUID();
        this.memberId = tmp.getId();
    }

    /**
     * NEVER Use this method. It is used by the SessionManager to persist the
     * login session of a user even in case of a server restart.
     * 
     * @param memberId
     * @throws NotFoundException
     */
    public AuthenticatedUserToken(final int memberId) throws NotFoundException {
        final User tmp = MemberManager.getById(memberId);
        if (tmp == null) {
            throw new NotFoundException("Identification failed");
        }
        this.key = UUID.randomUUID();
        this.memberId = memberId;
    }

    public AuthenticatedUserToken(final Member member) {
        this.key = UUID.randomUUID();
        this.memberId = member.getId();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.right.AbstractAuthToken#getKey()
     */
    @Override
    public UUID getKey() {
        return key;
    }

    @Override
    public Member getMember() {
        return MemberManager.getById(memberId);
    }

    @Override
    public void setAsTeam(final Team team) {
        if (team != null) {
            this.asTeamId = team.getId();
        } else {
            this.asTeamId = null;
        }
    }

    @Override
    public Team getAsTeam() {
        if (asTeamId == null) {
            return null;
        }
        return TeamManager.getById(asTeamId);
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
