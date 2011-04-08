package com.bloatit.framework.webprocessor.components.advanced;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;

public final class HtmlGenericTableModel<U> extends HtmlTable.HtmlTableModel {

    private U element;
    private final Iterator<U> iterator;
    private final List<Column<U>> columns = new ArrayList<HtmlGenericTableModel.Column<U>>();

    public HtmlGenericTableModel(final Iterable<U> iterable) {
        super();
        this.iterator = iterable.iterator();
    }

    public static abstract class ColumnGenerator<U> {
        public abstract XmlNode getBody(U element);
    }

    public static abstract class StringColumnGenerator<U> extends ColumnGenerator<U> {
        @Override
        public XmlNode getBody(final U element) {
            return new HtmlText(getStringBody(element));
        }

        public abstract String getStringBody(U element);
    }

    private static class Column<U> {
        public final XmlNode header;
        public final ColumnGenerator<U> generator;

        public Column(final XmlNode header, final ColumnGenerator<U> generator) {
            super();
            this.header = header;
            this.generator = generator;
        }
    }

    public void addColumn(final XmlNode name, final ColumnGenerator<U> generator) {
        columns.add(new Column<U>(name, generator));
    }

    public void addColumn(final String name, final ColumnGenerator<U> generator) {
        columns.add(new Column<U>(new HtmlText(name), generator));
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public XmlNode getHeader(final int column) {
        return columns.get(column).header;
    }

    @Override
    public XmlNode getBody(final int column) {
        return columns.get(column).generator.getBody(element);
    }

    @Override
    public boolean next() {
        if (iterator.hasNext()) {
            element = iterator.next();
            return true;
        }
        return false;
    }

}
