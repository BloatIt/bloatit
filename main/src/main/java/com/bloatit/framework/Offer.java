package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.demand.Demand;
import com.bloatit.framework.lists.BatchList;
import com.bloatit.model.data.DaoBatch;
import com.bloatit.model.data.DaoDescription;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoOffer;

// TODO rightManagement

public final class Offer extends Kudosable {

    private final DaoOffer dao;

    // ////////////////////////////////////////////////////////////////////////
    // Construction
    // ////////////////////////////////////////////////////////////////////////

    public static Offer create(final DaoOffer dao) {
        if (dao == null) {
            return null;
        }
        return new Offer(dao);
    }

    /**
     * @param amount must be positive (can be ZERO) non null.
     * @param locale must be non null. Is the locale in which the title and the text are
     *        written.
     * @param title is the title of the offer. Must be non null.
     * @param text is the description of the offer. Must be non null.
     * @param dateExpir is the date when this offer should be finished. Must be non null.
     *        Must be in the future.
     */
    public Offer(final Member member,
                 final Demand demand,
                 final BigDecimal amount,
                 final String title,
                 final String description,
                 final Locale local,
                 final Date dateExpire) {
        dao = DaoOffer.createAndPersist(member.getDao(), demand.getDao(), amount, DaoDescription.createAndPersist(member.getDao(), local, title, description),
                dateExpire);
    }

    private Offer(final DaoOffer dao) {
        super();
        this.dao = dao;
    }

    public void addBatch(final Date dateExpire, final BigDecimal amount, String title, String description, int secondBeforeValidation) {
        // TODO blind me !
        Locale locale = dao.getBatches().iterator().next().getDescription().getDefaultLocale();
        dao.addBatch(DaoBatch.createAndPersist(dateExpire,
                                               amount,
                                               DaoDescription.createAndPersist(dao.getAuthor(), locale, title, description),
                                               dao,
                                               secondBeforeValidation));
    }

    // ////////////////////////////////////////////////////////////////////////
    // Work-flow
    // ////////////////////////////////////////////////////////////////////////

    public boolean isFinished() {
        return !hasBatchLeft();
    }

    public boolean validateCurrentBatch(boolean force) {
        // If the validation is not complete, there is nothing to do in the demand
        boolean isAllValidated = findCurrentDaoBatch().validate(force);
        if (isAllValidated) {
            if (dao.hasBatchesLeft()) {
                getDemand().setOfferIsValidated();
            } else {
                getDemand().setBatchIsValidated();
            }
        }
        return isAllValidated;
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

    private boolean hasBatchLeft() {
        return findCurrentDaoBatch() != null;
    }

    // ////////////////////////////////////////////////////////////////////////
    // Notifications
    // ////////////////////////////////////////////////////////////////////////

    @Override
    protected void notifyKudos(boolean positif) {
        getDemand().notifyOfferKudos(this, positif);
    }

    @Override
    protected void notifyRejected() {
        getDemand().unSelectOffer(this);
    }

    // ////////////////////////////////////////////////////////////////////////
    // Getters
    // ////////////////////////////////////////////////////////////////////////

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
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

    public Date getExpirationDate() {
        return dao.getExpirationDate();
    }

}
