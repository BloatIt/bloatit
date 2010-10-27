package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.model.Specification;
import com.bloatit.model.data.DaoSpecification;

public class SpecificationIterator extends com.bloatit.framework.lists.IteratorBinder<Specification, DaoSpecification> {

    public SpecificationIterator(Iterable<DaoSpecification> daoIterator) {
        super(daoIterator);
    }

    public SpecificationIterator(Iterator<DaoSpecification> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected Specification createFromDao(DaoSpecification dao) {
        return new Specification(dao);
    }

}
