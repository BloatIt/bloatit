package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.Group;
import com.bloatit.model.data.DaoGroup;

public class GroupIterator extends com.bloatit.framework.lists.IteratorBinder<Group, DaoGroup> {

    public GroupIterator(final Iterable<DaoGroup> daoIterator) {
        super(daoIterator);
    }

    public GroupIterator(final Iterator<DaoGroup> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected Group createFromDao(final DaoGroup dao) {
        return Group.create(dao);
    }

}
