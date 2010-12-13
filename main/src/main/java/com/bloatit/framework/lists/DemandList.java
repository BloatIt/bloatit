package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Demand;
import com.bloatit.model.data.DaoDemand;

public class DemandList extends ListBinder<Demand, DaoDemand> {

    public DemandList(final PageIterable<DaoDemand> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Demand> createFromDaoIterator(final Iterator<DaoDemand> dao) {
        return new DemandIterator(dao);
    }

}
