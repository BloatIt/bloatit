package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.data.DBRequests;
import com.bloatit.data.DaoBatch;
import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoOffer;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.DemandImplementation;
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
                                        DBRequests.getById(DaoDemand.class, demand.getId()),
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
        final Locale locale = getDao().getBatches().iterator().next().getDescription().getDefaultLocale();
        getDao().addBatch(DaoBatch.createAndPersist(dateExpire,
                                               amount,
                                               DaoDescription.createAndPersist(getDao().getAuthor(), locale, title, description),
                                               getDao(),
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
            if (getDao().hasBatchesLeft()) {
                getDemandImplementation().setBatchIsValidated();
            } else {
                getDemandImplementation().setOfferIsValidated();
            }
        }
        return isAllValidated;
    }

    public void cancelEverythingLeft() {
        getDao().cancelEverythingLeft();
    }

    private DaoBatch findCurrentDaoBatch() {
        if (getDao().hasBatchesLeft()) {
            return getDao().getCurrentBatch();
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
        getDemandImplementation().notifyOfferKudos(this, positif);
    }

    @Override
    protected void notifyRejected() {
        getDemand().unSelectOffer(this);
    }

    // ////////////////////////////////////////////////////////////////////////
    // Getters
    // ////////////////////////////////////////////////////////////////////////

    public Demand getDemand() {
        return getDemandImplementation();
    }

    private DemandImplementation getDemandImplementation() {
        return DemandImplementation.create(getDao().getDemand());
    }

    public BigDecimal getAmount() {
        return getDao().getAmount();
    }

    public PageIterable<Batch> getBatches() {
        return new BatchList(getDao().getBatches());
    }

    public Date getExpirationDate() {
        return getDao().getExpirationDate();
    }

    // ////////////////////////////////////////////////////////////////////////
    // Kudosable configuration
    // ////////////////////////////////////////////////////////////////////////

    /**
     * @see com.bloatit.model.Kudosable#turnPending()
     */
    @Override
    protected int turnPending() {
        return KudosableConfiguration.getOfferTurnPending();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnValid()
     */
    @Override
    protected int turnValid() {
        return KudosableConfiguration.getOfferTurnValid();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnRejected()
     */
    @Override
    protected int turnRejected() {
        return KudosableConfiguration.getOfferTurnRejected();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnHidden()
     */
    @Override
    protected int turnHidden() {
        return KudosableConfiguration.getOfferTurnHidden();
    }

}
