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
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;

/**
 * An AuthToken is a token representing an authenticated user. You can use it to
 * tell a {@link RestrictedObject} class which user is using it.
 */
public final class AuthToken {
    private final int memberId;
    private Integer asTeamId;
    private final UUID key;

    /**
     * Create an authoToken using the login and password of a person.
     * 
     * @throws NotFoundException if the login is not found or if the password is
     *             wrong.
     */
    public AuthToken(final String login, final String password) throws NotFoundException {
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

        memberId = tmp.getId();
        key = UUID.randomUUID();
    }

    /**
     * NEVER Use this method. It is used by the SessionManager to persist the
     * login session of a user even in case of a server restart.
     * 
     * @param memberId
     * @throws NotFoundException
     */
    public AuthToken(final int memberId) throws NotFoundException {
        final User tmp = MemberManager.getById(memberId);
        if (tmp == null) {
            throw new NotFoundException("Identification failed");
        }
        this.memberId = memberId;
        key = UUID.randomUUID();
    }

    public AuthToken(final Member member) {
        this.memberId = member.getId();
        key = UUID.randomUUID();
    }

    /**
     * @return a unique key, identifying this authToken.
     */
    public UUID getKey() {
        return key;
    }

    /**
     * If a transaction is active, make sure the member has an internal
     * persistent dao.
     * 
     * @return the member that is authenticated by this token.
     */
    public Member getMember() {
        return MemberManager.getById(memberId);
    }

    public void setAsTeam(final Team team) {
        if (team != null) {
            this.asTeamId = team.getId();
        } else {
            this.asTeamId = null;
        }
    }

    public Team getAsTeam() {
        if (asTeamId == null) {
            return null;
        }
        return TeamManager.getById(asTeamId);
    }

}
