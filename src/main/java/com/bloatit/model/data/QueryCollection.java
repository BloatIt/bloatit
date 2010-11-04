package com.bloatit.model.data;

import java.util.Iterator;

import org.hibernate.Query;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

public class QueryCollection<T> implements PageIterable<T> {

    private final Query query;
    private final Query sizeQuery;
    private int pageSize;
    private int size;
    private int currentPage;

    /**
     * Use this constructor with string that start with "from ..."
     */
    protected QueryCollection(String queryStr) {
        this(SessionManager.createQuery(queryStr));
    }

    /**
     * Use this constructor with query that start with "from ..."
     */
    protected QueryCollection(Query query) {
        this(query, SessionManager.getSessionFactory().getCurrentSession()
                .createQuery("select count (*) " + query.getQueryString()));
    }

    protected QueryCollection(Query query, Query sizeQuery) {
        pageSize = 0;
        size = -1;
        this.query = query;
        this.sizeQuery = sizeQuery;
    }

    public QueryCollection<T> setEntity(String name, Object entity) {
        query.setEntity(name, entity);
        sizeQuery.setEntity(name, entity);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return query.list().iterator();
    }

    @Override
    public void setPage(int page) {
        currentPage = page;
        query.setFirstResult(page * pageSize);
    }

    @Override
    public void setPageSize(int pageSize) {
        query.setMaxResults(pageSize);
        query.setFetchSize(pageSize);
        this.pageSize = pageSize;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int size() {
        if (size == -1) {
            return size = ((Long) sizeQuery.uniqueResult()).intValue();
        }
        return size;
    }

    @Override
    public int pageNumber() {
        if (pageSize != 0) {
            return size() / pageSize;
        } else {
            return 1;
        }
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }
}
