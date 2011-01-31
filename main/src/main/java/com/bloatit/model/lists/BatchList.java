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
