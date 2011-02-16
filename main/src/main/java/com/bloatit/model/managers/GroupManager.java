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

public final class GroupManager {

    private GroupManager() {
        // Desactivate default ctor
    }

    public static Group getByName(final String name) {
        return Group.create(DaoGroup.getByName(name));
    }

    public static boolean exist(final String name) {
        return DaoGroup.getByName(name) != null;
    }

    public static JoinGroupInvitation getInvitation(final Group group, final Member member) {
        return JoinGroupInvitation.create(DaoJoinGroupInvitation.getInvitation(group.getDao(), member.getDao()));
    }
    
    public static JoinGroupInvitation getInvitationById(final int id){
        return JoinGroupInvitation.create(DBRequests.getById(DaoJoinGroupInvitation.class, id));
    }

    public static Group getGroupById(int id) {
        return Group.create(DBRequests.getById(DaoGroup.class, id));
    }
    
    public static PageIterable<Group> getGroups(){
        return new GroupList(DBRequests.getAll(DaoGroup.class));
    }
}
