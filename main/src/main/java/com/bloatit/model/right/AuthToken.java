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

import com.bloatit.common.Log;
import com.bloatit.data.DaoExternalServiceMembership;
import com.bloatit.data.DaoMember;
import com.bloatit.data.exceptions.ElementNotFoundException;
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

    public static void authenticate(final String login, final String password) throws ElementNotFoundException {
        final DaoMember tmp = DaoMember.getByLogin(login);
        if (tmp == null) {
            // Spend some time here.
            Hash.calculateHash(password, "012345678901234567890");
            Log.model().info("Identification error with login " + login);
            throw new ElementNotFoundException("Identification failed");
        }

        // Spend some time here. (Normal computation).
        final String digestedPassword = Hash.calculateHash(password, tmp.getSalt());
        if (!tmp.passwordEquals(digestedPassword)) {
            Log.model().info("Authentication error with login " + login);
            throw new ElementNotFoundException("Authentication failed");
        }

        if (tmp.getActivationState() != ActivationState.ACTIVE) {
            Log.model().warn("Authentication with inactive or deleted account with login " + login);
            throw new ElementNotFoundException("Authentication with inactive or deleted account.");
        }

        This.get().memberId = tmp.getId();
        final Session session = Context.getSession();
        if (session != null) {
            session.logIn(This.get().memberId, tmp.getLocale());
        }
    }

    public static void authenticate(final RequestKey key) throws ElementNotFoundException {
        switch (key.getSource()) {
            case COOKIE:
                This.get().memberId = sessionAuthenticate(key);
                break;
            case TOKEN:
                This.get().memberId = oauthAuthenticate(key);
                break;
            case GENERATED:
                This.get().memberId = null;
                break;
            default:
                break;
        }
    }

    private static Integer oauthAuthenticate(final RequestKey key) throws ElementNotFoundException {
        final DaoExternalServiceMembership service = DaoExternalServiceMembership.getByToken(key.getId());

        // TODO manage error codes:
        // When a request fails, the resource server responds using the
        // appropriate HTTP status code (typically, 400, 401, or 403), and
        // includes one of the following error codes in the response:
        //
        // invalid_request
        // The request is missing a required parameter, includes an unsupported
        // parameter or parameter value, repeats the same parameter, uses more
        // than one method for including an access token, or is otherwise
        // malformed. The resource server SHOULD respond with the HTTP 400 (Bad
        // Request) status code.
        //
        // invalid_token
        // The access token provided is expired, revoked, malformed, or invalid
        // for other reasons. The resource SHOULD respond with the HTTP 401
        // (Unauthorized) status code. The client MAY request a new access token
        // and retry the protected resource request.
        //
        // insufficient_scope
        // The request requires higher privileges than provided by the access
        // token. The resource server SHOULD respond with the HTTP 403
        // (Forbidden) status code and MAY include the scope attribute with the
        // scope necessary to access the protected resource.
        //
        // If the request lacks any authentication information (i.e. the client
        // was unaware authentication is necessary or attempted using an
        // unsupported authentication method), the resource server SHOULD NOT
        // include an error code or other error information.
        //
        // For example:
        // HTTP/1.1 401 Unauthorized
        // WWW-Authenticate: Bearer realm="example"

        if (service == null) {
            throw new ElementNotFoundException("The current key does not belong to anyone.");
        }
        return service.getMember().getId();
    }

    private static Integer sessionAuthenticate(final RequestKey key) throws ElementNotFoundException {
        final Session session = SessionManager.getOrCreateSession(key);
        final Integer id = session.getMemberId();
        if (id == null) {
            throw new ElementNotFoundException("The current session is not authenticated.");
        }
        if (MemberManager.getById(id) == null) {
            session.logOut();
            throw new ElementNotFoundException("Wrong authentication id stored in the session.");
        }
        return id;
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
