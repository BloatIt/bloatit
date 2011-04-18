package com.bloatit.framework.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sorter<T, U extends Comparable<U>> {

    private final List<T> outputList;
    private final List<SortEntry> inputList;

    public enum Order {
        ASC, DESC,
    }

    public Sorter(final List<T> outputList) {
        this.outputList = outputList;
        inputList = new ArrayList<Sorter<T, U>.SortEntry>();
    }

    public void add(final T element, final U sortkey) {
        inputList.add(new SortEntry(element, sortkey));
    }

    public void performSort(final Order order) {
        Collections.sort(inputList, new SortEntryComparator(order));

        for (final SortEntry entry : inputList) {
            outputList.add(entry.getElement());
        }
    }

    private class SortEntry implements Comparable<SortEntry> {
        private final T element;
        private final U sortKey;

        SortEntry(final T element, final U sortKey) {
            this.element = element;
            this.sortKey = sortKey;

        }

        public T getElement() {
            return element;
        }

        private U getSortKey() {
            return sortKey;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(final SortEntry o) {
            return sortKey.compareTo(o.getSortKey());
        }

        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (o == null || o.getClass() != getClass()) {
                return false;
            }
            return compareTo((SortEntry) o) == 0;
        }
    }

    private class SortEntryComparator implements Comparator<SortEntry> {

        private final Order order;

        private SortEntryComparator(final Order order) {
            this.order = order;

        }

        @Override
        public int compare(final SortEntry o1, final SortEntry o2) {
            if (order == Order.ASC) {
                return o1.compareTo(o2);
            }
            return -o1.compareTo(o2);
        }

    }

}
