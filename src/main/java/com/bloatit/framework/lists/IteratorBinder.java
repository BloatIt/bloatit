package com.bloatit.framework.lists;

public abstract class IteratorBinder<E, DAO> implements java.util.Iterator<E> {

    private java.util.Iterator<DAO> daoIterator;

    public IteratorBinder(java.util.Iterator<DAO> daoIterator) {
        super();
        this.daoIterator = daoIterator;
    }

    public IteratorBinder(Iterable<DAO> daoIterator) {
        super();
        this.daoIterator = daoIterator.iterator();
    }

    @Override
    public boolean hasNext() {
        return daoIterator.hasNext();
    }

    @Override
    public E next()
    {
        return createFromDao(daoIterator.next());
    }

    @Override
    public void remove() {
        daoIterator.remove();
    }
    
    protected abstract E createFromDao(DAO dao);

}
