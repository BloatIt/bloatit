package com.bloatit.model;

import com.bloatit.model.data.DaoKudos;
import com.bloatit.model.data.DaoUserContent;

public class Kudos extends UserContent {

    private DaoKudos dao;

    public Kudos(DaoKudos dao) {
        super();
        this.dao = dao;
    }

    public DaoKudos getDao() {
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
