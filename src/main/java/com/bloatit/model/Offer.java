package com.bloatit.model;

import java.util.Date;

import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoOffer;

public class Offer extends Kudosable {

    private DaoOffer dao;

    public Offer(DaoOffer dao) {
        super();
        this.dao = dao;
    }

    public DaoOffer getDao() {
        return dao;
    }

    public Date getDateExpire() {
        return dao.getDateExpire();
    }

    public void setDateExpire(Date dateExpire) {
        dao.setDateExpire(dateExpire);
    }

    public Demand getDemand() {
        return new Demand(dao.getDemand());
    }

    public Description getDescription() {
        return new Description(dao.getDescription());
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

}
