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

import org.hibernate.classic.Session;

import com.bloatit.common.Log;
import com.bloatit.data.DaoMember.ActivationState;
import com.bloatit.data.SessionManager;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;

/**
 * An AuthToken is a token representing an authenticated user. You can use it to
 * tell a {@link RestrictedObject} class which user is using it.
 */
public final class AuthToken {
    private final Member member;
    private final UUID key;

    /**
     * Create an authoToken using the login and password of a person.
     * 
     * @throws NotFoundException if the login is not found or if the password is
     *             wrong.
     */
    public AuthToken(final String login, final String password) throws NotFoundException {
        final Member tmp = MemberManager.getByLoginAndPassword(login, password);
        if (tmp == null) {
            Log.model().warn("Authentication error with login " + login);
            throw new NotFoundException("Identification or authentication failed");
        }

        if (tmp.getActivationState() != ActivationState.ACTIVE) {
            // TODO: display a different notification error
            Log.model().warn("Authentication with inactive or deleted account with login " + login);
            throw new NotFoundException("Authentication with inactive or deleted account.");
        }

        member = tmp;
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
        final Member tmp = MemberManager.getById(memberId);
        if (tmp == null) {
            throw new NotFoundException("Identification failed");
        }
        member = tmp;
        key = UUID.randomUUID();
    }

    public AuthToken(final Member member) {
        this.member = member;
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
        final Session currentSession = SessionManager.getSessionFactory().getCurrentSession();
        if (!currentSession.getTransaction().isActive() || currentSession.contains(member.getDao())) {
            return member;
        }
        final Member memberById = MemberManager.getById(member.getId());
        return memberById;
    }

    public Member getNonPersistantMember() {
        return member;
    }

}
