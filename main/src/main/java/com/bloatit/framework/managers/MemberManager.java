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
package com.bloatit.framework.managers;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Member;
import com.bloatit.framework.lists.MemberList;
import com.bloatit.model.data.DBRequests;
import com.bloatit.model.data.DaoActor;
import com.bloatit.model.data.DaoMember;

public final class MemberManager {
    
    // Desactivate default ctor
    private MemberManager() {
    }

    public static Member getMemberByLogin(final String login) {
        final DaoMember daoMember = DaoMember.getByLogin(login);
        if (daoMember == null) {
            return null;
        }

        return Member.create(daoMember);
    }

    public static boolean existsMember(final String login) {
        return DaoActor.exist(login);
    }

    public static Member getMemberById(final Integer id) {
        return Member.create(DBRequests.getById(DaoMember.class, id));
    }

    public static PageIterable<Member> getMembers() {
        return new MemberList(DBRequests.getAll(DaoMember.class));
    }

    public static Member getByLoginAndPassword(final String login, final String password) {
        final DaoMember daoMember = DaoMember.getByLoginAndPassword(login, password);
        if (daoMember == null) {
            return null;
        }

        return Member.create(daoMember);
    }

    public static int getMembersCount() {
        return DBRequests.count(DaoMember.class);
    }
}
