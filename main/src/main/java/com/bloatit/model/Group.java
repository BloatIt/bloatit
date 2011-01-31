package com.bloatit.model;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoGroup;
import com.bloatit.data.DaoGroup.Right;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.MemberList;

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
