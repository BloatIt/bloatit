package com.bloatit.framework;

import com.bloatit.framework.right.MemberRight;
import com.bloatit.framework.right.RightManager.Action;
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

    public void accept() {
        new MemberRight.InviteInGroup().tryAccess(calculateRole(getReciever(), getGroup()), Action.READ);
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
