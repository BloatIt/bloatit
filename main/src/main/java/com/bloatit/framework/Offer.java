package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.demand.Demand;
import com.bloatit.framework.lists.BatchList;
import com.bloatit.model.data.DaoBatch;
import com.bloatit.model.data.DaoBatch.State;
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
        return getCurrentDaoBatch() != null;
    }

    // public Batch getCurrentBatch() {
    // return Batch.create(getCurrentDaoBatch());
    // }

    public void voteUp(Actor actor){
        getCurrentDaoBatch().vote(actor.getDao(), true);
    }

    public void voteDown(Actor actor){
        getCurrentDaoBatch().vote(actor.getDao(), false);
    }

    public void currentBatchDone() {
        getCurrentDaoBatch().setState(State.DONE);
    }

    public void currentBatchDevelopping() {
        getCurrentDaoBatch().setState(State.DEVELOPPING);
    }

    public void cancel() {
        for (DaoBatch batch : dao.getBatches()) {
            batch.setState(State.CANCELED);
        }
    }

    private DaoBatch getCurrentDaoBatch() {
        for (DaoBatch batch : dao.getBatches()) {
            if (batch.getState() == State.PENDING || batch.getState() == State.DEVELOPPING) {
                return batch;
            }
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
