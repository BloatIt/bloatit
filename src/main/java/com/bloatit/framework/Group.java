package com.bloatit.framework;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.MemberList;
import com.bloatit.model.data.DaoActor;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoGroup.Right;

public class Group extends Actor {

    private final DaoGroup dao;

    public Group(DaoGroup dao) {
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

    public void addMember(Member member, boolean isAdmin) {
        dao.addMember(member.getDao(), isAdmin);
    }

    public void removeMember(Member member) {
        dao.removeMember(member.getDao());
    }

    public Right getRight() {
        return dao.getRight();
    }

    public void setRight(Right right) {
        dao.setRight(right);
    }

    @Override
    protected DaoActor getDaoActor() {
        return dao;
    }

}
