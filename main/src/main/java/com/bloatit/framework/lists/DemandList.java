package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Demand;
import com.bloatit.model.data.DaoDemand;

public final class DemandList extends ListBinder<Demand, DaoDemand> {

    public DemandList(final PageIterable<DaoDemand> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Demand> createFromDaoIterator(final Iterator<DaoDemand> dao) {
        return new DemandIterator(dao);
    }

    static final class DemandIterator extends com.bloatit.framework.lists.IteratorBinder<Demand, DaoDemand> {

        public DemandIterator(final Iterable<DaoDemand> daoIterator) {
            super(daoIterator);
        }

        public DemandIterator(final Iterator<DaoDemand> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Demand createFromDao(final DaoDemand dao) {
            return Demand.create(dao);
        }

    }

}
