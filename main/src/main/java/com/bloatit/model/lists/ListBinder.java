package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.framework.utils.PageIterable;

public abstract class ListBinder<E, DAO> implements PageIterable<E> {

    private final PageIterable<DAO> daoCollection;

    public ListBinder(final PageIterable<DAO> daoCollection) {
        super();
        this.daoCollection = daoCollection;
    }

    @Override
    public final Iterator<E> iterator() {
        return createFromDaoIterator(daoCollection.iterator());
    }

    @Override
    public final void setPage(final int page) {
        daoCollection.setPage(page);
    }

    @Override
    public final void setPageSize(final int pageSize) {
        daoCollection.setPageSize(pageSize);
    }

    @Override
    public final int getPageSize() {
        return daoCollection.getPageSize();
    }

    @Override
    public final int size() {
        return daoCollection.size();
    }

    @Override
    public final int pageNumber() {
        return daoCollection.pageNumber();
    }

    @Override
    public final int getCurrentPage() {
        return daoCollection.getCurrentPage();
    }

    protected abstract Iterator<E> createFromDaoIterator(Iterator<DAO> dao);

}
