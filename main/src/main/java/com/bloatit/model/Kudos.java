package com.bloatit.model;

import com.bloatit.data.DaoKudos;
import com.bloatit.data.DaoUserContent;

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
