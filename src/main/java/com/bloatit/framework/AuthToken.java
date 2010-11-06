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

import com.bloatit.common.Log;
import com.bloatit.framework.managers.MemberManager;
import com.bloatit.model.data.util.SessionManager;

public class AuthToken {
    private final Member member;
    private final UUID key;

    public AuthToken(String login, String password) throws NotFoundException {
        final Member tmp = MemberManager.getByLoginAndPassword(login, password);
        if (tmp == null) {
            Log.framework().warn("Authentication error " + login + " " + password);
            throw new NotFoundException("Identifiaction or authentication failed");
        }
        member = tmp;
        key = UUID.randomUUID();
    }

    public UUID getKey() {
        return key;
    }

    public Member getMember() {
        if (SessionManager.getSessionFactory().getCurrentSession().contains(member.getDao())) {
            return member;
        }
        return MemberManager.getMemberById(member.getId());
    }

}
