package com.bloatit.framework.webserver.pageparameters;

import java.util.Iterator;

import com.sun.tools.javac.util.List;

public class PageParametersList implements Iterable<PageParameter> {
    List<PageParameter> parameters;

    @Override
    public Iterator<PageParameter> iterator() {
        return parameters.iterator();
    }
}
