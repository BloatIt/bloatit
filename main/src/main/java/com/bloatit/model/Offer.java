package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.data.DaoBatch;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoOffer;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.Demand;
import com.bloatit.model.lists.BatchList;

// TODO rightManagement

public final class Offer extends Kudosable<DaoOffer> {

    // ////////////////////////////////////////////////////////////////////////
    // Construction
    // ////////////////////////////////////////////////////////////////////////

    public static Offer create(final DaoOffer dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoOffer> created = CacheManager.get(dao);
            if (created == null) {
                return new Offer(dao);
            }
            return (Offer) created;
        }
        return null;
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
        super(DaoOffer.createAndPersist(member.getDao(),
                                        demand.getDao(),
                                        amount,
                                        DaoDescription.createAndPersist(member.getDao(), local, title, description),
                                        dateExpire));
    }

    private Offer(final DaoOffer dao) {
        super(dao);
    }

    public void addBatch(final Date dateExpire,
                         final BigDecimal amount,
                         final String title,
                         final String description,
                         final int secondBeforeValidation) {
        // TODO blind me !
        final Locale locale = dao.getBatches().iterator().next().getDescription().getDefaultLocale();
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

    public boolean validateCurrentBatch(final boolean force) {
        // If the validation is not complete, there is nothing to do in the demand
        final boolean isAllValidated = findCurrentDaoBatch().validate(force);
        if (isAllValidated) {
            if (dao.hasBatchesLeft()) {
                getDemand().setBatchIsValidated();
            } else {
                getDemand().setOfferIsValidated();
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
    protected void notifyKudos(final boolean positif) {
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
