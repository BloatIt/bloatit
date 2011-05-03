//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
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

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(final Object o) {
            if (o == null || o.getClass() != getClass()) {
                return false;
            }
            return compareTo((SortEntry) o) == 0;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((sortKey == null) ? 0 : sortKey.hashCode());
            return result;
        }
    }

    private class SortEntryComparator implements Comparator<SortEntry> {

        private final Order order;

        SortEntryComparator(final Order order) {
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
