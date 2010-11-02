package com.bloatit.model.data;

import java.util.Iterator;

import org.hibernate.Query;

import com.bloatit.common.PageIterable;

public class QueryCollection<T> implements PageIterable<T> {

    private final Query query;
    private int pageSize;

    protected QueryCollection(Query query) {
        pageSize = 0;
        this.query = query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return query.iterate();
    }

    /**
     * by default a this will return a page with all the elements.
     * 
     * @param page the page number
     * @return a list of entity.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Iterable<T> getPage(int page) {
        query.setFirstResult(page * pageSize);
        return query.list();
    }

    @Override
    public void setPageSize(int pageSize) {
        query.setFetchSize(pageSize);
        this.pageSize = pageSize;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long size() {
        // TODO optimize me !
        return query.list().size();
    }

    @Override
    public long pageNumber() {
        if (pageSize != 0) {
            return size() / pageSize;
        } else {
            return 1;
        }
    }

}
