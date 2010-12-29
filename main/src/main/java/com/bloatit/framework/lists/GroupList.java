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

}
