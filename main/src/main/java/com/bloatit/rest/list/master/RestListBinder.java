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

import com.bloatit.data.IdentifiableInterface;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.utils.PageIterable;

public class RestListBinder<T extends RestElement<M>, M extends IdentifiableInterface> implements Iterable<T> {
    private PageIterable<M> collection;

    protected RestListBinder() {
    }

    /**
     * Instantiates a new list binder for model to rest elements
     * 
     * @param daoCollection the dao collection
     */
    public RestListBinder(final PageIterable<M> collection) {
        super();
        this.collection = collection;
    }

    public int size() {
        return collection.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new RestIteratorBinder<T, M>(collection);
    }

}
