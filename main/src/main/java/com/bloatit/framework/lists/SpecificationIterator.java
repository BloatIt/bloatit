package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.Specification;
import com.bloatit.model.data.DaoSpecification;

public final class SpecificationIterator extends com.bloatit.framework.lists.IteratorBinder<Specification, DaoSpecification> {

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
