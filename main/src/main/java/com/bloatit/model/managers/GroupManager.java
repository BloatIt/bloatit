package com.bloatit.model.managers;

import com.bloatit.data.DaoGroup;
import com.bloatit.data.DaoJoinGroupInvitation;
import com.bloatit.model.Group;
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.Member;

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
}
