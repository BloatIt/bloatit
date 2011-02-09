package com.bloatit.model.demand;

import java.util.Iterator;

import com.bloatit.data.DaoDemand;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.DemandInterface;
import com.bloatit.model.lists.ListBinder;

public final class DemandList extends ListBinder<DemandInterface, DaoDemand> {

    public DemandList(final PageIterable<DaoDemand> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<DemandInterface> createFromDaoIterator(final Iterator<DaoDemand> dao) {
        return new DemandIterator(dao);
    }

    static final class DemandIterator extends com.bloatit.model.lists.IteratorBinder<DemandInterface, DaoDemand> {

        public DemandIterator(final Iterable<DaoDemand> daoIterator) {
            super(daoIterator);
        }

        public DemandIterator(final Iterator<DaoDemand> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected DemandInterface createFromDao(final DaoDemand dao) {
            return Demand.create(dao);
        }

    }

}
