package com.bloatit.framework.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sorter<T, U extends Comparable<U>> {

    private final List<T> outputList;
    private final List<SortEntry> inputList;

    public enum Order {
        ASC,
        DESC,
    }

    public Sorter(List<T> outputList) {
        this.outputList = outputList;
        inputList = new ArrayList<Sorter<T, U>.SortEntry>();
    }

    public void add(T element, U sortkey) {
        inputList.add(new SortEntry(element, sortkey));
    }

    public void performSort(Order order) {
        Collections.sort(inputList, new SortEntryComparator(order));

        for (SortEntry entry : inputList) {
            outputList.add(entry.getElement());
        }
    }

    private class SortEntry implements Comparable<SortEntry> {
        private final T element;
        private final U sortKey;

        SortEntry(T element, U sortKey) {
            this.element = element;
            this.sortKey = sortKey;

        }

        public T getElement() {
            return element;
        }

        private U getSortKey() {
            return sortKey;
        }

        @Override
        public int compareTo(SortEntry o) {
            return sortKey.compareTo(o.getSortKey());
        }
    }

    private class SortEntryComparator implements Comparator<SortEntry> {

        private final Order order;


        private SortEntryComparator(Order order) {
            this.order = order;

        }


        @Override
        public int compare(SortEntry o1, SortEntry o2) {
            if(order == Order.ASC) {
                return o1.compareTo(o2);
            }
            return -o1.compareTo(o2);
        }

    }

}
