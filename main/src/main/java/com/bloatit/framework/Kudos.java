package com.bloatit.framework;

import com.bloatit.model.data.DaoKudos;
import com.bloatit.model.data.DaoUserContent;

public final class Kudos extends UserContent {

    private final DaoKudos dao;

    public Kudos(final DaoKudos dao) {
        super();
        this.dao = dao;
    }

    protected DaoKudos getDao() {
        return dao;
    }

    public int getValue() {
        return dao.getValue();
    }

    @Override
    protected DaoUserContent getDaoUserContent() {
        return dao;
    }

}
