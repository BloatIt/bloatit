package com.bloatit.framework.managers;

import com.bloatit.framework.Group;
import com.bloatit.model.data.DaoGroup;

public class GroupManager {
    public static Group getByName(String name) {
        return new Group(DaoGroup.getByName(name));
    }

    public static boolean exist(String name) {
        return DaoGroup.getByName(name) != null;
    }
}
