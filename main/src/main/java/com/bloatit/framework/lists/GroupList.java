package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Group;
import com.bloatit.model.data.DaoGroup;

public final class GroupList extends ListBinder<Group, DaoGroup> {

    public GroupList(final PageIterable<DaoGroup> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Group> createFromDaoIterator(final Iterator<DaoGroup> dao) {
        return new GroupIterator(dao);
    }

    static final class GroupIterator extends com.bloatit.framework.lists.IteratorBinder<Group, DaoGroup> {

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

}
