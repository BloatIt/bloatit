package com.bloatit.common;

/**
 * A PageIterable should always be an ordered collection.
 * 
 * @author Thomas Guyard
 * @param <T> the Object stored in the collection.
 */
public interface PageIterable<T> extends Iterable<T> {

    void setPageSize(int pageSize);

    int getPageSize();

    int size();

    int pageNumber();

    void setPage(int page);

    int getCurrentPage();
}
