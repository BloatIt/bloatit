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

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.model.ConstructorVisitor;
import com.bloatit.model.Identifiable;

/**
 * The Class IteratorBinder is the base class of the iterator used in the List
 * classes. The List classes transform PageIterable<Dao...> to
 * PageIterable<...>.
 * 
 * @param <E> the Model representation of a Dao element.
 * @param <DAO> the Dao Element. (If <code>E</code> id Demand then
 *            <code>DAO</code> must be DaoDemand.
 */
public class IteratorBinder<E extends Identifiable<DAO>, DAO extends DaoIdentifiable> implements java.util.Iterator<E> {

    /** The dao iterator. */
    private final java.util.Iterator<DAO> daoIterator;

    /**
     * Instantiates a new iterator binder.
     * 
     * @param daoIterator the dao iterator
     */
    public IteratorBinder(final java.util.Iterator<DAO> daoIterator) {
        super();
        this.daoIterator = daoIterator;
    }

    /**
     * Instantiates a new iterator binder.
     * 
     * @param daoIterator the dao iterator
     */
    public IteratorBinder(final Iterable<DAO> daoIterator) {
        super();
        this.daoIterator = daoIterator.iterator();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public final boolean hasNext() {
        return daoIterator.hasNext();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @SuppressWarnings("unchecked")
    @Override
    public final E next() {
        return (E) daoIterator.next().accept(new ConstructorVisitor());
    }

    /*
     * (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public final void remove() {
        daoIterator.remove();
    }
}
