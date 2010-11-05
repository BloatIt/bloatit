package com.bloatit.model.data;

import java.util.Iterator;

import org.hibernate.search.FullTextQuery;

import com.bloatit.common.PageIterable;

public class SearchCollection<T> implements PageIterable<T> {

    private final FullTextQuery query;
    private int pageSize;
    private int currentPage;

    protected SearchCollection(FullTextQuery query) {
        super();
        this.query = query;
        this.pageSize = 0;
        this.currentPage = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator() {
        return query.iterate();
    }

    @Override
    public void setPageSize(int pageSize) {
        query.setFetchSize(pageSize);
        query.setMaxResults(pageSize);
        this.pageSize = pageSize;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int size() {
        return query.getResultSize();
    }

    @Override
    public int pageNumber() {
        if (pageSize != 0) {
            // make sure every element is in a page :
            // round to superior.
            return size() / pageSize + (size() % pageSize != 0 ? 1 : 0);
        } else {
            return 1;
        }
    }

    @Override
    public void setPage(int page) {
        currentPage = page;
        query.setFirstResult(page * pageSize);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

}
