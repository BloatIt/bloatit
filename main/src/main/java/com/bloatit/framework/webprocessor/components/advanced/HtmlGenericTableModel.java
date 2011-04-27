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
