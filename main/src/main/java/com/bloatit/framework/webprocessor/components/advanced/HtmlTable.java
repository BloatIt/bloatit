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
 * This class display an html table using a data model (HtmlTableModel) For the
 * header then for each line, the table object will call the next method then
 * use the different getter to get the informations for the current line.
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

                if (model.getId(i) != null) {
                    td.setId(model.getId(i));
                }

                if (model.getColspan(i) != 1) {
                    td.addAttribute("colspan", String.valueOf(model.getColspan(i)));
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

        /**
         * Switch to next line and indicate if the next line exist
         *
         * @return true if there is more line
         */
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

        public int getColspan(int column) {
            return 1;
        }

        public String getId(int column) {
            return null;
        }

    }

    public static class HtmlLineTableModel extends HtmlTableModel {

        private final List<HtmlTableLine> lines = new ArrayList<HtmlTableLine>();
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
        public String getId(int column) {
            return lines.get(currentLine).getCells().get(column).getId();
        }

        @Override
        public int getColspan(final int column) {
            return lines.get(currentLine).getCells().get(column).getColspan();
        }

        @Override
        public String getLineCss() {
            return lines.get(currentLine).getCssClass();
        }

        public static class HtmlTableLine {
            private final List<HtmlTableCell> cells = new ArrayList<HtmlTableCell>();
            private String css = null;

            final public void setCssClass(final String css) {
                this.css = css;
            }

            final public String getCssClass() {
                return css;
            }

            final public void addCell(final HtmlTableCell cell) {
                cells.add(cell);
            }

            final public List<HtmlTableCell> getCells() {
                return cells;
            }
        }

        public static abstract class HtmlTableCell {

            private final String css;
            private String id = null;

            public HtmlTableCell(final String css) {
                this.css = css;
            }

            public abstract XmlNode getBody();

            public String getCss() {
                return css;
            }

            public int getColspan() {
                return 1;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
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
