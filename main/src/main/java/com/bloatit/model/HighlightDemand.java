package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoHighlightDemand;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.feature.DemandImplementation;

public class HighlightDemand extends Identifiable<DaoHighlightDemand> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoHighlightDemand, HighlightDemand> {
        @Override
        public HighlightDemand doCreate(final DaoHighlightDemand dao) {
            return new HighlightDemand(dao);
        }
    }

    public static HighlightDemand create(final DaoHighlightDemand dao) {
        return new MyCreator().create(dao);
    }

    public HighlightDemand(final Feature demand, final int position, final String reason, final Date activationDate, final Date desactivationDate) {
        super(DaoHighlightDemand.createAndPersist(DBRequests.getById(DaoFeature.class, demand.getId()),
                                                  position,
                                                  reason,
                                                  activationDate,
                                                  desactivationDate));
    }

    private HighlightDemand(final DaoHighlightDemand dao) {
        super(dao);
    }

    public int getPosition() {
        return getDao().getPosition();
    }

    public Date getActivationDate() {
        return getDao().getActivationDate();
    }

    public Feature getDemand() {
        return DemandImplementation.create(getDao().getDemand());
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
