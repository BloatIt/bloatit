package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoGroup;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Group;

public final class GroupList extends ListBinder<Group, DaoGroup> {

    public GroupList(final PageIterable<DaoGroup> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Group> createFromDaoIterator(final Iterator<DaoGroup> dao) {
        return new GroupIterator(dao);
    }

    static final class GroupIterator extends com.bloatit.model.lists.IteratorBinder<Group, DaoGroup> {

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
