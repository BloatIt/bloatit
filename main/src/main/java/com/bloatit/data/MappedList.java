package com.bloatit.data;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.bloatit.framework.utils.PageIterable;

public class MappedList<T> implements PageIterable<T> {

    private List<T> list;
    private Query sizeQuery;
    private int pageSize;
    private int size;
    private int currentPage;

    public MappedList(final List<T> mappedList) {
        this.list = mappedList;
        this.sizeQuery = SessionManager.createFilter(mappedList, "select count(*)");
        this.pageSize = 0;
        this.size = -1;
    }

    @Override
    public Iterator<T> iterator() {
        return this.list.listIterator(this.pageSize * this.currentPage);
    }

    @Override
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int size() {
        if (this.size == -1) {
            this.size = ((Long) this.sizeQuery.uniqueResult()).intValue();
            return this.size;
        }
        return this.size;
    }

    @Override
    public int pageNumber() {
        if (this.pageSize != 0) {
            return (int) Math.ceil((double) size() / (double) this.pageSize);
        }
        return 1;
    }

    @Override
    public void setPage(final int page) {
        this.currentPage = page;
    }

    @Override
    public int getCurrentPage() {
        return this.currentPage;
    }

}
