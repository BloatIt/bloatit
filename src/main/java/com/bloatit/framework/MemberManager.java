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

import com.bloatit.model.Member;
import com.bloatit.model.exceptions.ElementNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MemberManager {

    private final static ArrayList<Member> members = new ArrayList<Member>();

    static {
        members.add(new Member(1, "Yoann", "yplenet@gmail.com", "Yoann Plénet", -14413));
        members.add(new Member(2, "Fred", "fred@gmail.com", "Frédéric Bertolus", 12));
        members.add(new Member(3, "Tom", "tom@gmail.com", "Thomas Guyard", 3000000000000000000L));
    }

    public static Member getMemberByLogin(String login) throws ElementNotFoundException {
        for (Member m : members) {
            if (login.equalsIgnoreCase(m.getLogin())) {
                return m;
            }
        }
        throw new ElementNotFoundException();
    }

    public static boolean existsMember(String login) {
        for (Member m : members) {
            if (login.equals(m.getLogin())) {
                return true;
            }
        }
        return false;
    }

    public static List<Member> getMemberList() {
        return members;
    }

    public static Member GetMemberById(Integer id) throws ElementNotFoundException {
        for (Member d : members) {
            if (d.getId() == id) {
                return d;
            }
        }
        throw new ElementNotFoundException();
    }
}
