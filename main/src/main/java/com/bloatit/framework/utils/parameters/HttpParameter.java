package com.bloatit.framework.utils.parameters;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * A class used to describe HttpParameters that can contain many values for one
 * parameter (eg: for checkboxes).
 * </p>
 * <p>
 * It may represent a single value or a multiple value parameter.
 * </p>
 */
public class HttpParameter implements Iterable<String> {
    private final List<String> values = new LinkedList<String>();

    public HttpParameter(final String value) {
        super();
        values.add(value);
    }

    /**
     * <p>
     * Indicates whether the parameter contains multiple values or not
     * </p>
     * 
     * @return <code>true</code> if the parameter contains multiple values,
     *         <code>false</code> otherwise
     */
    public boolean isMultiple() {
        return values.size() > 1;
    }

    /**
     * <p>
     * Returns <b>only</b> the first value in the list of values.
     * </p>
     * 
     * @return the first value for this parameter
     */
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
