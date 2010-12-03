package com.bloatit.model.data;

import java.util.Collections;
import java.util.Iterator;

import com.bloatit.common.PageIterable;

public class NullCollection<T> implements PageIterable<T> {

    private int pageSize;
    
    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return (Iterator<T>) Collections.emptyList().iterator();
    }

    @Override
    public void setPageSize(int pageSize) {
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
    public void setPage(int page) {
    }

    @Override
    public int getCurrentPage() {
        return 0;
    }
}
