package com.bloatit.framework;

import java.util.Date;

import com.bloatit.model.data.DaoUserContent;

public abstract class UserContent extends Identifiable {

    protected abstract DaoUserContent getDaoUserContent();

    public Member getAuthor() {
        return Member.create(getDaoUserContent().getAuthor());
    }

    public Date getCreationDate() {
        return getDaoUserContent().getCreationDate();
    }

    public void setAsGroup(Group asGroup) {
        getDaoUserContent().setAsGroup(asGroup.getDao());
    }

    public Group getAsGroup() {
        return Group.create(getDaoUserContent().getAsGroup());
    }

    @Override
    public final int getId() {
        return getDaoUserContent().getId();
    }

}
