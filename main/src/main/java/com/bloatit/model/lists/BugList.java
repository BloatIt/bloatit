package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoBug;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Bug;

public final class BugList extends ListBinder<Bug, DaoBug> {

    public BugList(final PageIterable<DaoBug> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Bug> createFromDaoIterator(final Iterator<DaoBug> dao) {
        return new BugIterator(dao);
    }

    static final class BugIterator extends com.bloatit.model.lists.IteratorBinder<Bug, DaoBug> {

        public BugIterator(final Iterable<DaoBug> daoIterator) {
            super(daoIterator);
        }

        public BugIterator(final Iterator<DaoBug> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Bug createFromDao(final DaoBug dao) {
            return Bug.create(dao);
        }

    }

}
