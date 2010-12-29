package com.bloatit.framework.managers;

import com.bloatit.framework.Group;
import com.bloatit.framework.JoinGroupInvitation;
import com.bloatit.framework.Member;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoJoinGroupInvitation;

public final class GroupManager {

    // Desactivate default ctor
    private GroupManager() {
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
