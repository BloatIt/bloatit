package com.bloatit.framework;

import com.bloatit.model.data.DaoKudos;
import com.bloatit.model.data.DaoUserContent;

public class Kudos extends UserContent {

    private DaoKudos dao;

    public Kudos(DaoKudos dao) {
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
