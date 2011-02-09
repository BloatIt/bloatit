package com.bloatit.model.demand;

import java.util.Iterator;

import com.bloatit.data.DaoDemand;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Demand;
import com.bloatit.model.lists.ListBinder;

public final class DemandList extends ListBinder<Demand, DaoDemand> {

    public DemandList(final PageIterable<DaoDemand> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Demand> createFromDaoIterator(final Iterator<DaoDemand> dao) {
        return new DemandIterator(dao);
    }

    static final class DemandIterator extends com.bloatit.model.lists.IteratorBinder<Demand, DaoDemand> {

        public DemandIterator(final Iterable<DaoDemand> daoIterator) {
            super(daoIterator);
        }

        public DemandIterator(final Iterator<DaoDemand> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Demand createFromDao(final DaoDemand dao) {
            return DemandImplementation.create(dao);
        }

    }

}
