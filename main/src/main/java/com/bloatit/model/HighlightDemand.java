package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoHighlightDemand;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.demand.DemandImplementation;

public class HighlightDemand extends Identifiable<DaoHighlightDemand> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoHighlightDemand, HighlightDemand> {
        @Override
        public HighlightDemand doCreate(DaoHighlightDemand dao) {
            return new HighlightDemand(dao);
        }
    }

    public static HighlightDemand create(final DaoHighlightDemand dao) {
        return new MyCreator().create(dao);
    }

    public HighlightDemand(Demand demand, int position, String reason, Date activationDate, Date desactivationDate) {
        super(DaoHighlightDemand.createAndPersist(DBRequests.getById(DaoDemand.class, demand.getId()), position, reason, activationDate, desactivationDate));
    }

    private HighlightDemand(DaoHighlightDemand dao) {
        super(dao);
    }

    public int getPosition() {
        return getDao().getPosition();
    }

    public Date getActivationDate() {
        return getDao().getActivationDate();
    }

    public Demand getDemand() {
        return DemandImplementation.create(getDao().getDemand());
    }

    @Override
    protected boolean isMine(Member member) {
        // TODO Auto-generated method stub
        return false;
    }

}
