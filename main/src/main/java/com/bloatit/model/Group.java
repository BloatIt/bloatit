package com.bloatit.model;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoGroup;
import com.bloatit.data.DaoGroup.Right;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.MemberList;

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

    @Override
    public DaoGroup getDao() {
        return dao;
    }

    public PageIterable<Member> getMembers() {
        return new MemberList(dao.getMembers());
    }

    public Right getRight() {
        return dao.getRight();
    }

    public void setRight(final Right right) {
        dao.setRight(right);
    }

    @Override
    protected DaoActor getDaoActor() {
        return dao;
    }

}
