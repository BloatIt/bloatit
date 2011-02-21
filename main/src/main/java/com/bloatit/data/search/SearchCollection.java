package com.bloatit.data.search;

import java.util.Iterator;

import org.hibernate.search.FullTextQuery;

import com.bloatit.framework.utils.PageIterable;

/**
 * This is the {@link PageIterable} implementation using the Hibernate Search
 * querying interface.
 */
public class SearchCollection<T> implements PageIterable<T> {

    private final FullTextQuery query;
    private int pageSize;
    private int currentPage;

    protected SearchCollection(final FullTextQuery query) {
        super();
        this.query = query;
        this.pageSize = 0;
        this.currentPage = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Iterator<T> iterator() {
        return query.iterate();
    }

    @Override
    public final void setPageSize(final int pageSize) {
        query.setFetchSize(pageSize);
        query.setMaxResults(pageSize);
        this.pageSize = pageSize;
    }

    @Override
    public final int getPageSize() {
        return this.pageSize;
    }

    @Override
    public final int size() {
        return query.getResultSize();
    }

    @Override
    public final int pageNumber() {
        if (pageSize != 0) {
            // make sure every element is in a page :
            // round to superior.
            return size() / pageSize + (size() % pageSize != 0 ? 1 : 0);
        }
        return 1;
    }

    @Override
    public final void setPage(final int page) {
        currentPage = page;
        query.setFirstResult(page * pageSize);
    }

    @Override
    public final int getCurrentPage() {
        return currentPage;
    }

}
