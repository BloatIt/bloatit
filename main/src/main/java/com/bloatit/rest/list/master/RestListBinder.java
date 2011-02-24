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
