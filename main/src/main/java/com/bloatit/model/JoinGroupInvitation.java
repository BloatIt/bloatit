package com.bloatit.model;

import com.bloatit.data.DaoJoinGroupInvitation;

public final class JoinGroupInvitation extends Identifiable<DaoJoinGroupInvitation> {

    private JoinGroupInvitation(final DaoJoinGroupInvitation dao) {
        super(dao);
    }

    public static JoinGroupInvitation create(final DaoJoinGroupInvitation dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoJoinGroupInvitation> created = CacheManager.get(dao);
            if (created == null) {
                return new JoinGroupInvitation(dao);
            }
            return (JoinGroupInvitation) created;
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
