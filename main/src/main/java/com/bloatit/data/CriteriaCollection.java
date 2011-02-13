package com.bloatit.data;

import java.util.Iterator;

import org.hibernate.Criteria;

import com.bloatit.framework.utils.PageIterable;

/**
 * This is the implementation of the {@link PageIterable} interface using a Hibernate
 * criteria.
 */
public class CriteriaCollection<T> implements PageIterable<T> {

    private final Criteria criteria;
    private int pageSize;
    private int size;
    private int currentPage;

    protected CriteriaCollection(final Criteria criteria) {
        pageSize = 0;
        size = -1;
        this.criteria = criteria;
    }

    protected Criteria getCriteria() {
        return criteria;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<T> iterator() {
        return criteria.list().iterator();
    }

    @Override
    public final void setPage(final int page) {
        currentPage = page;
        criteria.setFirstResult(page * pageSize);
    }

    @Override
    public final void setPageSize(final int pageSize) {
        criteria.setMaxResults(pageSize);
        criteria.setFetchSize(pageSize);
        this.pageSize = pageSize;
    }

    @Override
    public final int getPageSize() {
        return pageSize;
    }

    @Override
    public final int size() {
        if (size == -1) {
            size = criteria.list().size();
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
