package com.bloatit.framework;

import com.bloatit.model.data.DaoJoinGroupInvitation;

public class JoinGroupInvitation extends Identifiable {

    private DaoJoinGroupInvitation dao;

    public Member getSender() {
        return Member.create(dao.getSender());
    }

    public Member getReciever() {
        return Member.create(dao.getReceiver());
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
