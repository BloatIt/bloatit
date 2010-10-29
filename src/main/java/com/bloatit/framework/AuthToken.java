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

import java.util.UUID;

import javassist.NotFoundException;

import com.bloatit.framework.managers.MemberManager;

public class AuthToken {
    private Member member;
    private UUID key;

    public AuthToken(String login, String password) throws NotFoundException {
        Member tmp = MemberManager.getByLoginAndPassword(login, password);
        if (tmp == null) {
            throw new NotFoundException("Identifiaction or authentification failed");
        }
        this.member = tmp;
        this.key = UUID.randomUUID();
    }

    public UUID getKey() {
        return key;
    }

    public Member getMember() {
        return member;
    }

}
