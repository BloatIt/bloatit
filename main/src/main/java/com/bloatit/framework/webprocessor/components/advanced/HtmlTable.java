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
import java.util.List;

import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;

/**
 * TODO : Fred has to comment this
 */
public class HtmlTable extends HtmlGenericElement {

    private final HtmlTableModel model;
    private int columnCount;

    public HtmlTable(final HtmlTableModel model) {
        super("table");
        this.model = model;

        generateHeader();
        generateBody();

    }

    private void generateBody() {
        while (model.next()) {
            final HtmlGenericElement tr = new HtmlGenericElement("tr");
            columnCount = model.getColumnCount();

            if (model.getLineCss() != null) {
                tr.setCssClass(model.getLineCss());
            }

            for (int i = 0; i < columnCount; i++) {
                final HtmlGenericElement td = new HtmlGenericElement("td");
                td.add(model.getBody(i));
                if (model.getColumnCss(i) != null) {
                    td.setCssClass(model.getColumnCss(i));
                }
                tr.add(td);
            }
            add(tr);
        }
    }

    private void generateHeader() {
        if (model.hasHeader()) {
            final HtmlGenericElement tr = new HtmlGenericElement("tr");
            columnCount = model.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                final HtmlGenericElement th = new HtmlGenericElement("th");
                th.add(model.getHeader(i));
                tr.add(th);
            }
            add(tr);
        }
    }

    public static abstract class HtmlTableModel {
        public abstract int getColumnCount();

        public abstract XmlNode getHeader(int column);

        public abstract XmlNode getBody(int column);

        public abstract boolean next();

        public boolean hasHeader() {
            return true;
        }

        /**
         * @param column The column number on which we want to get the css 
         */
        public String getColumnCss(final int column) {
            return null;
        }

        public String getLineCss() {
            return null;
        }
    }

    public static class HtmlLineTableModel extends HtmlTableModel {

        private List<HtmlTableLine> lines = new ArrayList<HtmlTableLine>();
        int currentLine = -1;

        public void addLine(final HtmlTableLine line) {
            lines.add(line);
        }

        @Override
        public int getColumnCount() {
            if (lines.size() > currentLine) {
                return lines.get(currentLine).getCells().size();
            }
            return 0;
        }

        @Override
        public XmlNode getHeader(final int column) {
            return null;
        }

        @Override
        public XmlNode getBody(final int column) {
            if (lines.size() > currentLine) {
                return lines.get(currentLine).getCells().get(column).getBody();
            }
            return null;
        }

        @Override
        public boolean next() {
            currentLine++;
            if (lines.size() > currentLine) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasHeader() {
            return false;
        }

        @Override
        public String getColumnCss(final int column) {
            return lines.get(currentLine).getCells().get(column).getCss();
        }

        @Override
        public String getLineCss() {
            return lines.get(currentLine).getCssClass();
        }

        public static class HtmlTableLine {
            private List<HtmlTableCell> cells = new ArrayList<HtmlTableCell>();
            private String css = null;

            public void setCssClass(final String css) {
                this.css = css;
            }

            public String getCssClass() {
                return css;
            }

            public void addCell(final HtmlTableCell cell) {
                cells.add(cell);
            }

            public List<HtmlTableCell> getCells() {
                return cells;
            }
        }

        public static abstract class HtmlTableCell {

            private final String css;

            public HtmlTableCell(final String css) {
                this.css = css;
            }

            public abstract XmlNode getBody();

            public String getCss() {
                return css;
            }

        }

        public static class EmptyCell extends HtmlTableCell {

            public EmptyCell() {
                super("");
            }

            @Override
            public XmlNode getBody() {
                return new PlaceHolderElement();
            }
        }

    }

}
