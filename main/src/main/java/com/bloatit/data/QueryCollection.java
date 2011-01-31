package com.bloatit.data;

import java.util.Iterator;

import org.hibernate.Query;

import com.bloatit.framework.utils.PageIterable;

/**
 * This is the implementation of the {@link PageIterable} interface using a Hibernate HQL
 * query.
 */
public class QueryCollection<T> implements PageIterable<T> {

    private final Query query;
    private final Query sizeQuery;
    private int pageSize;
    private int size;
    private int currentPage;

    /**
     * Use this constructor with string that start with "from ..."
     */
    protected QueryCollection(final String queryStr) {
        this(SessionManager.createQuery(queryStr));
    }

    /**
     * Use this constructor with query that start with "from ..."
     */
    protected QueryCollection(final Query query) {
        this(query, SessionManager.getSessionFactory().getCurrentSession().createQuery("select count (*) " + query.getQueryString()));
    }

    protected QueryCollection(final Query query, final Query sizeQuery) {
        pageSize = 0;
        size = -1;
        this.query = query;
        this.sizeQuery = sizeQuery;
    }

    public final QueryCollection<T> setEntity(final String name, final Object entity) {
        query.setEntity(name, entity);
        sizeQuery.setEntity(name, entity);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<T> iterator() {
        return query.list().iterator();
    }

    @Override
    public final void setPage(final int page) {
        currentPage = page;
        query.setFirstResult(page * pageSize);
    }

    @Override
    public final void setPageSize(final int pageSize) {
        query.setMaxResults(pageSize);
        query.setFetchSize(pageSize);
        this.pageSize = pageSize;
    }

    @Override
    public final int getPageSize() {
        return pageSize;
    }

    @Override
    public final int size() {
        if (size == -1) {
            size = ((Long) sizeQuery.uniqueResult()).intValue();
            return size;
        }
        return size;
    }

    @Override
    public final int pageNumber() {
        if (pageSize != 0) {
            return (int) Math.ceil((double) size() / (double) pageSize);
        }
        return 1;
    }

    @Override
    public final int getCurrentPage() {
        return currentPage;
    }
}
