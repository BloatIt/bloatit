package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.demand.Demand;
import com.bloatit.framework.lists.BatchList;
import com.bloatit.model.data.DaoBatch;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoOffer;

// TODO rightManagement

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

    public Demand getDemand() {
        return Demand.create(dao.getDemand());
    }

    public BigDecimal getAmount() {
        return dao.getAmount();
    }

    public PageIterable<Batch> getBatches() {
        return new BatchList(dao.getBatches());
    }

    public void addBatch(final Batch batch) {
        dao.addBatch(batch.getDao());
    }

    public Date getExpirationDate() {
        return dao.getExpirationDate();
    }

    public boolean hasBatchLeft() {
        return findCurrentDaoBatch() != null;
    }

    public void closeCurrentBatch(boolean valide) {
        if (valide) {
            if (dao.hasBatchesLeft()) {
                getDemand().setOfferIsValidated();
            } else {
                getDemand().setBatchIsValidated();
            }
        } else {
            getDemand().setBatchIsRejected();
        }
    }

    public void cancelEverythingLeft() {
        dao.cancelEverythingLeft();
    }

    private DaoBatch findCurrentDaoBatch() {
        if (dao.hasBatchesLeft()) {
            return dao.getCurrentBatch();
        }
        return null;
    }

    @Override
    protected void notifyKudos(boolean positif) {
        Demand.create(dao.getDemand()).notifyOfferKudos(this, positif);
    }

    @Override
    protected void notifyRejected() {
        Demand.create(dao.getDemand()).unSelectOffer(this);
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

}
