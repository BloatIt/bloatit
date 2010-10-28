package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Specification;
import com.bloatit.model.data.DaoSpecification;

public class SpecificationList extends ListBinder<Specification, DaoSpecification> {

    public SpecificationList(PageIterable<DaoSpecification> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Specification> createFromDaoIterator(Iterator<DaoSpecification> dao) {
        return new SpecificationIterator(dao);
    }

}
