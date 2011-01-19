package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.BatchList;
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

    private Offer(final DaoOffer dao) {
        super();
        this.dao = dao;
    }

    public DaoOffer getDao() {
        return dao;
    }

    public Date getDateExpire() {
        return dao.getExpirationDate();
    }

    public Demand getDemand() {
        return Demand.create(dao.getDemand());
    }

    public BigDecimal getAmount() {
        return dao.getAmount();
    }

    public PageIterable<Batch> getBatches() {
        return new BatchList(dao.getBatches());
    }

    public void addBatch(Batch batch) {
        dao.addBatch(batch.getDao());
    }

    public Date getExpirationDate() {
        return dao.getExpirationDate();
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }
}
