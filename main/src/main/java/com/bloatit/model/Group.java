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
