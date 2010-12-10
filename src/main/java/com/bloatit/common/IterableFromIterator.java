package com.bloatit.common;

import java.util.Iterator;

public class IterableFromIterator<T> implements Iterable<T> {

    private final Iterator<T> it;

    public IterableFromIterator(final Iterator<T> it) {
        super();
        this.it = it;
    }

    @Override
    public Iterator<T> iterator() {
        return it;
    }

}
