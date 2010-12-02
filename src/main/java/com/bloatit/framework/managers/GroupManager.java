package com.bloatit.framework.managers;

import com.bloatit.framework.Group;
import com.bloatit.framework.JoinGroupInvitation;
import com.bloatit.framework.Member;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoJoinGroupInvitation;

public class GroupManager {
    public static Group getByName(String name) {
        return Group.create(DaoGroup.getByName(name));
    }

    public static boolean exist(String name) {
        return DaoGroup.getByName(name) != null;
    }

    public static JoinGroupInvitation getInvitation(Group group, Member member) {
        return JoinGroupInvitation.create(DaoJoinGroupInvitation.getInvitation(group.getDao(), member.getDao()));
    }
}
