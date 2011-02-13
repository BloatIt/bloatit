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
