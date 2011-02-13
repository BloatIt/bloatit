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
package com.bloatit.model;

import com.bloatit.data.DaoGroup;
import com.bloatit.data.DaoGroup.Right;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.MemberList;

/**
 * This is a group ... There are member in it.
 * @see DaoGroup
 * 
 * @author Thomas Guyard
 */
public final class Group extends Actor<DaoGroup> {

    public static Group create(final DaoGroup dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoGroup> created = CacheManager.get(dao);
            if (created == null) {
                return new Group(dao);
            }
            return (Group) created;
        }
        return null;
    }

    private Group(final DaoGroup dao) {
        super(dao);
    }

    public PageIterable<Member> getMembers() {
        return new MemberList(getDao().getMembers());
    }

    public Right getRight() {
        return getDao().getRight();
    }

    public void setRight(final Right right) {
        getDao().setRight(right);
    }

}
