package com.bloatit.framework;

import com.bloatit.model.data.DaoJoinGroupInvitation;

public class JoinGroupInvitation extends Identifiable {

    private DaoJoinGroupInvitation dao;
    
    private JoinGroupInvitation(DaoJoinGroupInvitation dao) {
        this.dao = dao;
    }

    public static JoinGroupInvitation create(DaoJoinGroupInvitation dao){
        if ( dao != null){
            return new JoinGroupInvitation(dao);
        }
        return null;
    }

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
