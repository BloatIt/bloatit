package com.bloatit.framework;

import com.bloatit.model.data.DaoJoinGroupInvitation;

public final class JoinGroupInvitation extends Identifiable {

    private final DaoJoinGroupInvitation dao;

    private JoinGroupInvitation(final DaoJoinGroupInvitation dao) {
        this.dao = dao;
    }

    public static JoinGroupInvitation create(final DaoJoinGroupInvitation dao) {
        if (dao != null) {
            return new JoinGroupInvitation(dao);
        }
        return null;
    }

    public Member getSender() {
        return Member.create(dao.getSender());
    }

    public Member getReciever() {
        return Member.create(dao.getReceiver());
    }

    public Group getGroup() {
        return Group.create(dao.getGroup());
    }

    /**
     * @see DaoJoinGroupInvitation#accept()
     */
    public void accept() {
        dao.accept();
    }

    /**
     * @see DaoJoinGroupInvitation#refuse()
     */
    public void refuse() {
        dao.refuse();
    }

    @Override
    public int getId() {
        return dao.getId();
    }

}
