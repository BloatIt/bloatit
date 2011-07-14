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

import javassist.NotFoundException;

import com.bloatit.common.Log;
import com.bloatit.data.DaoExternalService;
import com.bloatit.data.DaoMember;
import com.bloatit.framework.utils.Hash;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.webprocessor.context.SessionManager;
import com.bloatit.framework.webprocessor.context.User.ActivationState;
import com.bloatit.framework.xcgiserver.RequestKey;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;

/**
 * An AuthToken is a token representing an authenticated user. You can use it to
 * tell a {@link RestrictedObject} class which user is using it.
 */
public final class AuthToken {
    private Integer asTeamId;
    private Integer memberId;

    static class This {
        private static final ThreadLocal<AuthToken> data = new ThreadLocal<AuthToken>() {
            @Override
            protected AuthToken initialValue() {
                return new AuthToken();
            }
        };

        private static AuthToken get() {
            return data.get();
        }
    } // UniqueThreadContext

    public static void authenticate(final String login, final String password) throws NotFoundException {
        final DaoMember tmp = DaoMember.getByLogin(login);
        if (tmp == null) {
            // Spend some time here.
            Hash.calculateHash(password, "012345678901234567890");
            Log.model().info("Identification error with login " + login);
            throw new NotFoundException("Identification failed");
        }

        // Spend some time here. (Normal computation).
        final String digestedPassword = Hash.calculateHash(password, tmp.getSalt());
        if (!tmp.passwordEquals(digestedPassword)) {
            Log.model().info("Authentication error with login " + login);
            throw new NotFoundException("Authentication failed");
        }

        if (tmp.getActivationState() != ActivationState.ACTIVE) {
            Log.model().warn("Authentication with inactive or deleted account with login " + login);
            throw new NotFoundException("Authentication with inactive or deleted account.");
        }

        This.get().memberId = tmp.getId();
        final Session session = Context.getSession();
        if (session != null) {
            session.logIn(This.get().memberId, tmp.getLocale());
        }
    }

    public static void authenticate(final RequestKey key) throws NotFoundException {
        final Integer id = SessionManager.getOrCreateSession(key).getMemberId();
        if (id == null) {
            throw new NotFoundException("The current session is not authenticated.");
        }
        This.get().memberId = id;
    }

    public static void authenticate(final String key) throws NotFoundException {
        final DaoExternalService service = DaoExternalService.getServiceByKey(key);
        if (service == null) {
            throw new NotFoundException("The current key does not belong to anyone.");
        }
        This.get().memberId = service.getMember().getId();
    }

    public static void authenticate(final Member member) {
        This.get().memberId = member.getId();
        final Session session = Context.getSession();
        if (session != null) {
            session.logIn(This.get().memberId, member.getLocale());
        }
    }

    public static void logOut() {
        final Session session = Context.getSession();
        if (session != null) {
            session.logOut();
        }
        unAuthenticate();
    }

    public static void unAuthenticate() {
        This.get().memberId = null;
        This.get().asTeamId = null;
    }

    public static boolean isAuthenticated() {
        return This.get().memberId != null;
    }

    public static Member getMember() {
        return MemberManager.getById(This.get().memberId);
    }

    public static void setAsTeam(final Team team) {
        if (team != null) {
            This.get().asTeamId = team.getId();
        } else {
            This.get().asTeamId = null;
        }
    }

    public static Team getAsTeam() {
        if (This.get().asTeamId == null) {
            return null;
        }
        return TeamManager.getById(This.get().asTeamId);
    }

    private AuthToken() {
        memberId = null;
    }
}
