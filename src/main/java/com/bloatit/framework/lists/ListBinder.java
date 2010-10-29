package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.IterableFromIterator;
import com.bloatit.common.PageIterable;

public abstract class ListBinder<E, DAO> implements PageIterable<E>{

    private PageIterable<DAO> daoCollection;

    public ListBinder(PageIterable<DAO> daoCollection) {
        super();
        this.daoCollection = daoCollection;
    }

    @Override
    public Iterator<E> iterator() {
        return createFromDaoIterator(daoCollection.iterator());
    }

    @Override
    public Iterable<E> getPage(int page) {
        return new IterableFromIterator<E>(createFromDaoIterator(daoCollection.getPage(page).iterator()));
    }

    @Override
    public void setPageSize(int pageSize) {
        daoCollection.setPageSize(pageSize);
    }

    @Override
    public int getPageSize() {
        return daoCollection.getPageSize();
    }
    
    @Override
    public long size() {
         return daoCollection.size();
    }

    @Override
    public long pageNumber() {
        return daoCollection.pageNumber();
    }

    
    protected abstract Iterator<E> createFromDaoIterator(Iterator<DAO> dao);

}
