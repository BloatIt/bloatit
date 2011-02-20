package com.bloatit.framework.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HttpParameter implements Iterable<String> {
    private final List<String> values = new LinkedList<String>();

    public HttpParameter(final String value) {
        super();
        values.add(value);
    }

    public boolean isMultiple() {
        return values.size() > 1;
    }

    public String getSimpleValue() {
        return values.get(0);
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    public void add(final String value) {
        values.add(value);
    }
}
