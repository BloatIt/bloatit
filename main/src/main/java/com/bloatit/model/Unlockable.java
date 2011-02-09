/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.model;

import java.util.EnumSet;

import com.bloatit.data.DaoGroup.MemberStatus;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;
import com.bloatit.model.right.RightManager.Role;

/**
 * An Unlockable class is a class that you can unlock with an {@link AuthToken}. You also
 * can use the calculateRole to get the role of user. The role is calculated using the
 * {@link AuthToken}, and the argument of the calculateRole method. To more
 * understandable: <li>The {@link AuthToken} is represent the user that try to access an
 * attribute.</li> <li>The argument of the calculateRole method represent the author of
 * the attribute.</li>
 */
public class Unlockable implements UnlockableInterface {

    private AuthToken token = null;

    /* (non-Javadoc)
     * @see com.bloatit.model.UnlockableInterface#authenticate(com.bloatit.model.AuthToken)
     */
    @Override
    public final void authenticate(final AuthToken authToken) {
        this.token = authToken;
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

    /**
     * Calculate the role using the login of the author. Sometimes you do not have a
     * complete Member object to describe the author of a "content". You can use this
     * method (the login is unique). This method cannot set some Group roles, you have to
     * use the {@link Unlockable#calculateRole(Member, Group)} method.
     *
     * @return An EnumSet with the roles of the member authenticate by the
     *         {@link AuthToken}.
     */
    protected final EnumSet<Role> calculateRole(final String login) {
        if (token == null) {
            return EnumSet.of(Role.NOBODY);
        }
        if (token.getMember().getLoginUnprotected().equals(login)) {
            switch (token.getMember().getRole()) {
            case NORMAL:
                return EnumSet.of(Role.OWNER);
            }
        } else {
            switch (token.getMember().getRole()) {
            case PRIVILEGED:
                return EnumSet.range(Role.PRIVILEGED, Role.PRIVILEGED);
            case REVIEWER:
                return EnumSet.range(Role.PRIVILEGED, Role.REVIEWER);
            case MODERATOR:
                return EnumSet.range(Role.PRIVILEGED, Role.MODERATOR);
            case ADMIN:
                return EnumSet.range(Role.PRIVILEGED, Role.ADMIN);
            }
        }
        return EnumSet.of(Role.OTHER);
    }

    /**
     * Helper function. Call the {@link Unlockable#calculateRole(Member)} method.
     */
    protected final EnumSet<Role> calculateRole(final UserContentInterface<?> userContent) {
        if (userContent == null || userContent.getAuthor() == null) {
            // This should never append.
            throw new NonOptionalParameterException();
        }
        return calculateRole(userContent.getAuthor());
    }

    /**
     * Helper function. Call the {@link Unlockable#calculateRole(Member, Group)} method.
     * (Group is null).
     */
    protected final EnumSet<Role> calculateRole(final Member member) {
        return calculateRole(member, null);
    }

    /**
     * Calculate the role {@link AuthToken} user, on a content created by
     * "member as group".
     *
     * @param member The creator of the content.
     * @param group the creator uses "group" to create the content.
     * @return all the role that correspond to the {@link AuthToken}.
     */
    protected final EnumSet<Role> calculateRole(final Member member, final Group group) {
        if (token == null) {
            return EnumSet.of(Role.NOBODY);
        }
        final EnumSet<Role> roles = calculateRole(member.getLoginUnprotected());
        if (group != null) {
            final MemberStatus status = token.getMember().getStatusUnprotected(group);
            if (status == MemberStatus.ADMIN) {
                roles.add(Role.GROUP_ADMIN);
                roles.add(Role.IN_GROUP);
            }
            if (status == MemberStatus.IN_GROUP) {
                roles.add(Role.IN_GROUP);
            }
        }
        return roles;
    }

    /**
     * Helper function.
     *
     * @return Nobody or Other
     */
    protected final EnumSet<Role> calculateNoOwnerRole() {
        if (getAuthTokenUnprotected() == null) {
            return EnumSet.of(Role.NOBODY);
        }
        return EnumSet.of(Role.OTHER);
    }
}
