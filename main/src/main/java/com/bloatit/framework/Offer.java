package com.bloatit.framework;

import java.util.Date;

import com.bloatit.framework.right.OfferRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoOffer;

public final class Offer extends Kudosable {

    private final DaoOffer dao;

    public static Offer create(final DaoOffer dao) {
        if (dao == null) {
            return null;
        }
        return new Offer(dao);
    }
    
    public Offer(final DaoOffer dao) {
        super();
        this.dao = dao;
    }

    public DaoOffer getDao() {
        return dao;
    }

    public Date getDateExpire() {
        return dao.getDateExpire();
    }

    public boolean canSetdatExpire() {
        return new OfferRight.DateExpire().canAccess(calculateRole(this), Action.WRITE);
    }

    public void setDateExpire(final Date dateExpire) {
        new OfferRight.DateExpire().tryAccess(calculateRole(this), Action.WRITE);
        dao.setDateExpire(dateExpire);
    }

    public Demand getDemand() {
        return Demand.create(dao.getDemand());
    }

    public Description getDescription() {
        return new Description(dao.getDescription());
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

}
