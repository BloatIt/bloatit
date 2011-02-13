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

import com.bloatit.data.DaoBug;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Bug;

public final class BugList extends ListBinder<Bug, DaoBug> {

    public BugList(final PageIterable<DaoBug> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Bug> createFromDaoIterator(final Iterator<DaoBug> dao) {
        return new BugIterator(dao);
    }

    static final class BugIterator extends com.bloatit.model.lists.IteratorBinder<Bug, DaoBug> {

        public BugIterator(final Iterable<DaoBug> daoIterator) {
            super(daoIterator);
        }

        public BugIterator(final Iterator<DaoBug> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Bug createFromDao(final DaoBug dao) {
            return Bug.create(dao);
        }

    }

}
