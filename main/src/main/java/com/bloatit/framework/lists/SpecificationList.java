package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Specification;
import com.bloatit.model.data.DaoSpecification;

public final class SpecificationList extends ListBinder<Specification, DaoSpecification> {

    public SpecificationList(final PageIterable<DaoSpecification> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Specification> createFromDaoIterator(final Iterator<DaoSpecification> dao) {
        return new SpecificationIterator(dao);
    }

    static final class SpecificationIterator extends com.bloatit.framework.lists.IteratorBinder<Specification, DaoSpecification> {

        public SpecificationIterator(final Iterable<DaoSpecification> daoIterator) {
            super(daoIterator);
        }

        public SpecificationIterator(final Iterator<DaoSpecification> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Specification createFromDao(final DaoSpecification dao) {
            return new Specification(dao);
        }

    }

}
