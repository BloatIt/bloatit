/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.rest.list.master;

import java.util.Iterator;

import com.bloatit.framework.restprocessor.RestElement;
import com.bloatit.model.IdentifiableInterface;
import com.bloatit.rest.resources.ModelToRestVisitor;

/**
 * Takes an iterator over a list of <code>M extends Identifiable</code> and
 * converts it to an iterator over a list of RestElements using a visitor to
 * create objects
 * 
 * @param <T> the Type of the RestElement to instantiate
 * @param <M> the Type of the Identifiable (from the model) from which it will
 *            be created
 */
public class RestIteratorBinder<T extends RestElement<M>, M extends IdentifiableInterface> implements Iterator<T> {
    private final java.util.Iterator<M> modelIterator;

    public RestIteratorBinder(final Iterator<M> modelIterator) {
        super();
        this.modelIterator = modelIterator;
    }

    public RestIteratorBinder(final Iterable<M> modelIterable) {
        super();
        this.modelIterator = modelIterable.iterator();
    }

    @Override
    public boolean hasNext() {
        return modelIterator.hasNext();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T next() {
        return (T) modelIterator.next().accept(new ModelToRestVisitor());
    }

    @Override
    public void remove() {
        modelIterator.remove();
    }
}
