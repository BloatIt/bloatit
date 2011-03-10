package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoHighlightFeature;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.feature.DemandImplementation;

public class HighlightDemand extends Identifiable<DaoHighlightFeature> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoHighlightFeature, HighlightDemand> {
        @Override
        public HighlightDemand doCreate(final DaoHighlightFeature dao) {
            return new HighlightDemand(dao);
        }
    }

    public static HighlightDemand create(final DaoHighlightFeature dao) {
        return new MyCreator().create(dao);
    }

    public HighlightDemand(final Feature demand, final int position, final String reason, final Date activationDate, final Date desactivationDate) {
        super(DaoHighlightFeature.createAndPersist(DBRequests.getById(DaoFeature.class, demand.getId()),
                                                  position,
                                                  reason,
                                                  activationDate,
                                                  desactivationDate));
    }

    private HighlightDemand(final DaoHighlightFeature dao) {
        super(dao);
    }

    public int getPosition() {
        return getDao().getPosition();
    }

    public Date getActivationDate() {
        return getDao().getActivationDate();
    }

    public Feature getDemand() {
        return DemandImplementation.create(getDao().getFeature());
    }

    public String getReason() {
        return getDao().getReason();
    }

    @Override
    protected boolean isMine(final Member member) {
        // TODO Auto-generated method stub
        return false;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
