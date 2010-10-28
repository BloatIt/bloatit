package com.bloatit.model;

import com.bloatit.framework.right.DemandRight;
import com.bloatit.framework.right.RightManager.Action;
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

    public boolean canSetContent() {
        return new DemandRight.Specification().canAccess(calculateRole(this), Action.WRITE);
    }

    public void setContent(String content) {
        new DemandRight.Specification().tryAccess(calculateRole(this), Action.WRITE);
        dao.setContent(content);
    }

    @Override
    protected DaoUserContent getDaoUserContent() {
        return dao;
    }

}
