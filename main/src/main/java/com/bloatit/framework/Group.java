package com.bloatit.framework;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.MemberList;
import com.bloatit.model.data.DaoActor;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoGroup.Right;

public final class Group extends Actor {

    private final DaoGroup dao;

    public static Group create(final DaoGroup dao) {
        if (dao == null) {
            return null;
        }
        return new Group(dao);
    }

    private Group(final DaoGroup dao) {
        super();
        this.dao = dao;
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
