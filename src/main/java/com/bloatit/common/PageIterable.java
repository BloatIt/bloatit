package com.bloatit.common;

import java.util.List;

/**
 * A PageIterable should always be an ordered collection.
 * 
 * @author Thomas Guyard
 * 
 * @param <T> the Object stored in the collection.
 */
public interface PageIterable<T> extends Iterable<T> {

    public List<T> getPage(int page);

    public void setPageSize(int pageSize);

    public int getPageSize();
}
