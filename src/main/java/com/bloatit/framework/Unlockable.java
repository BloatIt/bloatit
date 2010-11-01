/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.framework;

import java.util.EnumSet;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.RightManager.Role;
import com.bloatit.model.data.DaoGroup.MemberStatus;

public class Unlockable {

    private AuthToken token = null;

    public void authenticate(AuthToken token) {
        this.token = token;
    }

    protected AuthToken getToken() {
        if (token == null) {
            throw new UnauthorizedOperationException();
        }
        return token;
    }

    protected EnumSet<Role> calculateRole(Member member) {
        return calculateRole(member, null);
    }

    protected EnumSet<Role> calculateRole(String login) {
        if (token == null) {
            return EnumSet.of(Role.OTHER);
        }
        if (token.getMember().getUnprotectedLogin().equals(login)) {
            switch (token.getMember().getRole()) {
            case NORMAL:
                return EnumSet.of(Role.OWNER);
            case PRIVILEGED:
                return EnumSet.range(Role.OWNER, Role.PRIVILEGED);
            case REVIEWER:
                return EnumSet.range(Role.OWNER, Role.REVIEWER);
            case MODERATOR:
                return EnumSet.range(Role.OWNER, Role.MODERATOR);
            case ADMIN:
                return EnumSet.range(Role.OWNER, Role.ADMIN);
            }
        }
        return EnumSet.of(Role.OTHER);
    }

    protected EnumSet<Role> calculateRole(UserContent userContent) {
        return calculateRole(userContent.getAuthor());
    }

    protected EnumSet<Role> calculateRole(Member member, Group group) {
        if (token == null) {
            return EnumSet.of(Role.OTHER);
        }
        final EnumSet<Role> roles = calculateRole(member.getUnprotectedLogin());
        if (group != null) {
            MemberStatus status = token.getMember().getStatusUnprotected(group);
            if (status == MemberStatus.ADMIN) {
                roles.add(Role.GROUP_ADMIN);
            }
            if (status == MemberStatus.IN_GROUP) {
                roles.add(Role.IN_GROUP);
            }
        }
        return roles;
    }
}
