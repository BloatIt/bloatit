package com.bloatit.model.data;

import java.util.Collections;
import java.util.Iterator;

import com.bloatit.common.PageIterable;

public final class NullCollection<T> implements PageIterable<T> {

    private int pageSize;

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<T> iterator() {
        return (Iterator<T>) Collections.emptyList().iterator();
    }

    @Override
    public final void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public final int getPageSize() {
        return pageSize;
    }

    @Override
    public final int size() {
        return 0;
    }

    @Override
    public final int pageNumber() {
        return 0;
    }

    @Override
    public final void setPage(final int page) {
    }

    @Override
    public final int getCurrentPage() {
        return 0;
    }
}
