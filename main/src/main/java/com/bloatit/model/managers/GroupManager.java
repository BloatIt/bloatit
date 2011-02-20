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
package com.bloatit.model.managers;

import com.bloatit.data.DaoGroup;
import com.bloatit.data.DaoJoinGroupInvitation;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Group;
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.Member;
import com.bloatit.model.lists.GroupList;

/**
 * The Class GroupManager is an utility class containing static methods for {@link Group}
 * loading etc.
 */
public final class GroupManager {

    /**
     * Desactivated constructor on utility class.
     */
    private GroupManager() {
        // Desactivate default ctor
    }

    /**
     * Gets the a group using its (unique) name.
     * 
     * @param name the name of the group we are looking for.
     * @return the group found or null if not found.
     */
    public static Group getByName(final String name) {
        return Group.create(DaoGroup.getByName(name));
    }

    /**
     * Tells if a group exist using its name.
     * 
     * @param name the name of the group we are trying to know if it exists.
     * @return true, if it exist, false otherwise.
     */
    public static boolean exist(final String name) {
        return DaoGroup.getByName(name) != null;
    }

    /**
     * Gets the invitation to join a group.
     * 
     * @param group the group this invitation is on.
     * @param member the member this invitation was sent to.
     * @return the invitation, are null if there is no invitation on that
     * <code>group</code> sent to this <code>member</code>.
     */
    public static JoinGroupInvitation getInvitation(final Group group, final Member member) {
        return JoinGroupInvitation.create(DaoJoinGroupInvitation.getInvitation(group.getDao(), member.getDao()));
    }

    /**
     * Gets the invitation by id.
     * 
     * @param id the id
     * @return the invitation or null if not found.
     */
    public static JoinGroupInvitation getInvitationById(final int id) {
        return JoinGroupInvitation.create(DBRequests.getById(DaoJoinGroupInvitation.class, id));
    }

    /**
     * Gets the group by id.
     * 
     * @param id the id
     * @return the group or null if not found
     */
    public static Group getGroupById(int id) {
        return Group.create(DBRequests.getById(DaoGroup.class, id));
    }

    /**
     * Gets the all the groups.
     * 
     * @return the groups.
     */
    public static PageIterable<Group> getGroups() {
        return new GroupList(DBRequests.getAll(DaoGroup.class));
    }
}
