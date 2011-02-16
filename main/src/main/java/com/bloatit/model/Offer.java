//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.data.DaoBatch;
import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.DemandImplementation;
import com.bloatit.model.lists.BatchList;
import com.bloatit.model.right.DemandRight;
import com.bloatit.model.right.RightManager.Action;

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
     * written.
     * @param title is the title of the offer. Must be non null.
     * @param text is the description of the offer. Must be non null.
     * @param dateExpir is the date when this offer should be finished. Must be non null.
     * Must be in the future.
     */
    public Offer(final Member member,
            final Demand demand,
            final BigDecimal amount,
            final String description,
            final Locale local,
            final Date dateExpire,
            int secondsBeforeValidation) {
        super(DaoOffer.createAndPersist(member.getDao(),
                                        DBRequests.getById(DaoDemand.class, demand.getId()),
                                        amount,
                                        DaoDescription.createAndPersist(member.getDao(), local, "RFU", description),
                                        dateExpire,
                                        secondsBeforeValidation));
    }

    public Offer(final Member member,
            final Demand demand) {
        super(DaoOffer.createAndPersist(member.getDao(),
                                        DBRequests.getById(DaoDemand.class, demand.getId())
                                        ));
    }

    private Offer(final DaoOffer dao) {
        super(dao);
    }

    public void addBatch(final BigDecimal amount, final String description, final Locale local, final Date dateExpire, final int secondBeforeValidation) {
        getDao().addBatch(DaoBatch.createAndPersist(dateExpire,
                                                    amount,
                                                    DaoDescription.createAndPersist(getDao().getAuthor(), local, "RFU", description),
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

    /** The Constant PROGRESSION_PERCENT. */
    public static final int PROGRESSION_PERCENT = 100;

    /**
     * Return the progression of the funding of this offer with the amount available on the demand
     * @return
     * @throws UnauthorizedOperationException
     */
    public float getProgression() throws UnauthorizedOperationException {

        if (getAmount().floatValue() != 0) {
            new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);

            return (getDemand().getContribution().floatValue() * PROGRESSION_PERCENT) / getAmount().floatValue();
        }
        return Float.POSITIVE_INFINITY;
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
