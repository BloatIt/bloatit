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
 * 
 * @see DaoGroup
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

    /**
     * <p>
     * Creates a new group
     * </p>
     * 
     * @param login
     *            the displayed name of the group
     * @param contact
     *            a string with various means to contact the group
     * @param description
     *            a textual description of the group
     * @param right
     *            <ether the group is <code>PUBLIC</code> or
     *            <code>PROTECTED</code>
     * @param author
     *            the creator of the group
     */
    public Group(String login, String contact, String description, Right right, Member author) {
        super(DaoGroup.createAndPersiste(login, contact, description, right));
        author.addToGroupUnprotected(this);
        author.setGroupRoleUnprotected(this, TeamRole.ADMIN);
    }

    private Group(final DaoGroup dao) {
        super(dao);
    }

    /**
     * @return the list of members that are part of this group
     */
    public PageIterable<Member> getMembers() {
        return new MemberList(getDao().getMembers());
    }

    /**
     * @return the type of group: either <code>PROTECTED</code> or
     *         <code>PUBLIC</code>
     */
    public Right getRight() {
        return getDao().getRight();
    }

    /**
     * Sets the type of group: either <code>PROTECTED</code> or
     * <code>PUBLIC</code>
     */
    public void setRight(final Right right) {
        getDao().setRight(right);
    }

    /**
     * Indicates wheter the group is public or not
     * 
     * @return <code>true</code> if the group is public, <code>false</code>
     *         otherwise
     */
    public boolean isPublic() {
        return (getDao().getRight() == Right.PUBLIC);
    }

    /**
     * @return the textual representation of this group
     */
    public String getDescription() {
        return getDao().getDescription();
    }

}
