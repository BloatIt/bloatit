package com.bloatit.rest.list.master;

import java.util.Iterator;

import com.bloatit.data.IdentifiableInterface;
import com.bloatit.framework.rest.RestElement;
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
