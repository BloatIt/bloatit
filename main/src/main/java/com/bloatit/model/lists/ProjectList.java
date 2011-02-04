package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoProject;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Project;

public final class ProjectList extends ListBinder<Project, DaoProject> {

    public ProjectList(final PageIterable<DaoProject> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Project> createFromDaoIterator(final Iterator<DaoProject> dao) {
        return new ProjectIterator(dao);
    }

    static final class ProjectIterator extends IteratorBinder<Project, DaoProject> {

        public ProjectIterator(final Iterable<DaoProject> daoIterator) {
            super(daoIterator);
        }

        public ProjectIterator(final Iterator<DaoProject> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Project createFromDao(final DaoProject dao) {
            return Project.create(dao);
        }

    }

}
