package com.bloatit.framework;

import com.bloatit.model.data.DaoJoinGroupDemand;

public class JoinGroupDemand extends Identifiable {

    private DaoJoinGroupDemand dao;

    public Member getSender() {
        return Member.create(dao.getSender());
    }

    public Member getReciever() {
        return Member.create(dao.getReciever());
    }

    public Group getGroup() {
        return Group.create(dao.getGroup());
    }

    public void accept() {
        dao.accept();
    }

    public void refuse() {
        dao.refuse();
    }

    @Override
    public int getId() {
        return dao.getId();
    }

}
