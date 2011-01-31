package com.bloatit.data;

import java.util.Collections;
import java.util.Iterator;

import com.bloatit.framework.utils.PageIterable;

public final class NullCollection<T> implements PageIterable<T> {

    private int pageSize;

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return (Iterator<T>) Collections.emptyList().iterator();
    }

    @Override
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int pageNumber() {
        return 0;
    }

    @Override
    public void setPage(final int page) {
        // nothing to do it is empty.
    }

    @Override
    public int getCurrentPage() {
        return 0;
    }
}
