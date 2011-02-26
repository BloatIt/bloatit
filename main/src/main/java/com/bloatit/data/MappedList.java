package com.bloatit.data;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.bloatit.framework.utils.PageIterable;

public class MappedList<T> implements PageIterable<T> {

    private  List<T> list;
    private Query sizeQuery;
    private int pageSize;
    private int size;
    private int currentPage;

    public MappedList(List<T> mappedList) {
        this.list = mappedList;
        this.sizeQuery = SessionManager.createFilter(mappedList, "select count(*)");
        this.pageSize = 0;
        this.size = -1;
    }

    @Override
    public Iterator<T> iterator() {
        return list.listIterator(pageSize * currentPage);
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
        if (size == -1) {
            size = ((Long) sizeQuery.uniqueResult()).intValue();
            return size;
        }
        return size;
    }

    @Override
    public int pageNumber() {
        if (pageSize != 0) {
            return (int) Math.ceil((double) size() / (double) pageSize);
        }
        return 1;
    }

    @Override
    public void setPage(int page) {
        currentPage = page;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    private class BoundedIterator<E> implements Iterator<E> {

        private  Iterator<E> it;
        int i;

        public BoundedIterator(Iterator<E> it) {
            this(0, it);
        }

        private BoundedIterator(int i, Iterator<E> it) {
            super();
            this.i = i;
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return (i < getPageSize() || getPageSize() == 0) && it.hasNext();
        }

        @Override
        public E next() {
            if (hasNext()) {
                i++;
                return it.next();
            }
            return null;
        }

        @Override
        public void remove() {
            it.remove();
        }
    }

}
