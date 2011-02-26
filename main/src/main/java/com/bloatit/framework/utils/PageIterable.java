package com.bloatit.framework.utils;

/**
 * A PageIterable should always be an ordered collection.
 * 
 * @param <T> the Object stored in the collection.
 */
public interface PageIterable<T> extends Iterable<T> {

    /**
     * You should never change the page size during the iteration of this
     * {@link PageIterable}. The behavior is not specified and could result in
     * weird results.
     * 
     * @param pageSize
     */
    void setPageSize(int pageSize);

    int getPageSize();

    int size();

    int pageNumber();

    void setPage(int page);

    int getCurrentPage();
}
