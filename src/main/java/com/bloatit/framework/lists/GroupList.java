package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.model.Group;
import com.bloatit.model.data.DaoGroup;

public class GroupList extends ListBinder<Group, DaoGroup> {

    public GroupList(PageIterable<DaoGroup> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Group> createFromDaoIterator(Iterator<DaoGroup> dao) {
        return new GroupIterator(dao);
    }

}
