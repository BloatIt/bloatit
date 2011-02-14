//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoProject;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Project;

/**
 * The Class ProjectList transforms PageIterable<DaoProject> to PageIterable<Project>.
 */
public final class ProjectList extends ListBinder<Project, DaoProject> {

    /**
     * Instantiates a new project list.
     * 
     * @param daoCollection the dao collection
     */
    public ProjectList(final PageIterable<DaoProject> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator)
     */
    @Override
    protected Iterator<Project> createFromDaoIterator(final Iterator<DaoProject> dao) {
        return new ProjectIterator(dao);
    }

    /**
     * The Class ProjectIterator.
     */
    static final class ProjectIterator extends IteratorBinder<Project, DaoProject> {

        /**
         * Instantiates a new project iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public ProjectIterator(final Iterable<DaoProject> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new project iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public ProjectIterator(final Iterator<DaoProject> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object)
         */
        @Override
        protected Project createFromDao(final DaoProject dao) {
            return Project.create(dao);
        }

    }

}
