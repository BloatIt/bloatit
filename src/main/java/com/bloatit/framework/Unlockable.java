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

import com.bloatit.framework.right.RightManager.Role;

public class Unlockable {

    private AuthToken token = null;

    public void unLock(AuthToken token) {
        this.token = token;
    }
    public void lock() {
        this.token = null;
    }

    protected AuthToken getToken() {
        return token;
    }

    protected Role calculateRole(Member member) {
        return calculateRole(member, null);
    }

    protected Role calculateRole(String login) {
        if (token == null) {
            return Role.OTHER;
        }
        if (token.getMember().getUnprotectedLogin().equals("admin")) {
            return Role.ADMIN;
        }
        if (token.getMember().getUnprotectedLogin().equals(login)) {
            return Role.OWNER;
        }
        return Role.OTHER;
    }

    protected Role calculateRole(UserContent userContent) {
        return calculateRole(userContent.getAuthor());
    }

    protected Role calculateRole(Member member, Group group) {
        if (token == null) {
            return Role.OTHER;
        }
        if (token.getMember().getUnprotectedLogin().equals("admin")) {
            return Role.ADMIN;
        }
        if (token.getMember().getUnprotectedLogin().equals(member.getUnprotectedLogin())) {
            return Role.OWNER;
        }
        if (group != null && token.getMember().isInGroupUnprotected(group)) {
            return Role.GROUP;
        }
        return Role.OTHER;

    }
}
