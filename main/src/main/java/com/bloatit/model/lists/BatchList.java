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
package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoBatch;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Batch;

public final class BatchList extends ListBinder<Batch, DaoBatch> {

    public BatchList(final PageIterable<DaoBatch> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Batch> createFromDaoIterator(final Iterator<DaoBatch> dao) {
        return new BatchIterator(dao);
    }

    static final class BatchIterator extends com.bloatit.model.lists.IteratorBinder<Batch, DaoBatch> {

        public BatchIterator(final Iterable<DaoBatch> daoIterator) {
            super(daoIterator);
        }

        public BatchIterator(final Iterator<DaoBatch> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Batch createFromDao(final DaoBatch dao) {
            return Batch.create(dao);
        }

    }

}
