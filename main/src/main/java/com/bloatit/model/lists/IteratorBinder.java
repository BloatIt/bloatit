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

public abstract class IteratorBinder<E, DAO> implements java.util.Iterator<E> {

    private final java.util.Iterator<DAO> daoIterator;

    public IteratorBinder(final java.util.Iterator<DAO> daoIterator) {
        super();
        this.daoIterator = daoIterator;
    }

    public IteratorBinder(final Iterable<DAO> daoIterator) {
        super();
        this.daoIterator = daoIterator.iterator();
    }

    @Override
    public final boolean hasNext() {
        return daoIterator.hasNext();
    }

    @Override
    public final E next() {
        return createFromDao(daoIterator.next());
    }

    @Override
    public final void remove() {
        daoIterator.remove();
    }

    protected abstract E createFromDao(DAO dao);

}
