package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;

public abstract class ListBinder<E, DAO> implements PageIterable<E> {

    private final PageIterable<DAO> daoCollection;

    public ListBinder(final PageIterable<DAO> daoCollection) {
        super();
        this.daoCollection = daoCollection;
    }

    @Override
    public Iterator<E> iterator() {
        return createFromDaoIterator(daoCollection.iterator());
    }

    @Override
    public void setPage(final int page) {
        daoCollection.setPage(page);
    }

    @Override
    public void setPageSize(final int pageSize) {
        daoCollection.setPageSize(pageSize);
    }

    @Override
    public int getPageSize() {
        return daoCollection.getPageSize();
    }

    @Override
    public int size() {
        return daoCollection.size();
    }

    @Override
    public int pageNumber() {
        return daoCollection.pageNumber();
    }

    @Override
    public int getCurrentPage() {
        return daoCollection.getCurrentPage();
    }

    protected abstract Iterator<E> createFromDaoIterator(Iterator<DAO> dao);

}
