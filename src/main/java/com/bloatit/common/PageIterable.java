package com.bloatit.common;

/**
 * A PageIterable should always be an ordered collection.
 * 
 * @author Thomas Guyard
 * 
 * @param <T> the Object stored in the collection.
 */
public interface PageIterable<T> extends Iterable<T> {

    public void setPageSize(int pageSize);

    public int getPageSize();

    public int size();

    public int pageNumber();

	void setPage(int page);
}
