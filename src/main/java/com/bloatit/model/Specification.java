package com.bloatit.model;

import com.bloatit.model.data.DaoSpecification;
import com.bloatit.model.data.DaoUserContent;

public class Specification extends UserContent {

    private DaoSpecification dao;
    
    public Specification(DaoSpecification dao) {
        super();
        this.dao = dao;
    }
    

    public DaoSpecification getDao() {
        return dao;
    }


    public String getContent() {
        return dao.getContent();
    }

    public void setContent(String content) {
        dao.setContent(content);
    }


    @Override
    protected DaoUserContent getDaoUserContent() {
        return dao;
    }

}
